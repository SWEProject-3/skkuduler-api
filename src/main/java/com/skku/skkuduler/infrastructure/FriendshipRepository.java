package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.friendship.Friendship;
import com.skku.skkuduler.dto.response.FriendInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    // 두 사람이 이미 친구인지 아닌지 확인
    @Query("""
    SELECT COUNT(f) > 0
    FROM friendship f
    WHERE f.status = 'ACCEPTED' AND ((f.fromUserId = :aUserId AND f.toUserId = :bUserId) OR (f.fromUserId = :bUserId AND f.toUserId = :aUserId))
    """)
    boolean existsFriendshipByUserIdsAndAccept(@Param("aUserId") Long aUserId , @Param("bUserId") Long bUserId);

    @Query("""
    SELECT f
    FROM friendship f
    WHERE ((f.fromUserId = :aUserId AND f.toUserId = :bUserId) OR (f.fromUserId = :bUserId AND f.toUserId = :aUserId))
    """)
    Optional<Friendship> findFriendshipByUserIds(@Param("aUserId") Long aUserId , @Param("bUserId") Long bUserId);

    @Query("""
    SELECT new com.skku.skkuduler.dto.response.FriendInfoDto(
        f.friendshipId,
        CAST(CASE WHEN f.fromUserId = :userId THEN f.toUserId ELSE f.fromUserId END AS long),
        CASE WHEN f.fromUserId = :userId THEN u2.name ELSE u1.name END,
        CASE WHEN f.fromUserId = :userId THEN u2.email ELSE u1.email END,
        f.status
    )
    FROM friendship f
    INNER JOIN user u1 ON f.fromUserId = u1.userId AND u1.deletedAt IS NULL
    INNER JOIN user u2 ON f.toUserId = u2.userId AND u2.deletedAt IS NULL
    WHERE f.status = 'ACCEPTED' AND (f.fromUserId = :userId OR f.toUserId = :userId)
    """)
    List<FriendInfoDto> getFriendInfoDtosByUserIdAndAccepted(@Param("userId") Long userId);

    @Query("""
    SELECT new com.skku.skkuduler.dto.response.FriendInfoDto(
        f.friendshipId,
        u.userId,
        u.name,
        u.email,
        f.status
    )
    FROM friendship f
    INNER JOIN user u ON u.userId = f.toUserId AND u.deletedAt IS NULL
    WHERE f.status = 'PENDING' AND f.fromUserId = :fromUserId
    """)
    List<FriendInfoDto> getFriendInfoDtosByFromUserIdAndPending(@Param("fromUserId") Long fromUserId);

    @Query("""
    SELECT new com.skku.skkuduler.dto.response.FriendInfoDto(
        f.friendshipId,
        u.userId,
        u.name,
        u.email,
        f.status
    )
    FROM friendship f
    INNER JOIN user u ON u.userId = f.fromUserId AND u.deletedAt IS NULL
    WHERE f.status = 'PENDING' AND f.toUserId = :toUserId
    """)
    List<FriendInfoDto> getFriendInfoDtosByToUserIdAndPending(@Param("toUserId") Long toUserId);

    List<Friendship> findByToUserIdAndStatus(Long userId, Friendship.FriendshipStatus friendshipStatus);
}