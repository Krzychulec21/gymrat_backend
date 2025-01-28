package com.example.gymrat.service;

import com.example.gymrat.DTO.workout.ExerciseSessionDTO;
import com.example.gymrat.DTO.workout.WorkoutSessionDTO;
import com.example.gymrat.DTO.workout.WorkoutSessionResponseDTO;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.mapper.WorkoutMapper;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.ExerciseRepository;
import com.example.gymrat.repository.ExerciseSessionRepository;
import com.example.gymrat.repository.PostRepository;
import com.example.gymrat.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final ExerciseRepository exerciseRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final ExerciseSessionRepository exerciseSessionRepository;
    private final UserService userService;
    private final WorkoutMapper workoutMapper;
    private final PostRepository postRepository;
    private final ChallengeScoreUpdateService challengeScoreUpdateService;


    public Long saveWorkout(WorkoutSessionDTO workoutSessionDTO) {
        WorkoutSession workoutSession = workoutMapper.mapToEntity(workoutSessionDTO);
        User user = userService.getCurrentUser();
        workoutSession.setUser(user);

        challengeScoreUpdateService.updateUserChallengesScore(workoutSession, null);

        workoutSessionRepository.save(workoutSession);

        return workoutSession.getId();
    }


    public Integer getNumberOfUserWorkouts() {
        User user = userService.getCurrentUser();
        int count = workoutSessionRepository.countAllByUserId(user.getId());
        return count;
    }

    public Double getTotalWeightLiftedByUser() {
        User user = userService.getCurrentUser();
        return workoutSessionRepository.findTotalWeightLiftedByUser(user.getId());
    }

    public LocalDate getDateOfTheLastWorkout() {
        User user = userService.getCurrentUser();
        return workoutSessionRepository.findFirstByUserIdOrderByDateDesc(user.getId())
                .map(WorkoutSession::getDate)
                .orElse(null);
    }

    public List<CategoryPercentage> getTopCategoriesForUser() {
        User user = userService.getCurrentUser();

        List<Object[]> results = workoutSessionRepository.findTopCategoriesByUserIdWithSetCount(user.getId());
        results = results.stream().toList();

        int totalSets = results.stream()
                .mapToInt(result -> ((Number) result[1]).intValue())
                .sum();

        List<CategoryPercentage> categories = new ArrayList<>();
        for (Object[] result : results) {
            String category = ((CategoryName) result[0]).name();
            int setCount = ((Number) result[1]).intValue();
            double percentage = Math.round((double) setCount / totalSets * 100);


            categories.add(new CategoryPercentage(category, percentage));
        }

        return categories;
    }

    public Page<WorkoutSessionResponseDTO> getUserWorkouts(int page, int size, String sortBy, String sortDir) {
        User user = userService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Page<WorkoutSession> workoutSessionPage = workoutSessionRepository.findAllByUserId(user.getId(), pageable);
        return workoutSessionPage.map(workoutMapper::mapToResponseDTO);
    }

    public void updateWorkout(Long id, WorkoutSessionDTO workoutSessionDTO) {
        User user = userService.getCurrentUser();

        WorkoutSession existingWorkoutSession = workoutSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout session not found"));

        WorkoutSession oldWorkout = deepCopyWorkoutSession(existingWorkoutSession);

        if (!existingWorkoutSession.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this workout session");
        }

        existingWorkoutSession.setDate(workoutSessionDTO.date());
        existingWorkoutSession.setNote(workoutSessionDTO.note());


        List<ExerciseSession> existingExerciseSessions = existingWorkoutSession.getExerciseSessions();
        if (existingExerciseSessions == null) {
            existingExerciseSessions = new ArrayList<>();
            existingWorkoutSession.setExerciseSessions(existingExerciseSessions);
        } else {
            existingExerciseSessions.clear();
        }

        for (ExerciseSessionDTO esDTO : workoutSessionDTO.exerciseSessions()) {
            Exercise exercise = exerciseRepository.findById(esDTO.exerciseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

            ExerciseSession exerciseSession = new ExerciseSession();
            exerciseSession.setExercise(exercise);
            exerciseSession.setWorkoutSession(existingWorkoutSession);

            List<ExerciseSet> sets = esDTO.sets().stream().map(setDTO -> {
                ExerciseSet exerciseSet = new ExerciseSet();
                exerciseSet.setReps(setDTO.reps());
                exerciseSet.setWeight(setDTO.weight());
                exerciseSet.setExerciseSession(exerciseSession);
                return exerciseSet;
            }).collect(Collectors.toList());

            exerciseSession.setSets(sets);
            existingExerciseSessions.add(exerciseSession);
        }

        challengeScoreUpdateService.updateUserChallengesScore(existingWorkoutSession, oldWorkout);

        workoutSessionRepository.save(existingWorkoutSession);
    }

    private WorkoutSession deepCopyWorkoutSession(WorkoutSession original) {
        WorkoutSession copy = new WorkoutSession();
        copy.setDate(original.getDate());
        copy.setNote(original.getNote());

        List<ExerciseSession> copiedSessions = original.getExerciseSessions().stream()
                .map(es -> {
                    ExerciseSession copiedSession = new ExerciseSession();
                    copiedSession.setExercise(es.getExercise());
                    copiedSession.setWorkoutSession(copy);

                    List<ExerciseSet> copiedSets = es.getSets().stream().map(set -> {
                        ExerciseSet copiedSet = new ExerciseSet();
                        copiedSet.setReps(set.getReps());
                        copiedSet.setWeight(set.getWeight());
                        copiedSet.setExerciseSession(copiedSession);
                        return copiedSet;
                    }).collect(Collectors.toList());

                    copiedSession.setSets(copiedSets);
                    return copiedSession;
                }).collect(Collectors.toList());

        copy.setExerciseSessions(copiedSessions);
        return copy;
    }


    public void deleteWorkout(Long id) {
        User user = userService.getCurrentUser();

        WorkoutSession existingWorkoutSession = workoutSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout session not found"));

        if (!existingWorkoutSession.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this workout session");
        }

        workoutSessionRepository.delete(existingWorkoutSession);
    }

    public WorkoutSessionResponseDTO getWorkoutById(Long id) {
        Post post = postRepository.findByWorkoutSessionId(id).orElse(null);
        if (post == null) {
            throw new AccessDeniedException("You do not have permission to this resource");
        }
        WorkoutSession workoutSession = workoutSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout session with given ID does not exist"));

        return workoutMapper.mapToResponseDTO(workoutSession);
    }

    public List<CategoryCount> getTrainedCategoriesCount() {
        User user = userService.getCurrentUser();
        List<Object[]> results = workoutSessionRepository.findTrainedCategoriesCount(user.getId());

        return results.stream()
                .map(result -> new CategoryCount(((CategoryName) result[0]).name(), ((Number) result[1]).intValue()))
                .collect(Collectors.toList());
    }

    public List<ExerciseCount> getTrainedExercisesCount() {
        User user = userService.getCurrentUser();
        List<Object[]> results = workoutSessionRepository.findTrainedExercisesCount(user.getId());

        return results.stream()
                .map(result -> new ExerciseCount((String) result[0], ((Number) result[1]).intValue()))
                .collect(Collectors.toList());
    }

    public List<OneRepMaxDataPoint> getExerciseOneRepMaxProgress(Long exerciseId) {
        User user = userService.getCurrentUser();
        List<ExerciseSession> sessions = exerciseSessionRepository.findByExerciseIdAndUserId(exerciseId, user.getId());

        Map<LocalDate, Double> dateToMaxOneRepMax = new TreeMap<>();

        for (ExerciseSession session : sessions) {
            LocalDate date = session.getWorkoutSession().getDate();
            double maxOneRepMax = session.getSets().stream()
                    .mapToDouble(set -> calculateOneRepMax(set.getWeight(), set.getReps()))
                    .max()
                    .orElse(0);

            dateToMaxOneRepMax.merge(date, maxOneRepMax, Math::max);
        }

        return dateToMaxOneRepMax.entrySet().stream()
                .map(entry -> new OneRepMaxDataPoint(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private double calculateOneRepMax(double weight, int reps) {
        return weight * (1 + reps / 30.0);
    }


}
