package com.example.gymrat.service;

import com.example.gymrat.DTO.trainingPlan.*;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.mapper.TrainingPlanMapper;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.ExerciseRepository;
import com.example.gymrat.repository.FavoriteTrainingPlanRepository;
import com.example.gymrat.repository.TrainingPlanLikeRepository;
import com.example.gymrat.repository.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final UserService userService;
    private final TrainingPlanMapper trainingPlanMapper;
    private final ExerciseRepository exerciseRepository;
    private final CommentService commentService;
    private final LikeService likeService;
    private final TrainingPlanLikeRepository trainingPlanLikeRepository;
    private final FavoriteTrainingPlanRepository favoriteTrainingPlanRepository;

    public void saveTrainingPlan(CreateTrainingPlanDTO dto) {
        User currentUser = userService.getCurrentUser();

        List<Long> exerciseIds = dto.exercises().stream()
                .map(ExerciseInPlanDTO::exerciseId)
                .toList();

        List<Exercise> exercises = exerciseRepository.findAllById(exerciseIds);

        // Validate exercises
        if (exercises.size() != exerciseIds.size()) {
            throw new ResourceNotFoundException("One or more exercises not found");
        }

        TrainingPlan trainingPlan = trainingPlanMapper.toEntity(dto, exercises);
        trainingPlan.setAuthor(currentUser);
        trainingPlan.setDifficultyLevel(calculateDifficultyOfTrainingPlan(trainingPlan));

        trainingPlanRepository.save(trainingPlan);
    }

    public TrainingPlanResponseDTO getTrainingPlanById(Long id) {
        User user = userService.getCurrentUser();
        Optional<TrainingPlanLike> likeOptional = trainingPlanLikeRepository.findTrainingPlanLikeByUserIdAndTrainingPlanId(user.getId(), id);
        String userReaction = likeOptional.map(like -> like.getIsLike() ? "like" : "dislike").orElse(null);

        TrainingPlan trainingPlan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training plan with given ID does not exist"));

        int likeCount = likeService.getLikeCount(id);
        List<CommentResponseDTO> comments = commentService.getComments(id, 0, Integer.MAX_VALUE, "dateCreated", "asc").getContent();

        return trainingPlanMapper.mapToResponseDTO(trainingPlan, likeCount, comments, userReaction);
    }

    public int calculateDifficultyOfTrainingPlan(TrainingPlan trainingPlan) {
        return Math.round(
                (float) trainingPlan.getExercisesInPlan()
                        .stream()
                        .map(exerciseInPlan -> exerciseInPlan.getExercise().getExerciseInfo().getDifficultyLevel())
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0)
        );
    }


    public Page<TrainingPlanSummaryDTO> getAllTrainingPlans(
            int page,
            int size,
            String sortField,
            String sortDirection,
            Set<CategoryName> categories,
            List<Integer> difficultyLevels,
            String authorNickname,
            Boolean onlyFavorite) {

        Specification<TrainingPlan> specification = (root, query, criteriaBuilder) -> {
            assert query != null;
            query.distinct(true);
            return null;
        };

        //set specification to filter
        if (categories != null && !categories.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) -> {
                return root.join("categories").in(categories);
            });
        }

        if (difficultyLevels != null && !difficultyLevels.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) -> {
                return root.get("difficultyLevel").in(difficultyLevels);
            });
        }

        if (authorNickname != null && !authorNickname.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) -> {
                return criteriaBuilder.like(
                        root.get("author").get("nickname"),
                        "%" + authorNickname + "%");
            });
        }

        User user = userService.getCurrentUser();
        if (Boolean.TRUE.equals(onlyFavorite)) {
            List<Long> favoriteTrainingPlanIds = favoriteTrainingPlanRepository.findByUserId(user.getId())
                    .stream()
                    .map(favorite -> favorite.getTrainingPlan().getId())
                    .toList();
            specification = specification.and(((root, query, criteriaBuilder) -> {
                return root.get("id").in(favoriteTrainingPlanIds);
            }));
        }

        Sort sort = switch (sortField) {
            case "likeCount" -> Sort.by(Sort.Direction.fromString(sortDirection), "likeCount");
            case "difficultyLevel" -> Sort.by(Sort.Direction.fromString(sortDirection), "difficultyLevel");
            case "name" -> Sort.by(Sort.Direction.fromString(sortDirection), "name");
            default -> Sort.by(Sort.Direction.fromString(sortDirection), "likeCount");
        };

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TrainingPlan> trainingPlans = trainingPlanRepository.findAll(specification, pageable);

        User currentUser = userService.getCurrentUser();
        List<FavoriteTrainingPlan> favoriteTrainingPlans = favoriteTrainingPlanRepository.findByUserId(currentUser.getId());
        Set<Long> favoritePlanIds = favoriteTrainingPlans.stream()
                .map(favoriteTrainingPlan -> favoriteTrainingPlan.getTrainingPlan().getId())
                .collect(Collectors.toSet());

        return trainingPlans.map(trainingPlan -> {
            boolean isFavorite = favoritePlanIds.contains(trainingPlan.getId());
            return trainingPlanMapper.mapToSummaryDTO(trainingPlan, isFavorite);
        });
    }


