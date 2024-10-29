package com.example.gymrat.service;

import com.example.gymrat.DTO.exercise.CreateExerciseDTO;
import com.example.gymrat.DTO.exercise.ExerciseInfoResponseDTO;
import com.example.gymrat.DTO.exercise.ExerciseResponseDTO;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.model.CategoryName;
import com.example.gymrat.model.Exercise;
import com.example.gymrat.model.ExerciseInfo;
import com.example.gymrat.repository.ExerciseInfoRepository;
import com.example.gymrat.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseInfoRepository exerciseInfoRepository;

    public List<ExerciseResponseDTO> getAllExercises() {
        List<Exercise> list = exerciseRepository.findAll();

        return list
                .stream()
                .map(exercise -> new ExerciseResponseDTO(
                        exercise.getId(),
                        exercise.getName(),
                        exercise.getCategory()))
                .toList();
    }


    public List<ExerciseResponseDTO> getExercisesByCategory(String category) {
        List<Exercise> list = exerciseRepository.findByCategory(CategoryName.valueOf(category));

        return list
                .stream()
                .map(exercise -> new ExerciseResponseDTO(
                        exercise.getId(),
                        exercise.getName(),
                        exercise.getCategory()))
                .toList();
    }

    public void saveExercise(CreateExerciseDTO request) {
        Exercise exercise = new Exercise();
        exercise.setName(request.name());
        exercise.setCategory(request.categoryName());

        exerciseRepository.save(exercise);
    }

    public ExerciseInfoResponseDTO getExerciseInfo(Long exerciseId) {
        ExerciseInfo exerciseInfo = exerciseInfoRepository.findByExerciseId(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("ExerciseInfo with given ID doesn't exist"));


        return new ExerciseInfoResponseDTO(
                new ArrayList<>(exerciseInfo.getDescription()),
                exerciseInfo.getVideoId());
    }

}
