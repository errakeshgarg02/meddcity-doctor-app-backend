/*
 * arg license
 *
 */

package com.arg.auth.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.arg.auth.dto.request.CreateUserRequest;
import com.arg.auth.dto.request.DeleteUserRequest;
import com.arg.auth.dto.request.PasswordRequest;
import com.arg.auth.dto.request.UpdateUserRequest;
import com.arg.auth.entity.AuthUser;
import com.arg.auth.enums.MessageType;
import com.arg.auth.enums.RoleEnum;
import com.arg.auth.enums.UserType;
import com.arg.auth.exception.ArgException;
import com.arg.auth.exception.BadRequestException;
import com.arg.auth.helper.AuthHelper;
import com.arg.auth.helper.ConverterHelper;
import com.arg.auth.repository.AuthUserRepository;
import com.arg.auth.repository.RoleRepository;
import com.arg.auth.service.IMfaService;
import com.arg.auth.utils.MapperUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private ConverterHelper converterHelper;

    @Autowired
    private IMfaService mfaService;

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MapperUtil mapperUtil;

    @Override
    public UserDetails loadUserByUsername(String input) {
        Optional<AuthUser> user = getUser(input);

        new AccountStatusUserDetailsChecker().check(user.get());

        return user.get();
    }

    private Optional<AuthUser> getUser(String input) {
        Optional<AuthUser> user = null;
        if (input.contains("@"))
            user = userRepository.findByEmail(input);
        else
            user = userRepository.findByUsername(input);

        if (!user.isPresent())
            throw new ArgException("User does not exists");
        return user;
    }

    private Optional<AuthUser> getUserByUsernameOrMobileNumberAndType(String input, UserType userType) {
        Optional<AuthUser> user = userRepository.findByUsernameOrMobileNumber(input, input).flatMap(list -> {
            return list.stream().filter(u -> {
                String data = u.getData();
                if (!ObjectUtils.isEmpty(data)) {
                    HashMap<String, String> map = mapperUtil.toObject(data, HashMap.class);
                    if (userType.name().equalsIgnoreCase(map.get("user_type"))) {
                        return true;
                    }
                }

                return false;
            }).findFirst();
        });

        if (!user.isPresent())
            throw new ArgException("User does not exists", HttpStatus.NOT_FOUND);
        return user;
    }

    @Transactional
    public Optional<AuthUser> createUser(CreateUserRequest request) {
        log.debug("inside createUser method");
        if (isUserExists(request.getUsername())) {
            throw new BadRequestException("User with email id or username already exists");
        }
        AuthUser user = conversionService.convert(request, AuthUser.class);
        return roleRepository.findByName(request.getRole()).map(role -> {
            user.setRoles(Stream.of(role).collect(Collectors.toSet()));
            return user;
        }).map(userRepository::save);
    }

    public boolean isUserExists(String input) {
        if (input.contains("@")) {
            return userRepository.existsByEmail(input);
        }
        return userRepository.existsByUsername(input);
    }

    @Transactional
    public Optional<AuthUser> updateUser(UpdateUserRequest request) {
        if (!this.isUserExists(request.getUsername())) {
            throw new BadRequestException("User does not exist");
        }
        return userRepository.findByUsername(request.getUsername()).map(user -> {
            if (!ObjectUtils.isEmpty(request.getCurrentPassword())) {
                this.authenticateUser(request.getUsername(), request.getCurrentPassword());
            }

            return converterHelper.updateUserData(user, request);

        }).flatMap(user -> {
            if (!ObjectUtils.isEmpty(request.getNewRole())) {
                return roleRepository.findByName(request.getNewRole()).map(role -> {
                    user.setRoles(Stream.of(role).collect(Collectors.toSet()));
                    return user;
                });
            }
            return Optional.of(user);
        }).map(userRepository::save);
    }

    public Authentication authenticateUser(String username, String password) {
        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        try {
            userAuth = this.authenticationManager.authenticate(userAuth);
            log.debug("isUserAuthenticated : {}", userAuth.isAuthenticated());
        } catch (AccountStatusException | BadCredentialsException e) {
            throw new InvalidGrantException(e.getMessage());
        }
        return userAuth;
    }

    @Transactional
    public Optional<Boolean> sendOtp(AuthUser user) {
        mfaService.sendVerificationCode(user, MessageType.MFA);
        return Optional.of(Boolean.TRUE);
    }

    @Transactional
    public Boolean deleteUser(String email) {
        return userRepository.findByEmail(email).map(user -> {
            user.setEnabled(false);
            userRepository.save(user);
            return Boolean.TRUE;
        }).orElseThrow(() -> new BadRequestException("Bad Request to disable the user " + email));
    }

    @Transactional
    public Boolean enableUser(String email) {
        return userRepository.findByEmail(email).map(user -> {
            user.setEnabled(true);
            userRepository.save(user);
            return Boolean.TRUE;
        }).orElseThrow(() -> new BadRequestException("Bad Request to enable the user " + email));
    }

    @Transactional
    public Optional<AuthUser> forgetPassword(PasswordRequest request) {
        log.debug("Entered CustomUserDetailsService.forgetPassword method with request {}", request);
        return this.getUserByUsernameOrMobileNumberAndType(request.getUsername(), request.getUserType()).map(user -> {
            log.debug("CustomUserDetailsService.forgetPassword method with username {}", user.getUsername());
            mfaService.sendVerificationCode(user, MessageType.FORGET_PWD);
            return user;
        });
    }

    @Transactional
    public Optional<AuthUser> changePassword(PasswordRequest request) {
        return this.getUserByUsernameOrMobileNumberAndType(request.getUsername(), request.getUserType())
                .filter(user -> authHelper.verifyMfa(user, request.getOtp())).map(user -> {
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    userRepository.save(user);
                    return user;
                });
    }

    public Optional<List<AuthUser>> findAdminUsers() {
        List<AuthUser> findByAuthorities = userRepository
                .findByRoles_NameIn(Arrays.asList(RoleEnum.ROLE_ADMIN.getValue()));
        return Optional.ofNullable(findByAuthorities);
    }

    @Transactional
    public Optional<Boolean> deletUser(DeleteUserRequest deleteUserRequest) {
        return userRepository.findByUsername(deleteUserRequest.getUsername()).map(user -> {
            userRepository.deleteById(user.getId());
            return Boolean.TRUE;
        });
    }
}