//    public Page<TrainingPlanSummaryDTO> getTrainingPlansByUser(Long userId, int page, int size, String sortField, String sortDirection) {
//        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
//                Sort.by(sortField).descending() : Sort.by(sortField).ascending();
//
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        Page<TrainingPlan> trainingPlansPage = trainingPlanRepository.findByAuthorId(userId, pageable);
//
//        return trainingPlansPage.map(trainingPlanMapper::mapToSummaryDTO);
//    }
    //TODO: check if is it necessary

    public void updateTrainingPlan(Long planId, UpdateTrainingPlanDTO dto) {
        User currentUser = userService.getCurrentUser();

        TrainingPlan trainingPlan = trainingPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Training plan with given ID does not exist"));

        if (!trainingPlan.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only edit your own training plans");
        }

        List<Long> exerciseIds = dto.exercises().stream()
                .map(ExerciseInPlanDTO::exerciseId)
                .toList();

        List<Exercise> exercises = exerciseRepository.findAllById(exerciseIds);

        if (exercises.size() != exerciseIds.size()) {
            throw new ResourceNotFoundException("One or more exercises not found");
        }

        trainingPlan.setName(dto.name());
        trainingPlan.setDescription(dto.description());

        trainingPlan.getExercisesInPlan().clear();
        Set<CategoryName> categories = new HashSet<>();

        List<ExerciseInPlan> exercisesInPlan = dto.exercises().stream().map(exerciseInPlanDTO -> {
            ExerciseInPlan exerciseInPlan = new ExerciseInPlan();

            Exercise exercise = exercises.stream()
                    .filter(e -> e.getId().equals(exerciseInPlanDTO.exerciseId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

            categories.add(exercise.getCategory());
            exerciseInPlan.setExercise(exercise);
            exerciseInPlan.setCustomInstructions(exerciseInPlanDTO.customInstructions());
            exerciseInPlan.setTrainingPlan(trainingPlan);

            return exerciseInPlan;
        }).toList();

        trainingPlan.getExercisesInPlan().addAll(exercisesInPlan);
        trainingPlan.setCategories(categories);

        trainingPlan.setDifficultyLevel(calculateDifficultyOfTrainingPlan(trainingPlan));

        trainingPlanRepository.save(trainingPlan);
    }

    public void deleteTrainingPlan(Long planId) {
        User currentUser = userService.getCurrentUser();

        TrainingPlan trainingPlan = trainingPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Training plan with given ID does not exist"));

        if (!trainingPlan.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only delete your own training plans");
        }

        trainingPlanRepository.delete(trainingPlan);
    }

    public void toggleFavorite(Long trainingPlanId) {
        User user = userService.getCurrentUser();

        Optional<FavoriteTrainingPlan> favorite = favoriteTrainingPlanRepository.findByUserIdAndTrainingPlanId(user.getId(), trainingPlanId);

        if (favorite.isPresent()) {
            favoriteTrainingPlanRepository.delete(favorite.get());
        }
        else {
            TrainingPlan trainingPlan = trainingPlanRepository.findById(trainingPlanId)
                    .orElseThrow(() -> new ResourceNotFoundException("Training plan with given ID does not exist"));

            FavoriteTrainingPlan newFavorite = new FavoriteTrainingPlan();
            newFavorite.setUser(user);
            newFavorite.setTrainingPlan(trainingPlan);
            favoriteTrainingPlanRepository.save(newFavorite);
        }
    }
}
