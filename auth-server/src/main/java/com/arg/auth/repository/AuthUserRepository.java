/*
 * arg license
 *
 */

package com.arg.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.arg.auth.entity.AuthUser;


@Repository
@Transactional
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {


    Optional<AuthUser> findByUsername(String username);

    Optional<AuthUser> findByEmail(String email);

    // Optional<AuthUser> findByMobileNumber(String mobileNumber);

    Optional<List<AuthUser>> findByMobileNumber(String mobileNumner);

    Optional<List<AuthUser>> findByUsernameOrMobileNumber(String username, String mobile_number);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<AuthUser> findByRoles_NameIn(List<String> roles);

}
