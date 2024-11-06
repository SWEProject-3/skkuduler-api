package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
    SELECT u
    FROM user u
    WHERE u.userId = :userId AND u.deletedAt IS NULL
    """)
    @NonNull
    Optional<User> findById(@Param("userId") @NonNull Long userId);

    @Query("""
    SELECT COUNT(*) > 0
    FROM user u
    WHERE u.email = :email AND u.deletedAt IS NULL
    """)
    boolean existsByEmail(@Param("email") String email);

    @Query("""
    SELECT u
    FROM user u
    WHERE u.email = :email AND u.deletedAt IS NULL
    """)
    Optional<User> findByEmail(@Param("email") String email);

    @Query("""
    SELECT COUNT(*) > 0
    FROM user u
    WHERE u.userId = :userId AND u.deletedAt IS NULL
    """)
    boolean existsByUserId(@Param("userId") Long userId);
}
