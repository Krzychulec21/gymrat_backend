package com.example.gymrat.specification;

import com.example.gymrat.model.Challenge;
import com.example.gymrat.model.ChallengeParticipant;
import com.example.gymrat.model.ChallengeStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Set;

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
            Join<Challenge, ChallengeParticipant> participants = root.join("challengeParticipants");
            return cb.equal(participants.get("user").get("id"), userId);
        };
    }


    public static Specification<Challenge> notBelongsToUser(Long userId) {
        return (root, query, cb) -> {
            query.distinct(true);

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<ChallengeParticipant> participants = subquery.from(ChallengeParticipant.class);
            subquery.select(participants.get("challenge").get("id"))
                    .where(
                            cb.equal(participants.get("user").get("id"), userId),
                            cb.equal(participants.get("challenge").get("id"), root.get("id"))
                    );

            return cb.not(cb.exists(subquery));
        };
    }


    public static Specification<Challenge> authorIn(Set<Long> userIds) {
        return (root, query, cb) -> root.get("author").get("id").in(userIds);
    }

    public static Specification<Challenge> exerciseCategoryEquals(String categoryName) {
        return (root, query, cb) -> {
            if (categoryName == null || categoryName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("exercise").get("category").as(String.class), categoryName);
        };
    }

    public static Specification<Challenge> typeEquals(String challengeTypeName) {
        return (root, query, cb) -> {
            if (challengeTypeName == null || challengeTypeName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("challengeType").get("name"), challengeTypeName);
        };
    }

    public static Specification<Challenge> filterPublic(Boolean isPublic) {
        return (root, query, criteriaBuilder) -> {
            if (isPublic == null) {
                return criteriaBuilder.conjunction();
            }
            return isPublic ? criteriaBuilder.isTrue(root.get("isPublic")) : criteriaBuilder.isFalse(root.get("isPublic"));
        };
    }

    public static Specification<Challenge> hasExpiredEndDate() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("endDate"), LocalDate.now());
    }


}
