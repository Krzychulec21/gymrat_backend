package com.example.gymrat.repository;

import com.example.gymrat.model.ChatRoom;
import com.example.gymrat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT c FROM ChatRoom c WHERE (c.user1.id = :userId1 AND c.user2.id = :userId2) OR (c.user1.id = :userId2 AND c.user2.id = :userId1)")
    Optional<ChatRoom> findByUsers(Long userId1, Long userId2);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM ChatRoom c WHERE c.id = :roomId AND (c.user1.id = :userId OR c.user2.id = : userId)")
    boolean existsByIdAndUserId(@Param("roomId") Long roomId, @Param("userId") Long userId);
}
