package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.friendship.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
}
