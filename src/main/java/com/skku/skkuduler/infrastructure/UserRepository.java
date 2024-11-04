package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);  // 이메일 중복 체크 메서드
    Optional<User> findByEmail(String email);

    @Query("""
    SELECT COUNT(*) > 0
    FROM user u
    WHERE u.userId = :userId AND u.deletedAt IS NULL
    """)
    boolean existsByUserId(@Param("userId") Long userId);
}
