package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.dto.response.UserProfileDto;
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
    SELECT DISTINCT u
    FROM user u
    LEFT JOIN FETCH u.calendar
    WHERE u.userId = :userId AND u.deletedAt IS NULL
    """)
    @NonNull
    Optional<User> findByIdFetchCalendar(@Param("userId") @NonNull Long userId);

    @Query("""
    SELECT DISTINCT u
    FROM user u
    LEFT JOIN FETCH u.subscriptions
    WHERE u.userId = :userId AND u.deletedAt IS NULL
    """)
    Optional<User> findByIdFetchSubscriptions(@Param("userId") Long userId);
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
    @Query("""
    SELECT new com.skku.skkuduler.dto.response.UserProfileDto(
        u.userId,
        u.name,
        u.email,
        CASE WHEN u.userId = :viewerId THEN true ELSE false END,
        CASE WHEN f IS NOT NULL AND f.status = 'ACCEPTED' THEN true ELSE false END
    )
    FROM user u
    LEFT JOIN friendship f ON (f.toUserId = :userId AND f.fromUserId = :viewerId) OR (f.fromUserId = :userId AND f.toUserId = :viewerId)
    WHERE u.userId = :userId AND u.deletedAt IS NULL
    """)
    Optional<UserProfileDto> getUserProfileDto(@Param("userId") Long userId, @Param("viewerId") Long viewerId);
}
