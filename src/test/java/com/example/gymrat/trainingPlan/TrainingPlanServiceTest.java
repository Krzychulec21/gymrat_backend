package com.example.gymrat.trainingPlan;

import com.example.gymrat.DTO.trainingPlan.CreateTrainingPlanDTO;
import com.example.gymrat.DTO.trainingPlan.ExerciseInPlanDTO;
import com.example.gymrat.DTO.trainingPlan.ExerciseInPlanResponseDTO;
import com.example.gymrat.DTO.trainingPlan.TrainingPlanResponseDTO;
import com.example.gymrat.exception.ResourceNotFoundException;
import com.example.gymrat.mapper.TrainingPlanMapper;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.ExerciseRepository;
import com.example.gymrat.repository.FavoriteTrainingPlanRepository;
import com.example.gymrat.repository.TrainingPlanLikeRepository;
import com.example.gymrat.repository.TrainingPlanRepository;
import com.example.gymrat.service.CommentService;
import com.example.gymrat.service.LikeService;
import com.example.gymrat.service.TrainingPlanService;
import com.example.gymrat.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TrainingPlanServiceTest {

    @InjectMocks
    private TrainingPlanService trainingPlanService;

    @Mock
    private TrainingPlanRepository trainingPlanRepository;

    @Mock
    private UserService userService;

    @Mock
    private TrainingPlanMapper trainingPlanMapper;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private CommentService commentService;

    @Mock
    private LikeService likeService;

    @Mock
    private TrainingPlanLikeRepository trainingPlanLikeRepository;

    @Mock
    private FavoriteTrainingPlanRepository favoriteTrainingPlanRepository;

    private User mockUser;
    private CreateTrainingPlanDTO mockCreateDTO;
    private TrainingPlan mockTrainingPlan;
    private TrainingPlanResponseDTO mockResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setNickname("testUser");

        ExerciseInfo mockExerciseInfo = new ExerciseInfo();
        mockExerciseInfo.setDifficultyLevel(3);
        Exercise mockExercise1 = new Exercise(1L, "Squat", CategoryName.NOGI);
        mockExercise1.setExerciseInfo(mockExerciseInfo);
        Exercise mockExercise2 = new Exercise(2L, "Bench Press", CategoryName.KLATKA_PIERSIOWA);
        mockExercise2.setExerciseInfo(mockExerciseInfo);

        mockTrainingPlan = new TrainingPlan();
        mockTrainingPlan.setId(1L);
        mockTrainingPlan.setAuthor(mockUser);
        mockTrainingPlan.setName("Test Plan");

        mockCreateDTO = new CreateTrainingPlanDTO(
                "Test Plan",
                "Full body plan",
                List.of(
                        new ExerciseInPlanDTO(1L, "Do 3 sets of 10 reps"),
                        new ExerciseInPlanDTO(2L, "Do 3 sets of 8 reps")
                )
        );

        mockResponseDTO = new TrainingPlanResponseDTO(
                1L,
                "Test Plan",
                "Full body plan",
                1L,
                "testUser",
                3,
                Set.of(CategoryName.NOGI, CategoryName.KLATKA_PIERSIOWA),
                List.of(
                        new ExerciseInPlanResponseDTO(1L, "Squat", "Do 3 sets of 10 reps"),
                        new ExerciseInPlanResponseDTO(2L, "Bench Press", "Do 3 sets of 8 reps")
                ),
                List.of(),
                10,
                null
        );
    }

    @Test
    void testSaveTrainingPlanSuccess() {
        when(userService.getCurrentUser()).thenReturn(mockUser);

        when(exerciseRepository.findAllById(List.of(1L, 2L))).thenReturn(
                List.of(
                        new Exercise(1L, "Squat", CategoryName.NOGI),  // Exercise without ExerciseInfo will be replaced
                        new Exercise(2L, "Bench Press", CategoryName.KLATKA_PIERSIOWA)  // Same for this one
                )
        );

        when(trainingPlanMapper.toEntity(eq(mockCreateDTO), anyList())).thenReturn(mockTrainingPlan);

        trainingPlanService.saveTrainingPlan(mockCreateDTO);

        verify(userService, times(1)).getCurrentUser();
        verify(exerciseRepository, times(1)).findAllById(List.of(1L, 2L));
        verify(trainingPlanMapper, times(1)).toEntity(eq(mockCreateDTO), anyList());
        verify(trainingPlanRepository, times(1)).save(mockTrainingPlan);
    }

    @Test
    void testSaveTrainingPlanExerciseNotFound() {
        when(userService.getCurrentUser()).thenReturn(mockUser);
        when(exerciseRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(new Exercise(1L, "Squat", CategoryName.NOGI)));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> trainingPlanService.saveTrainingPlan(mockCreateDTO)
        );

        assertEquals("One or more exercises not found", exception.getMessage());
        verify(trainingPlanRepository, never()).save(any());
    }


    @Test
    void testGetTrainingPlanByIdNotFound() {
        when(userService.getCurrentUser()).thenReturn(mockUser);
        when(trainingPlanRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> trainingPlanService.getTrainingPlanById(999L)
        );

        assertEquals("Training plan with given ID does not exist", exception.getMessage());
    }
}
