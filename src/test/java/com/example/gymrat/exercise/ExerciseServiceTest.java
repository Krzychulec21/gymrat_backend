package com.example.gymrat.exercise;

import com.example.gymrat.DTO.exercise.CreateExerciseDTO;
import com.example.gymrat.DTO.exercise.ExerciseResponseDTO;
import com.example.gymrat.model.CategoryName;
import com.example.gymrat.model.Exercise;
import com.example.gymrat.model.ExerciseInfo;
import com.example.gymrat.repository.ExerciseInfoRepository;
import com.example.gymrat.repository.ExerciseRepository;
import com.example.gymrat.service.ExerciseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @Mock
    ExerciseRepository exerciseRepository;

    @Mock
    ExerciseInfoRepository exerciseInfoRepository;

    @InjectMocks
    ExerciseService exerciseService;

    @Test
    void testGetAllExercises() {
        Exercise exercise1 = new Exercise(1L, "Squat", CategoryName.NOGI);
        Exercise exercise2 = new Exercise(2L, "Bench Press", CategoryName.KLATKA_PIERSIOWA);
        ExerciseInfo exerciseInfo1 = new ExerciseInfo();
        exerciseInfo1.setDifficultyLevel(3);
        exercise1.setExerciseInfo(exerciseInfo1);
        ExerciseInfo exerciseInfo2 = new ExerciseInfo();
        exerciseInfo2.setDifficultyLevel(4);
        exercise2.setExerciseInfo(exerciseInfo2);

        when(exerciseRepository.findAll()).thenReturn(List.of(exercise1, exercise2));

        List<ExerciseResponseDTO> result = exerciseService.getAllExercises();

        assertEquals(2, result.size());
        assertEquals("Squat", result.get(0).name());
        assertEquals(3, result.get(0).difficultyLevel());
        assertEquals("Bench Press", result.get(1).name());
        assertEquals(4, result.get(1).difficultyLevel());
    }

    @Test
    void testGetExercisesByCategory() {
        Exercise exercise1 = new Exercise(1L, "Squat", CategoryName.NOGI);
        Exercise exercise2 = new Exercise(2L, "Deadlift", CategoryName.NOGI);
        ExerciseInfo exerciseInfo1 = new ExerciseInfo();
        exerciseInfo1.setDifficultyLevel(3);
        exercise1.setExerciseInfo(exerciseInfo1);
        ExerciseInfo exerciseInfo2 = new ExerciseInfo();
        exerciseInfo2.setDifficultyLevel(4);
        exercise2.setExerciseInfo(exerciseInfo2);

        when(exerciseRepository.findByCategory(CategoryName.NOGI)).thenReturn(List.of(exercise1, exercise2));

        List<ExerciseResponseDTO> result = exerciseService.getExercisesByCategory(CategoryName.NOGI);

        assertEquals(2, result.size());
        assertEquals("Squat", result.get(0).name());
        assertEquals(3, result.get(0).difficultyLevel());
        assertEquals("Deadlift", result.get(1).name());
        assertEquals(4, result.get(1).difficultyLevel());
    }

    @Test
    void testSaveExercise() {
        CreateExerciseDTO createExerciseDTO = new CreateExerciseDTO(
                "Push Up",
                CategoryName.NOGI,
                "A basic exercise",
                "video_id_123",
                2
        );
        Exercise exercise = new Exercise();
        exercise.setName("Push Up");
        exercise.setCategory(CategoryName.NOGI);

        ExerciseInfo exerciseInfo = new ExerciseInfo();
        exerciseInfo.setExercise(exercise);
        exerciseInfo.setDifficultyLevel(2);
        exerciseInfo.setVideoId("video_id_123");
        exerciseInfo.setDescription(List.of("A basic exercise"));

        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);
        when(exerciseInfoRepository.save(any(ExerciseInfo.class))).thenReturn(exerciseInfo);

        exerciseService.saveExercise(createExerciseDTO);
        
        verify(exerciseRepository).save(any(Exercise.class));
        verify(exerciseInfoRepository).save(any(ExerciseInfo.class));
    }
}
