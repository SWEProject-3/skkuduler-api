package com.skku.skkuduler.infrastructure;


import com.skku.skkuduler.domain.like.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByUserIdAndEventId(Long UserId, Long EventId);
}
