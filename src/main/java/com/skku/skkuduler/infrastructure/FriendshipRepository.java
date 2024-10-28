package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.friendship.Friendship;
import com.skku.skkuduler.domain.friendship.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    // 두 사람이 이미 친구인지 아닌지 확인
    @Query("""
    SELECT COUNT(f) > 0
    FROM friendship f
    WHERE f.status = 'ACCEPTED' AND ((f.fromUserId = :aUserId AND f.toUserId = :bUserId) OR (f.fromUserId = :bUserId AND f.toUserId = :aUserId))
    """)
    boolean existsFriendshipByUserIds(@Param("aUserId") Long aUserId , @Param("bUserId") Long bUserId);

    @Query("SELECT f FROM friendship f WHERE (f.fromUserId = :userId OR f.toUserId = :userId) AND f.status = :status")
    List<Friendship> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") FriendshipStatus status);

    @Query("SELECT f FROM friendship f WHERE (f.fromUserId = :userId) AND f.status = :status")
    List<Friendship> findBySentUserIdAndStatus(@Param("userId") Long userId, @Param("status") FriendshipStatus status);

    @Query("SELECT f FROM friendship f WHERE (f.toUserId = :userId) AND f.status = :status")
    List<Friendship> findByReceivedUserIdAndStatus(@Param("userId") Long userId, @Param("status") FriendshipStatus status);


    // 내가 보내고 받은 친구요청 조회하기
    @Query("""
    SELECT f FROM friendship f 
    WHERE f.fromUserId = :userId OR f.toUserId = :userId
    """)
    List<Friendship> findAllByUserId(@Param("userId") Long userId);

    // 내가 보낸 친구요청 조회하기
    @Query("""
    SELECT f FROM friendship f 
    WHERE f.fromUserId = :fromUserId AND f.status = 'PENDING'
    """)
    List<Friendship> findByFromUserId(@Param("fromUserId") Long fromUserId);

    // 내가 받은 친구요청 조회하기
    @Query("""
    SELECT f FROM friendship f 
    WHERE f.toUserId = :toUserId AND f.status = 'PENDING'
    """)
    List<Friendship> findByToUserId(@Param("toUserId") Long toUserId);

    @Query("""
    SELECT f FROM friendship f 
    WHERE (f.fromUserId = :userId OR f.toUserId = :userId) 
    AND f.status = 'ACCEPTED'
    """)
    List<Friendship> findFriendsByUserId(@Param("userId") Long userId);
}