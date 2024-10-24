package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.friendship.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("""
    SELECT COUNT(f) > 0
    FROM friendship f
    WHERE f.status = 'ACCEPTED' AND ((f.fromUserId = :aUserId AND f.toUserId = :bUserId) OR (f.fromUserId = :bUserId AND f.toUserId = :aUserId))
    """)
    boolean existsFriendshipByUserIds(@Param("aUserId") Long aUserId , @Param("bUserId") Long bUserId);
}
