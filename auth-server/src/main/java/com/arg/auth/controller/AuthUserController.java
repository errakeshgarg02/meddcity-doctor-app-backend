/*
 * arg license
 *
 */

package com.arg.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arg.auth.configuration.CustomerTokenService;
import com.arg.auth.dto.request.CreateUserRequest;
import com.arg.auth.dto.request.DeleteUserRequest;
import com.arg.auth.dto.request.PasswordRequest;
import com.arg.auth.dto.request.UpdateUserRequest;
import com.arg.auth.dto.response.UserResponse;
import com.arg.auth.entity.AuthUser;
import com.arg.auth.enums.RoleEnum;
import com.arg.auth.helper.AuthEmailHelper;
import com.arg.auth.service.impl.CustomUserDetailsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/oauth/user")
public class AuthUserController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomerTokenService customerTokenService;

    @Autowired
    private AuthEmailHelper authEmailHelper;

    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpServletRequest request) {
        boolean response = customerTokenService.invalidateToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('role_administrator', 'role_admin')")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody CreateUserRequest request) {
        log.debug("inside registerUser method with request {}", request);
        RoleEnum.validateRole(request.getRole());

        return customUserDetailsService.createUser(request)
                .map(user -> UserResponse.builder().username(request.getUsername()).message("User created").build())
                .map(response -> ResponseEntity.ok().body(response))
                .orElseThrow(() -> new RuntimeException("Error creating user with username " + request.getUsername()));
    }

    @GetMapping("/isUserExist")
    public ResponseEntity<Boolean> isUserExist(@RequestParam("input") String input) {
        boolean userExists = customUserDetailsService.isUserExists(input);
        log.debug("AuthUserController.isUserExist response {}", userExists);
        return ResponseEntity.ok().body(userExists);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('role_administrator', 'role_admin')")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UpdateUserRequest request,
            HttpServletRequest servletRequest) {
        log.debug("inside updateUser method with request {}", request);
        if (!ObjectUtils.isEmpty(request.getNewRole())) {
            RoleEnum.validateRole(request.getNewRole());
        }

        return customUserDetailsService.updateUser(request)
                .map(user -> UserResponse.builder().username(request.getUsername()).message("User updated").build())
                .map(response -> ResponseEntity.ok().body(response))

                .orElseThrow(() -> new RuntimeException("Error updating user with username " + request.getUsername()));
    }

    @PutMapping("/sendOtp")
    public ResponseEntity<UserResponse> sendOtp() {

        AuthUser user = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("{}", user);
        return customUserDetailsService.sendOtp(user)
                .map(flag -> UserResponse.builder().username(user.getUsername())
                        .message(String.format("OTP sent to mobile number %s", user.getMobileNumber()))
                        .build())
                .map(response -> ResponseEntity.ok().body(response))
                .orElseThrow(() -> new RuntimeException("Error in sendOtp with username " + user.getUsername()));
    }

    @DeleteMapping("/disableUser/{email}")
    @PreAuthorize("hasAnyAuthority('role_user_administrator')")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable("email") String email) {
        log.debug("Entered deleteUser method with email {}", email);
        customUserDetailsService.deleteUser(email);
        UserResponse response = UserResponse.builder().username(email).message("User disabled").build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/enableUser/{email}")
    @PreAuthorize("hasAnyAuthority('role_user_administrator')")
    public ResponseEntity<UserResponse> enableUser(@PathVariable("email") String email) {
        log.debug("Entered deleteUser method with email {}", email);
        customUserDetailsService.enableUser(email);
        UserResponse response = UserResponse.builder().username(email).message("User enabled").build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<UserResponse> forgetPassword(@RequestBody PasswordRequest request) {
        log.debug("Entered AuthUserController.forgetPassword method with request {}", request);
        return customUserDetailsService.forgetPassword(request)
                .map(user -> UserResponse.builder().username(user.getUsername())
                        .message("OTP sent for reset password").build())
                .map(flag -> ResponseEntity.ok().body(flag)).orElseThrow(
                        () -> new RuntimeException("Error in forget password with username " + request.getUsername()));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<UserResponse> changePassword(@RequestBody PasswordRequest request) {
        return customUserDetailsService.changePassword(request).map(user -> {
            // authEmailHelper.sendMailOnResetPassword(user.getName(), user.getEmail());
            return user;
        }).map(user -> UserResponse.builder().username(request.getUsername()).message("Password changed successfully")
                .build()).map(flag -> ResponseEntity.ok().body(flag))
                .orElseThrow(() -> new RuntimeException(
                        "Error in changing password with username " + request.getUsername()));
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAnyAuthority('role_administrator', 'role_admin')")
    public ResponseEntity<UserResponse> deleteUser(@RequestBody DeleteUserRequest request) {
        log.debug("Entered AuthUserController.deleteUser method with request {}", request);
        return customUserDetailsService.deletUser(request)
                .map(flag -> UserResponse.builder().username(request.getUsername())
                        .message("User deleted successfully!").build())
                .map(flag -> ResponseEntity.ok().body(flag)).orElseThrow(
                        () -> new RuntimeException("Error in deleting user with username " + request.getUsername()));
    }
}
