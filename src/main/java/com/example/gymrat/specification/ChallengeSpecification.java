package com.example.gymrat.specification;

import com.example.gymrat.model.Challenge;
import com.example.gymrat.model.ChallengeStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ChallengeSpecification {

    public static Specification<Challenge> isPublic() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isPublic"));
    }

    public static Specification<Challenge> isPrivate() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isFalse(root.get("isPublic"));
    }

    public static Specification<Challenge> isActive() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("challengeStatus"), ChallengeStatus.ACTIVE);
    }

    public static Specification<Challenge> isFinished() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("challengeStatus"), ChallengeStatus.FINISHED);
    }

    public static Specification<Challenge> hasValidStartDate() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), LocalDate.now());
    }

    public static Specification<Challenge> hasValidEndDate() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), LocalDate.now());
    }

    public static Specification<Challenge> belongsToUser(Long userId) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.isMember(userId, root.join("challengeParticipants").get("user").get("id"));
        };
    }

    public static Specification<Challenge> notBelongsToUser(Long userId) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.not(cb.isMember(userId, root.join("challengeParticipants").get("user").get("id")));
        };
    }


}
