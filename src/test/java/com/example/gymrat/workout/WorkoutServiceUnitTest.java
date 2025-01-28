package com.example.gymrat.workout;

import com.example.gymrat.DTO.workout.ExerciseSessionDTO;
import com.example.gymrat.DTO.workout.ExerciseSetDTO;
import com.example.gymrat.DTO.workout.WorkoutSessionDTO;
import com.example.gymrat.mapper.WorkoutMapper;
import com.example.gymrat.model.User;
import com.example.gymrat.model.WorkoutSession;
import com.example.gymrat.repository.ExerciseRepository;
import com.example.gymrat.repository.ExerciseSessionRepository;
import com.example.gymrat.repository.PostRepository;
import com.example.gymrat.repository.WorkoutSessionRepository;
import com.example.gymrat.service.ChallengeScoreUpdateService;
import com.example.gymrat.service.UserService;
import com.example.gymrat.service.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkoutServiceUnitTest {
    @InjectMocks
    private WorkoutService workoutService;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private WorkoutSessionRepository workoutSessionRepository;

    @Mock
    private ExerciseSessionRepository exerciseSessionRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @Mock
    private WorkoutMapper workoutMapper;

    @Mock
    private ChallengeScoreUpdateService challengeScoreUpdateService;

    private User user;
    private WorkoutSession workoutSession;
    private WorkoutSessionDTO workoutSessionDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        user = new User();
        user.setId(1L);
        user.setNickname("testUser");


        workoutSession = new WorkoutSession();
        workoutSession.setId(100L);
        workoutSession.setUser(user);
        workoutSession.setDate(LocalDate.now());

        List<ExerciseSetDTO> sets = List.of(
                new ExerciseSetDTO(10, 50.0),
                new ExerciseSetDTO(8, 55.0)
        );

        List<ExerciseSessionDTO> exerciseSessions = List.of(
                new ExerciseSessionDTO(1L, "Squats", sets),
                new ExerciseSessionDTO(2L, "Bench Press", sets)
        );

        workoutSessionDTO = new WorkoutSessionDTO(
                LocalDate.now(),
                "Testowy opis treningu",
                exerciseSessions
        );
    }


    @Test
    void testSaveWorkout() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(workoutMapper.mapToEntity(workoutSessionDTO)).thenReturn(workoutSession);
        when(workoutSessionRepository.save(workoutSession)).thenReturn(workoutSession);

        Long result = workoutService.saveWorkout(workoutSessionDTO);

        verify(userService).getCurrentUser();
        verify(workoutMapper).mapToEntity(workoutSessionDTO);
        verify(workoutSessionRepository).save(workoutSession);
        verify(challengeScoreUpdateService).updateUserChallengesScore(workoutSession, null);

        assertEquals(100L, result);
    }

    @Test
    void testGetNumberOfUserWorkouts() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(workoutSessionRepository.countAllByUserId(user.getId())).thenReturn(5);

        Integer result = workoutService.getNumberOfUserWorkouts();

        verify(userService).getCurrentUser();
        verify(workoutSessionRepository).countAllByUserId(user.getId());

        assertEquals(5, result);
    }

    @Test
    void testGetTotalWeightLiftedByUser() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(workoutSessionRepository.findTotalWeightLiftedByUser(user.getId())).thenReturn(500.0);

        Double result = workoutService.getTotalWeightLiftedByUser();

        verify(userService).getCurrentUser();
        verify(workoutSessionRepository).findTotalWeightLiftedByUser(user.getId());

        assertEquals(500.0, result);
    }

    @Test
    void testGetDateOfTheLastWorkout() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(workoutSessionRepository.findFirstByUserIdOrderByDateDesc(user.getId()))
                .thenReturn(Optional.of(workoutSession));

        LocalDate result = workoutService.getDateOfTheLastWorkout();

        verify(userService).getCurrentUser();
        verify(workoutSessionRepository).findFirstByUserIdOrderByDateDesc(user.getId());

        assertEquals(workoutSession.getDate(), result);
    }

    @Test
    void testGetDateOfTheLastWorkout_NoWorkouts() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(workoutSessionRepository.findFirstByUserIdOrderByDateDesc(user.getId()))
                .thenReturn(Optional.empty());

        LocalDate result = workoutService.getDateOfTheLastWorkout();

        verify(userService).getCurrentUser();
        verify(workoutSessionRepository).findFirstByUserIdOrderByDateDesc(user.getId());

        assertNull(result);
    }
}
