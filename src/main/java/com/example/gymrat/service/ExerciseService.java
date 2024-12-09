package com.example.gymrat.service;

import com.example.gymrat.DTO.exercise.CreateExerciseDTO;
import com.example.gymrat.DTO.exercise.ExerciseInfoResponseDTO;
import com.example.gymrat.DTO.exercise.ExerciseResponseDTO;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.model.CategoryName;
import com.example.gymrat.model.Exercise;
import com.example.gymrat.model.ExerciseInfo;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.ExerciseInfoRepository;
import com.example.gymrat.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseInfoRepository exerciseInfoRepository;
    private final UserService userService;

    public List<ExerciseResponseDTO> getAllExercises() {
        List<Exercise> list = exerciseRepository.findAll();

        return list
                .stream()
                .map(exercise -> new ExerciseResponseDTO(
                        exercise.getId(),
                        exercise.getName(),
                        exercise.getCategory(),
                        exercise.getExerciseInfo().getDifficultyLevel()))
                .toList();
    }


    public List<ExerciseResponseDTO> getExercisesByCategory(CategoryName category) {
        List<Exercise> list = exerciseRepository.findByCategory(category);

        return list
                .stream()
                .map(exercise -> new ExerciseResponseDTO(
                        exercise.getId(),
                        exercise.getName(),
                        exercise.getCategory(),
                        exercise.getExerciseInfo().getDifficultyLevel()))
                .toList();
    }

    public void saveExercise(CreateExerciseDTO request) {
        try {
            Exercise exercise = new Exercise();
            exercise.setName(request.name());
            exercise.setCategory(request.categoryName());
            exerciseRepository.save(exercise);
            ExerciseInfo exerciseInfo = new ExerciseInfo();
            exerciseInfo.setExercise(exercise);
            exerciseInfo.setDifficultyLevel(request.difficultyLevel());
            exerciseInfo.setVideoId(request.videoId());
            exerciseInfo.setDescription(Arrays.asList(request.description().split("\n")));

            exerciseInfoRepository.save(exerciseInfo);
        } catch (Exception e) {
            System.err.println("Error in saveExercise: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public ExerciseInfoResponseDTO getExerciseInfo(Long exerciseId) {
        ExerciseInfo exerciseInfo = exerciseInfoRepository.findByExerciseId(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("ExerciseInfo with given ID doesn't exist"));


        return new ExerciseInfoResponseDTO(
                new ArrayList<>(exerciseInfo.getDescription()),
                exerciseInfo.getVideoId(),
                exerciseInfo.getDifficultyLevel(),
                exerciseInfo.getExercise().getName()
        );
    }

    public List<ExerciseResponseDTO> getExercisesTrainedByUser() {
        User user = userService.getCurrentUser();
        List<Exercise> exercises = exerciseRepository.findExercisesTrainedByUser(user.getId());

        return exercises.stream()
                .map(exercise -> new ExerciseResponseDTO(exercise.getId(), exercise.getName(), exercise.getCategory(), exercise.getExerciseInfo().getDifficultyLevel()))
                .collect(Collectors.toList());
    }


    public Page<ExerciseResponseDTO> getAllExercisesPaginate(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy.equals("categoryName") ? "category" : sortBy);
        Page<Exercise> exercisePage = exerciseRepository.findAll(pageable);
        return exercisePage.map(exercise -> new ExerciseResponseDTO(
                exercise.getId(),
                exercise.getName(),
                exercise.getCategory(),
                exercise.getExerciseInfo().getDifficultyLevel()
        ));
    }

    public void updateExercise(Long id, CreateExerciseDTO request) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise with given ID does not exist"));

        exercise.setName(request.name());
        exercise.setCategory(request.categoryName());
        exerciseRepository.save(exercise);

        ExerciseInfo exerciseInfo = exerciseInfoRepository.findByExerciseId(exercise.getId())
                .orElseThrow(() -> new ResourceNotFoundException("ExerciseInfo with given ID does not exist"));

        exerciseInfo.setDifficultyLevel(request.difficultyLevel());
        exerciseInfo.setVideoId(request.videoId());
        exerciseInfo.setDescription(new ArrayList<>(Arrays.asList(request.description().split("\n"))));
        exerciseInfoRepository.save(exerciseInfo);
    }

}
