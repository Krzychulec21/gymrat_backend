package com.example.gymrat.database;

import com.example.gymrat.DTO.auth.RegisterRequest;
import com.example.gymrat.DTO.workout.ExerciseSessionDTO;
import com.example.gymrat.DTO.workout.ExerciseSetDTO;
import com.example.gymrat.DTO.workout.WorkoutSessionDTO;
import com.example.gymrat.model.CategoryName;
import com.example.gymrat.model.Exercise;
import com.example.gymrat.repository.ExerciseRepository;
import com.example.gymrat.repository.UserRepository;
import com.example.gymrat.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import com.example.gymrat.model.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserService userService;
    private final ChatService chatService;
    private final FriendService friendService;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutService workoutService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedUsers();
        addFriends();
        addExercises();
        addWorkoutSession();
    }


    public void seedUsers() {
        RegisterRequest user1 = new RegisterRequest("Jan", "Kowalski", "kowalczyk", "kowalski@wp.pl", "password", LocalDate.of(2002, 12, 12));
        RegisterRequest user2 = new RegisterRequest("Michal", "Barylka", "essunia", "nowak@wp.pl", "password", LocalDate.of(1998, 3, 5));
        RegisterRequest user3 = new RegisterRequest("John", "Doe", "johndoe", "johndoe@example.com", "password", LocalDate.of(1990, 7, 21));
        RegisterRequest user4 = new RegisterRequest("Anna", "Nowak", "annaN", "anna@example.com", "password", LocalDate.of(1985, 1, 15));
        RegisterRequest user5 = new RegisterRequest("Ewa", "Zalewska", "ewaz", "ewa@wp.pl", "password", LocalDate.of(2000, 11, 30));
        RegisterRequest user6 = new RegisterRequest("Adam", "Malinowski", "adamM", "adam@example.com", "password", LocalDate.of(1995, 5, 20));
        RegisterRequest user7 = new RegisterRequest("Paweł", "Piotrowski", "pawelP", "pawel@example.com", "password", LocalDate.of(1999, 2, 18));
        RegisterRequest user8 = new RegisterRequest("Katarzyna", "Kwiatkowska", "katK", "katarzyna@wp.pl", "password", LocalDate.of(2001, 4, 4));
        RegisterRequest user9 = new RegisterRequest("Tomasz", "Jankowski", "tomJ", "tomasz@example.com", "password", LocalDate.of(1986, 8, 8));
        RegisterRequest user10 = new RegisterRequest("Aleksandra", "Zawisza", "aleksandraZ", "aleksandra@example.com", "password", LocalDate.of(1987, 6, 16));
        RegisterRequest user11 = new RegisterRequest("Piotr", "Grabowski", "piotrG", "piotr@example.com", "password", LocalDate.of(1992, 9, 9));
        RegisterRequest user12 = new RegisterRequest("Monika", "Szymanska", "monikaS", "monika@example.com", "password", LocalDate.of(1993, 10, 25));
        RegisterRequest user13 = new RegisterRequest("Wojciech", "Mazur", "wojciechM", "wojciech@example.com", "password", LocalDate.of(1988, 12, 1));
        RegisterRequest user14 = new RegisterRequest("Marek", "Pietrzak", "marekP", "marek@example.com", "password", LocalDate.of(1996, 3, 15));
        RegisterRequest user15 = new RegisterRequest("Magdalena", "Olszewska", "magdaO", "magda@example.com", "password", LocalDate.of(1994, 7, 22));
        RegisterRequest user16 = new RegisterRequest("Dariusz", "Zych", "darekZ", "dariusz@example.com", "password", LocalDate.of(1983, 5, 30));
        RegisterRequest user17 = new RegisterRequest("Agata", "Lewandowska", "agataL", "agata@example.com", "password", LocalDate.of(1997, 8, 4));
        RegisterRequest user18 = new RegisterRequest("Damian", "Sikorski", "damianS", "damian@example.com", "password", LocalDate.of(2000, 2, 12));
        RegisterRequest user19 = new RegisterRequest("Łukasz", "Kamiński", "lukaszK", "lukasz@example.com", "password", LocalDate.of(1989, 6, 6));
        RegisterRequest user20 = new RegisterRequest("Zuzanna", "Wróbel", "zuzannaW", "zuzanna@example.com", "password", LocalDate.of(1991, 11, 28));
        RegisterRequest user21 = new RegisterRequest("Kacper", "Czerwiński", "kacperC", "kacper@example.com", "password", LocalDate.of(1984, 4, 17));
        RegisterRequest user22 = new RegisterRequest("Marta", "Chmielewska", "martaC", "marta@example.com", "password", LocalDate.of(1998, 9, 20));
        RegisterRequest user23 = new RegisterRequest("Rafał", "Wysocki", "rafalW", "rafal@example.com", "password", LocalDate.of(1993, 1, 1));
        RegisterRequest user24 = new RegisterRequest("Justyna", "Sikora", "justynaS", "justyna@example.com", "password", LocalDate.of(1995, 12, 10));
        RegisterRequest user25 = new RegisterRequest("Grzegorz", "Górski", "gregG", "grzegorz@example.com", "password", LocalDate.of(1992, 7, 15));
        RegisterRequest user26 = new RegisterRequest("Karolina", "Michalska", "karolinaM", "karolina@example.com", "password", LocalDate.of(2003, 3, 3));
        RegisterRequest user27 = new RegisterRequest("Patryk", "Sadowski", "patrykS", "patryk@example.com", "password", LocalDate.of(1986, 2, 14));
        RegisterRequest user28 = new RegisterRequest("Izabela", "Wiśniewska", "izabelaW", "izabela@example.com", "password", LocalDate.of(1994, 5, 22));
        RegisterRequest user29 = new RegisterRequest("Artur", "Głowacki", "arturG", "artur@example.com", "password", LocalDate.of(1988, 8, 28));
        RegisterRequest user30 = new RegisterRequest("Joanna", "Kozłowska", "joannaK", "joanna@example.com", "password", LocalDate.of(1997, 10, 31));
        RegisterRequest user31 = new RegisterRequest("Krystian", "Sowa", "krystianS", "krystian@example.com", "password", LocalDate.of(1981, 9, 8));
        RegisterRequest user32 = new RegisterRequest("Natalia", "Wesołowska", "nataliaW", "natalia@example.com", "password", LocalDate.of(1995, 4, 12));
        RegisterRequest user33 = new RegisterRequest("Maciej", "Stasiak", "maciejS", "maciej@example.com", "password", LocalDate.of(1991, 3, 19));
        RegisterRequest user34 = new RegisterRequest("Oliwia", "Zając", "oliwiaZ", "oliwia@example.com", "password", LocalDate.of(1999, 6, 24));
        RegisterRequest user35 = new RegisterRequest("Dominik", "Król", "dominikK", "dominik@example.com", "password", LocalDate.of(2000, 7, 16));


        userService.register(user1);
        userService.register(user2);
        userService.register(user3);
        userService.register(user4);
        userService.register(user5);
        userService.register(user6);
        userService.register(user7);
        userService.register(user8);
        userService.register(user9);
        userService.register(user10);
        userService.register(user11);
        userService.register(user12);
        userService.register(user13);
        userService.register(user14);
        userService.register(user15);
        userService.register(user16);
        userService.register(user17);
        userService.register(user18);
        userService.register(user19);
        userService.register(user20);
        userService.register(user21);
        userService.register(user22);
        userService.register(user23);
        userService.register(user24);
        userService.register(user25);
        userService.register(user26);
        userService.register(user27);
        userService.register(user28);
        userService.register(user29);
        userService.register(user30);
        userService.register(user31);
        userService.register(user32);
        userService.register(user33);
        userService.register(user34);
        userService.register(user35);
    }

    public void addFriends() {
        User user1 = userRepository.findByEmail("kowalski@wp.pl")
                .orElseThrow(() -> new RuntimeException("User not found: kowalski@wp.pl"));

        String[] userEmails = {
                "nowak@wp.pl",
                "johndoe@example.com",
                "anna@example.com",
                "ewa@wp.pl",
                "adam@example.com",
                "pawel@example.com",
                "katarzyna@wp.pl",
                "tomasz@example.com",
                "aleksandra@example.com",
                "piotr@example.com",
                "monika@example.com",
                "wojciech@example.com",
                "marek@example.com",
                "magda@example.com",
                "dariusz@example.com",
                "agata@example.com",
                "damian@example.com",
                "lukasz@example.com",
                "zuzanna@example.com",
                "kacper@example.com",
                "marta@example.com",
                "rafal@example.com",
                "justyna@example.com",
                "grzegorz@example.com",
                "karolina@example.com",
                "patryk@example.com",
                "izabela@example.com",
                "artur@example.com",
                "joanna@example.com",
                "krystian@example.com",
                "natalia@example.com",
                "maciej@example.com",
                "oliwia@example.com",
                "dominik@example.com"
        };

        for (int i = 0; i < userEmails.length; i++) {
            String email = userEmails[i];
            User otherUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found: " + email));

            friendService.sendFriendRequest(user1.getEmail(), otherUser.getEmail());
            friendService.respondToFriendRequest((long) (i + 1), true);
        }
    }

    public void addExercises() {

        List<Exercise> exercises = Arrays.asList(
                // LEGS
                new Exercise(null, "Squat", CategoryName.LEGS),
                new Exercise(null, "Lunges", CategoryName.LEGS),
                new Exercise(null, "Leg Press", CategoryName.LEGS),
                new Exercise(null, "Leg Curl", CategoryName.LEGS),
                new Exercise(null, "Calf Raise", CategoryName.LEGS),

                // CHEST
                new Exercise(null, "Bench Press", CategoryName.CHEST),
                new Exercise(null, "Incline Dumbbell Press", CategoryName.CHEST),
                new Exercise(null, "Chest Fly", CategoryName.CHEST),
                new Exercise(null, "Push-ups", CategoryName.CHEST),
                new Exercise(null, "Dips", CategoryName.CHEST),

                // BACK
                new Exercise(null, "Deadlift", CategoryName.BACK),
                new Exercise(null, "Pull-ups", CategoryName.BACK),
                new Exercise(null, "Lat Pulldown", CategoryName.BACK),
                new Exercise(null, "Bent-over Row", CategoryName.BACK),
                new Exercise(null, "T-Bar Row", CategoryName.BACK),

                // BICEPS
                new Exercise(null, "Barbell Curl", CategoryName.BICEPS),
                new Exercise(null, "Dumbbell Curl", CategoryName.BICEPS),
                new Exercise(null, "Concentration Curl", CategoryName.BICEPS),
                new Exercise(null, "Hammer Curl", CategoryName.BICEPS),
                new Exercise(null, "Preacher Curl", CategoryName.BICEPS),

                // TRICEPS
                new Exercise(null, "Triceps Pushdown", CategoryName.TRICEPS),
                new Exercise(null, "Overhead Triceps Extension", CategoryName.TRICEPS),
                new Exercise(null, "Skull Crusher", CategoryName.TRICEPS),
                new Exercise(null, "Triceps Dips", CategoryName.TRICEPS),
                new Exercise(null, "Close-Grip Bench Press", CategoryName.TRICEPS),

                // SHOULDERS
                new Exercise(null, "Shoulder Press", CategoryName.SHOULDERS),
                new Exercise(null, "Lateral Raise", CategoryName.SHOULDERS),
                new Exercise(null, "Front Raise", CategoryName.SHOULDERS),
                new Exercise(null, "Arnold Press", CategoryName.SHOULDERS),
                new Exercise(null, "Reverse Pec Deck", CategoryName.SHOULDERS),

                // ABS
                new Exercise(null, "Crunches", CategoryName.ABS),
                new Exercise(null, "Leg Raise", CategoryName.ABS),
                new Exercise(null, "Plank", CategoryName.ABS),
                new Exercise(null, "Russian Twist", CategoryName.ABS),
                new Exercise(null, "Bicycle Crunch", CategoryName.ABS)
        );

        exerciseRepository.saveAll(exercises);
    }

    public void addWorkoutSession() {
        // Example exercises (assuming these already exist in your DB)
        Long exercise1Id = 1L; // ID for "Squat"
        Long exercise2Id = 2L; // ID for "Bench Press"

        // Create sets for the first exercise session (e.g., Squat)
        ExerciseSetDTO set1 = new ExerciseSetDTO(12, 80.0); // 12 reps, 80 kg
        ExerciseSetDTO set2 = new ExerciseSetDTO(10, 85.0); // 10 reps, 85 kg
        List<ExerciseSetDTO> squatSets = Arrays.asList(set1, set2);

        // Create the first exercise session (Squat)
        ExerciseSessionDTO squatSession = new ExerciseSessionDTO(exercise1Id, squatSets);

        // Create sets for the second exercise session (e.g., Bench Press)
        ExerciseSetDTO set3 = new ExerciseSetDTO(10, 60.0); // 10 reps, 60 kg
        ExerciseSetDTO set4 = new ExerciseSetDTO(8, 65.0);  // 8 reps, 65 kg
        List<ExerciseSetDTO> benchPressSets = Arrays.asList(set3, set4);

        // Create the second exercise session (Bench Press)
        ExerciseSessionDTO benchPressSession = new ExerciseSessionDTO(exercise2Id, benchPressSets);

        // Create a workout session with both exercise sessions
        WorkoutSessionDTO workoutSessionDTO = new WorkoutSessionDTO(
                LocalDate.now(),            // Current date
                "Morning workout",          // Note
                Arrays.asList(squatSession, benchPressSession) // List of exercise sessions
        );

        // Save workout using WorkoutService
        workoutService.saveWorkout(workoutSessionDTO);
    }


}
