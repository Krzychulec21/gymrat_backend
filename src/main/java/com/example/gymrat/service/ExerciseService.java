package com.example.gymrat.service;

import com.example.gymrat.DTO.exercise.CreateExerciseDTO;
import com.example.gymrat.DTO.exercise.ExerciseResponseDTO;
import com.example.gymrat.model.CategoryName;
import com.example.gymrat.model.Exercise;
import com.example.gymrat.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

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
}
