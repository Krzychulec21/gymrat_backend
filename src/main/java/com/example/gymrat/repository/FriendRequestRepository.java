package com.example.gymrat.repository;

import com.example.gymrat.DTO.friends.PendingFriendRequestDTO;
import com.example.gymrat.model.FriendRequest;
import com.example.gymrat.model.RequestStatus;
import com.example.gymrat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<PendingFriendRequestDTO> findByReceiver_EmailAndStatus(String receiverEmail, RequestStatus status);
    List<FriendRequest> findBySender_EmailAndStatus(String senderEmail, RequestStatus status);
    Optional<FriendRequest> findBySender_EmailAndReceiver_Email(String senderEmail, String receiverEmail);

    @Modifying
    @Transactional
    @Query("DELETE FROM FriendRequest fr WHERE (fr.sender.id = :userId AND fr.receiver.id = :friendId) OR (fr.sender.id = :friendId AND fr.receiver.id = :userId)")
    void deleteAllBetweenUsers(@Param("userId") Long userId, @Param("friendId") Long friendId);


    List<FriendRequest> findBySender(User currentUser);
}
