package com.example.gymrat.repository;

import com.example.gymrat.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String username);

    @Query("SELECT f FROM User u JOIN u.friends f WHERE u.id = :userId")
    List<User> findFriendsByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM User u WHERE (LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND u.id <> :currentUserId")
    Page<User> searchUsers(@Param("query") String query, @Param("currentUserId") Long currentUserId, Pageable pageable);

    @Query("SELECT u, MAX(m.timestamp) as latestMessageTimestamp FROM User u " +
            "JOIN u.personalInfo pi " +
            "LEFT JOIN Message m ON ((m.sender.id = u.id AND m.receiver.id = :currentUserId) " +
            "OR (m.sender.id = :currentUserId AND m.receiver.id = u.id)) " +
            "WHERE u.id IN (SELECT f.id FROM User usr JOIN usr.friends f WHERE usr.id = :currentUserId) " +
            "AND (YEAR(CURRENT_DATE) - YEAR(pi.dateOfBirth) BETWEEN :minAge AND :maxAge) " +
            "GROUP BY u.id")
    Page<Object[]> findFriendsWithLatestMessageTimestampAndAgeRange(
            @Param("currentUserId") Long currentUserId,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            Pageable pageable);


}
