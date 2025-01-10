package com.example.gymrat.database;

import com.example.gymrat.DTO.auth.RegisterRequest;
import com.example.gymrat.DTO.trainingPlan.CreateTrainingPlanDTO;
import com.example.gymrat.DTO.trainingPlan.ExerciseInPlanDTO;
import com.example.gymrat.DTO.workout.ExerciseSessionDTO;
import com.example.gymrat.DTO.workout.ExerciseSetDTO;
import com.example.gymrat.DTO.workout.WorkoutSessionDTO;
import com.example.gymrat.mapper.UserMapper;
import com.example.gymrat.mapper.WorkoutMapper;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.*;
import com.example.gymrat.service.FriendService;
import com.example.gymrat.service.TrainingPlanService;
import com.example.gymrat.service.UserService;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserService userService;
    private final FriendService friendService;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutMapper workoutMapper;
    private final TrainingPlanService trainingPlanService;
    private final PersonalInfoRepository personalInfoRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final ChallengeTypeRepository challengeTypeRepository;
    private final ChallengeRepository challengeRepository;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedUsers();
        addFriends();
        addExercises();
        addWorkoutSession();
        addTrainingPlans();
        addPersonalInfo();
        addPost();
        addAdmin();
        addChallengeTypes();
        createChallenges();
    }

    private void createChallenges() {
        User user = userService.getUserById(1L);
        User user2 = userService.getUserById(2L);
        ChallengeType type = challengeTypeRepository.findById(3L).orElseThrow();
        ChallengeType passa = challengeTypeRepository.findById(1L).orElseThrow();
        Exercise exercise = exerciseRepository.findById(6L).orElseThrow();

        // Existing finished challenges
        Challenge challenge = new Challenge();
        challenge.setPublic(true);
        challenge.setChallengeStatus(ChallengeStatus.FINISHED);
        challenge.setChallengeType(type);
        challenge.setExercise(exercise);
        challenge.setAuthor(user);
        challenge.setStartDate(LocalDate.of(2024, 12, 2));
        challenge.setEndDate(LocalDate.of(2024, 12, 23));
        challenge.setName("Najsilniejsza klatka");

        ChallengeParticipant participant = new ChallengeParticipant();
        participant.setUser(user);
        participant.setChallenge(challenge);
        participant.setScore(80.0);
        participant.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 12, 6), LocalTime.of(8, 23, 43)));

        ChallengeParticipant participant2 = new ChallengeParticipant();
        participant2.setUser(user2);
        participant2.setChallenge(challenge);
        participant2.setScore(75.0);
        participant2.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 12, 4), LocalTime.of(8, 23, 43)));

        List<ChallengeParticipant> list = new ArrayList<>();
        list.add(participant);
        list.add(participant2);
        challenge.setChallengeParticipants(list);
        challengeRepository.save(challenge);

        ChallengeType newType = challengeTypeRepository.findById(2L).orElseThrow();
        Exercise newExercise = exerciseRepository.findById(1L).orElseThrow();

        Challenge challenge2 = new Challenge();
        challenge2.setPublic(true);
        challenge2.setChallengeStatus(ChallengeStatus.FINISHED);
        challenge2.setChallengeType(newType);
        challenge2.setExercise(newExercise);
        challenge2.setAuthor(user);
        challenge2.setStartDate(LocalDate.of(2024, 12, 2));
        challenge2.setEndDate(LocalDate.of(2024, 12, 14));
        challenge2.setName("Pompowanie nóg");

        ChallengeParticipant participant3 = new ChallengeParticipant();
        participant3.setUser(user);
        participant3.setChallenge(challenge2);
        participant3.setScore(850.0);
        participant3.setLastUpdated(LocalDateTime.of(2024, 12, 10, 8, 23, 43));

        ChallengeParticipant participant4 = new ChallengeParticipant();
        participant4.setUser(user2);
        participant4.setChallenge(challenge2);
        participant4.setScore(1235.0);
        participant4.setLastUpdated(LocalDateTime.of(2024, 12, 13, 8, 23, 43));

        List<ChallengeParticipant> participants2 = new ArrayList<>();
        participants2.add(participant3);
        participants2.add(participant4);
        challenge2.setChallengeParticipants(participants2);
        challengeRepository.save(challenge2);

        // Existing active challenges
        Challenge challenge3 = new Challenge();
        challenge3.setPublic(true);
        challenge3.setChallengeStatus(ChallengeStatus.ACTIVE);
        challenge3.setChallengeType(passa);
        challenge3.setAuthor(user2);
        challenge3.setStartDate(LocalDate.of(2024, 12, 2));
        challenge3.setEndDate(LocalDate.of(2025, 1, 14));
        challenge3.setName("Systematyczność!");
        challengeRepository.save(challenge3);

        Challenge challenge4 = new Challenge();
        challenge4.setPublic(false);
        challenge4.setChallengeStatus(ChallengeStatus.ACTIVE);
        challenge4.setChallengeType(newType);
        challenge4.setExercise(newExercise);
        challenge4.setAuthor(user2);
        challenge4.setStartDate(LocalDate.of(2024, 12, 2));
        challenge4.setEndDate(LocalDate.of(2025, 1, 14));
        challenge4.setName("Kto najlepszy na ławce?");
        challengeRepository.save(challenge4);

        Challenge challenge5 = new Challenge();
        challenge5.setPublic(true);
        challenge5.setChallengeStatus(ChallengeStatus.ACTIVE);
        challenge5.setChallengeType(type);
        challenge5.setExercise(exercise);
        challenge5.setAuthor(user);
        challenge5.setStartDate(LocalDate.of(2024, 12, 2));
        challenge5.setEndDate(LocalDate.of(2025, 1, 23));
        challenge5.setName("Najsilniejsza klatka");

        ChallengeParticipant participant7 = new ChallengeParticipant();
        participant7.setUser(user);
        participant7.setChallenge(challenge5);
        participant7.setScore(80.0);
        participant7.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 12, 6), LocalTime.of(8, 23, 43)));

        ChallengeParticipant participant8 = new ChallengeParticipant();
        participant8.setUser(user2);
        participant8.setChallenge(challenge5);
        participant8.setScore(75.0);
        participant8.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 12, 4), LocalTime.of(8, 23, 43)));

        List<ChallengeParticipant> list2 = new ArrayList<>();
        list2.add(participant7);
        list2.add(participant8);
        challenge5.setChallengeParticipants(list2);
        challengeRepository.save(challenge5);

        Challenge challenge6 = new Challenge();
        challenge6.setPublic(false);
        challenge6.setChallengeStatus(ChallengeStatus.ACTIVE);
        challenge6.setChallengeType(newType);
        challenge6.setExercise(exercise);
        challenge6.setAuthor(user);
        challenge6.setStartDate(LocalDate.of(2024, 12, 2));
        challenge6.setEndDate(LocalDate.of(2025, 1, 15));
        challenge6.setName("Największy na siłowni");

        ChallengeParticipant participant9 = new ChallengeParticipant();
        participant9.setUser(user);
        participant9.setChallenge(challenge6);
        participant9.setScore(120.0);
        participant9.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 12, 27), LocalTime.of(8, 23, 43)));

        List<ChallengeParticipant> list3 = new ArrayList<>();
        list3.add(participant9);
        challenge6.setChallengeParticipants(list3);
        challengeRepository.save(challenge6);

        // 6 Additional active challenges (4 with user1 as author)
        Challenge challenge7 = new Challenge();
        challenge7.setPublic(true);
        challenge7.setChallengeStatus(ChallengeStatus.ACTIVE);
        challenge7.setChallengeType(type);
        challenge7.setExercise(exercise);
        challenge7.setAuthor(user);
        challenge7.setStartDate(LocalDate.of(2024, 12, 2));
        challenge7.setEndDate(LocalDate.of(2025, 2, 1));
        challenge7.setName("Wytrzymałość na maksa");

        ChallengeParticipant participant10 = new ChallengeParticipant();
        participant10.setUser(user);
        participant10.setChallenge(challenge7);
        participant10.setScore(90.0);
        participant10.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 12, 10), LocalTime.of(8, 23, 43)));

        List<ChallengeParticipant> list4 = new ArrayList<>();
        list4.add(participant10);
        challenge7.setChallengeParticipants(list4);
        challengeRepository.save(challenge7);

        Challenge challenge8 = new Challenge();
        challenge8.setPublic(false);
        challenge8.setChallengeStatus(ChallengeStatus.ACTIVE);
        challenge8.setChallengeType(newType);
        challenge8.setExercise(newExercise);
        challenge8.setAuthor(user);
        challenge8.setStartDate(LocalDate.of(2024, 12, 2));
        challenge8.setEndDate(LocalDate.of(2025, 1, 30));
        challenge8.setName("Sprint do celu");

        ChallengeParticipant participant11 = new ChallengeParticipant();
        participant11.setUser(user);
        participant11.setChallenge(challenge8);
        participant11.setScore(200.0);
        participant11.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 12, 15), LocalTime.of(8, 23, 43)));

        List<ChallengeParticipant> list5 = new ArrayList<>();
        list5.add(participant11);
        challenge8.setChallengeParticipants(list5);
        challengeRepository.save(challenge8);

        Challenge challenge9 = new Challenge();
        challenge9.setPublic(true);
        challenge9.setChallengeStatus(ChallengeStatus.ACTIVE);
        challenge9.setChallengeType(type);
        challenge9.setExercise(exercise);
        challenge9.setAuthor(user);
        challenge9.setStartDate(LocalDate.of(2024, 12, 2));
        challenge9.setEndDate(LocalDate.of(2025, 2, 10));
        challenge9.setName("Mocne plecy");

        ChallengeParticipant participant12 = new ChallengeParticipant();
        participant12.setUser(user);
        participant12.setChallenge(challenge9);
        participant12.setScore(110.0);
        participant12.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 12, 20), LocalTime.of(8, 23, 43)));

        List<ChallengeParticipant> list6 = new ArrayList<>();
        list6.add(participant12);
        challenge9.setChallengeParticipants(list6);
        challengeRepository.save(challenge9);

        Challenge challenge10 = new Challenge();
        challenge10.setPublic(true);
        challenge10.setChallengeStatus(ChallengeStatus.ACTIVE);
        challenge10.setChallengeType(newType);
        challenge10.setExercise(newExercise);
        challenge10.setAuthor(user2);
        challenge10.setStartDate(LocalDate.of(2024, 12, 2));
        challenge10.setEndDate(LocalDate.of(2025, 2, 5));
        challenge10.setName("Biceps na lato");

        ChallengeParticipant participant13 = new ChallengeParticipant();
        participant13.setUser(user2);
        participant13.setChallenge(challenge10);
        participant13.setScore(150.0);
        participant13.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 12, 18), LocalTime.of(8, 23, 43)));

        List<ChallengeParticipant> list7 = new ArrayList<>();
        list7.add(participant13);
        challenge10.setChallengeParticipants(list7);
        challengeRepository.save(challenge10);

        Challenge challenge11 = new Challenge();
        challenge11.setPublic(false);
        challenge11.setChallengeStatus(ChallengeStatus.ACTIVE);
        challenge11.setChallengeType(type);
        challenge11.setExercise(exercise);
        challenge11.setAuthor(user);
        challenge11.setStartDate(LocalDate.of(2024, 12, 2));
        challenge11.setEndDate(LocalDate.of(2025, 2, 15));
        challenge11.setName("Wyciskanie na maxa");

        ChallengeParticipant participant14 = new ChallengeParticipant();
        participant14.setUser(user);
        participant14.setChallenge(challenge11);
        participant14.setScore(130.0);
        participant14.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 12, 25), LocalTime.of(8, 23, 43)));

        List<ChallengeParticipant> list8 = new ArrayList<>();
        list8.add(participant14);
        challenge11.setChallengeParticipants(list8);
        challengeRepository.save(challenge11);

        Challenge challenge12 = new Challenge();
        challenge12.setPublic(true);
        challenge12.setChallengeStatus(ChallengeStatus.ACTIVE);
        challenge12.setChallengeType(newType);
        challenge12.setExercise(newExercise);
        challenge12.setAuthor(user2);
        challenge12.setStartDate(LocalDate.of(2024, 12, 2));
        challenge12.setEndDate(LocalDate.of(2025, 2, 20));
        challenge12.setName("Cardio maraton");

        ChallengeParticipant participant15 = new ChallengeParticipant();
        participant15.setUser(user2);
        participant15.setChallenge(challenge12);
        participant15.setScore(300.0);
        participant15.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 12, 22), LocalTime.of(8, 23, 43)));

        List<ChallengeParticipant> list9 = new ArrayList<>();
        list9.add(participant15);
        challenge12.setChallengeParticipants(list9);
        challengeRepository.save(challenge12);

        // 3 Additional finished challenges
        Challenge challenge13 = new Challenge();
        challenge13.setPublic(true);
        challenge13.setChallengeStatus(ChallengeStatus.FINISHED);
        challenge13.setChallengeType(type);
        challenge13.setExercise(exercise);
        challenge13.setAuthor(user);
        challenge13.setStartDate(LocalDate.of(2024, 11, 1));
        challenge13.setEndDate(LocalDate.of(2024, 11, 30));
        challenge13.setName("Wyzwanie listopada");

        ChallengeParticipant participant16 = new ChallengeParticipant();
        participant16.setUser(user);
        participant16.setChallenge(challenge13);
        participant16.setScore(100.0);
        participant16.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 11, 25), LocalTime.of(8, 23, 43)));

        ChallengeParticipant participant17 = new ChallengeParticipant();
        participant17.setUser(user2);
        participant17.setChallenge(challenge13);
        participant17.setScore(95.0);
        participant17.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 11, 28), LocalTime.of(8, 23, 43)));

        List<ChallengeParticipant> list10 = new ArrayList<>();
        list10.add(participant16);
        list10.add(participant17);
        challenge13.setChallengeParticipants(list10);
        challengeRepository.save(challenge13);

        Challenge challenge14 = new Challenge();
        challenge14.setPublic(false);
        challenge14.setChallengeStatus(ChallengeStatus.FINISHED);
        challenge14.setChallengeType(newType);
        challenge14.setExercise(newExercise);
        challenge14.setAuthor(user2);
        challenge14.setStartDate(LocalDate.of(2024, 10, 1));
        challenge14.setEndDate(LocalDate.of(2024, 10, 31));
        challenge14.setName("Październikowy sprint");

        ChallengeParticipant participant18 = new ChallengeParticipant();
        participant18.setUser(user2);
        participant18.setChallenge(challenge14);
        participant18.setScore(250.0);
        participant18.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 10, 30), LocalTime.of(8, 23, 43)));

        List<ChallengeParticipant> list11 = new ArrayList<>();
        list11.add(participant18);
        challenge14.setChallengeParticipants(list11);
        challengeRepository.save(challenge14);

        Challenge challenge15 = new Challenge();
        challenge15.setPublic(true);
        challenge15.setChallengeStatus(ChallengeStatus.FINISHED);
        challenge15.setChallengeType(type);
        challenge15.setExercise(exercise);
        challenge15.setAuthor(user);
        challenge15.setStartDate(LocalDate.of(2024, 9, 1));
        challenge15.setEndDate(LocalDate.of(2024, 9, 30));
        challenge15.setName("Wrzesień z wyciskaniem");

        ChallengeParticipant participant19 = new ChallengeParticipant();
        participant19.setUser(user);
        participant19.setChallenge(challenge15);
        participant19.setScore(140.0);
        participant19.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 9, 28), LocalTime.of(8, 23, 43)));

        ChallengeParticipant participant20 = new ChallengeParticipant();
        participant20.setUser(user2);
        participant20.setChallenge(challenge15);
        participant20.setScore(135.0);
        participant20.setLastUpdated(LocalDateTime.of(LocalDate.of(2024, 9, 29), LocalTime.of(8, 23, 43)));

        List<ChallengeParticipant> list12 = new ArrayList<>();
        list12.add(participant19);
        list12.add(participant20);
        challenge15.setChallengeParticipants(list12);
        challengeRepository.save(challenge15);
    }

    private void addChallengeTypes() {
        ChallengeType challengeType = new ChallengeType();
        challengeType.setName("Najdluzsza passa treningu");
        challengeType.setDescription("Wyzwanie polegajace na osiagnieciu najdluzszej ilosci przetrenowanych dni bez przerwy");
        challengeTypeRepository.save(challengeType);

        ChallengeType challengeType2 = new ChallengeType();
        challengeType2.setName("Najwięcej przerzuconych kilogramów");
        challengeType2.setDescription("Wyzwanie polegajace na osiagnieciu najwiekszej sumy podniesionych kilogramow z danego cwiczenia");
        challengeTypeRepository.save(challengeType2);

        ChallengeType challengeType3 = new ChallengeType();
        challengeType3.setName("Najsilniejszy na raz");
        challengeType3.setDescription("Wyzwanie polegajace na osiagnieciu najlepszego powtorzenia z danego cwiczenia");
        challengeTypeRepository.save(challengeType3);

    }

    public void addAdmin() {
        RegisterRequest admin = new RegisterRequest("Admin", "Admin", "admin", "admin@example.com", "password", LocalDate.of(2002, 12, 12));
        User user = UserMapper.toEntity(admin);
        user.setPassword(passwordEncoder.encode(admin.password()));
        user.setRole(Role.ROLE_ADMIN);
        user.setEmailVerified(true);
        userRepository.save(user);

        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setUser(user);
        personalInfoRepository.save(personalInfo);
    }

    public void seedUsers() {
        RegisterRequest user1 = new RegisterRequest("Jan", "Kowalski", "kowalczyk", "kowalski@wp.pl", "password", LocalDate.of(2002, 12, 12));
        RegisterRequest user2 = new RegisterRequest("Michal", "Barylka", "majkel_12", "nowak@wp.pl", "password", LocalDate.of(1998, 3, 5));
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

        user1.setEmailVerified(true);
        userRepository.save(user1);

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
            otherUser.setEmailVerified(true);
            userRepository.save(otherUser);

            friendService.sendFriendRequest(user1.getEmail(), otherUser.getEmail());
            friendService.respondToFriendRequest((long) (i + 1), true);
        }
    }

    public void addExercises() {
        // LEGS
        ExerciseInfo squatInfo = new ExerciseInfo();
        squatInfo.setVideoId("Wv7KejSwEvQ");
        squatInfo.setDescription(Arrays.asList(
                "Ustaw stopy na szerokość barków, palce skierowane lekko na zewnątrz.",
                "Utrzymuj klatkę piersiową w górze i patrz przed siebie.",
                "Zginaj kolana i biodra, obniżając ciało jakbyś chciał usiąść na krześle.",
                "Schodź w dół aż uda będą równoległe do podłoża lub niżej.",
                "Wróć do pozycji wyjściowej, prostując nogi i napinając pośladki."
        ));
        squatInfo.setDifficultyLevel(3);

        ExerciseInfo lungesInfo = new ExerciseInfo();
        lungesInfo.setVideoId("WIlpzl61YXk");
        lungesInfo.setDescription(Arrays.asList(
                "Stój prosto z nogami rozstawionymi na szerokość bioder.",
                "Zrób duży krok do przodu jedną nogą.",
                "Zginaj oba kolana, obniżając ciało, aż tylne kolano prawie dotknie podłogi.",
                "Wróć do pozycji wyjściowej, odpychając się przednią stopą.",
                "Powtórz na drugą nogę."
        ));
        lungesInfo.setDifficultyLevel(2);

        ExerciseInfo legPressInfo = new ExerciseInfo();
        legPressInfo.setVideoId("KbZCkI_WzWY");
        legPressInfo.setDescription(Arrays.asList(
                "Usiądź na maszynie do wyciskania nogami i oprzyj stopy na platformie.",
                "Ustaw stopy na szerokość barków.",
                "Odblokuj zabezpieczenia maszyny.",
                "Zginaj kolana, opuszczając platformę w kontrolowany sposób.",
                "Wypchnij platformę do góry, prostując nogi, ale nie blokując kolan."
        ));
        legPressInfo.setDifficultyLevel(1);

        ExerciseInfo legCurlInfo = new ExerciseInfo();
        legCurlInfo.setVideoId("dXc_dkkemOc");
        legCurlInfo.setDescription(Arrays.asList(
                "Połóż się na maszynie do uginania nóg leżąc na brzuchu.",
                "Ustaw wałek nad kostkami.",
                "Uginaj nogi w kolanach, przyciągając wałek w kierunku pośladków.",
                "Zatrzymaj ruch na chwilę na górze.",
                "Powoli opuszczaj nogi do pozycji wyjściowej."
        ));
        legCurlInfo.setDifficultyLevel(1);

        ExerciseInfo calfRaiseInfo = new ExerciseInfo();
        calfRaiseInfo.setVideoId("Wri0VppFWCY");
        calfRaiseInfo.setDescription(Arrays.asList(
                "Stań na platformie maszyny do wspięć na palce, stopy na szerokość bioder.",
                "Opuszczaj pięty poniżej poziomu platformy, rozciągając łydki.",
                "Wspinaj się na palce, unosząc pięty tak wysoko, jak to możliwe.",
                "Zatrzymaj ruch na chwilę na górze.",
                "Powoli opuszczaj pięty do pozycji wyjściowej."
        ));
        calfRaiseInfo.setDifficultyLevel(2);

        // CHEST
        ExerciseInfo benchPressInfo = new ExerciseInfo();
        benchPressInfo.setVideoId("8_33og5lN-Y");
        benchPressInfo.setDescription(Arrays.asList(
                "Połóż się na ławce, nogi ustawione stabilnie na podłożu.",
                "Chwyć sztangę na szerokość barków i unieś ją nad klatką piersiową.",
                "Opuść sztangę powoli do klatki, zginając łokcie.",
                "Wypchnij sztangę do góry, prostując ramiona."
        ));
        benchPressInfo.setDifficultyLevel(2);

        ExerciseInfo inclineDumbbellPressInfo = new ExerciseInfo();
        inclineDumbbellPressInfo.setVideoId("C06qMsCzjQ8");
        inclineDumbbellPressInfo.setDescription(Arrays.asList(
                "Ustaw ławkę pod kątem 30-45 stopni.",
                "Połóż się na ławce z hantlami w dłoniach na wysokości klatki piersiowej.",
                "Wypchnij hantle do góry, prostując ramiona.",
                "Powoli opuszczaj hantle do pozycji wyjściowej."
        ));
        inclineDumbbellPressInfo.setDifficultyLevel(3);

        ExerciseInfo chestFlyInfo = new ExerciseInfo();
        chestFlyInfo.setVideoId("BiYKN3uBpOQ");
        chestFlyInfo.setDescription(Arrays.asList(
                "Połóż się na ławce z hantlami trzymanymi nad klatką piersiową, ramiona lekko zgięte.",
                "Rozłóż ramiona na boki, opuszczając hantle w kontrolowany sposób.",
                "Poczuj rozciąganie w mięśniach klatki piersiowej.",
                "Przywiedź ramiona z powrotem do pozycji wyjściowej."
        ));
        chestFlyInfo.setDifficultyLevel(3);

        ExerciseInfo pushUpsInfo = new ExerciseInfo();
        pushUpsInfo.setVideoId("jBpX70SayRU");
        pushUpsInfo.setDescription(Arrays.asList(
                "Przyjmij pozycję deski, dłonie na szerokość barków.",
                "Utrzymuj ciało prosto od głowy do pięt.",
                "Zginaj łokcie, opuszczając ciało w kierunku podłogi.",
                "Wypchnij ciało do góry, prostując ramiona."
        ));
        pushUpsInfo.setDifficultyLevel(1);

        ExerciseInfo dipsInfo = new ExerciseInfo();
        dipsInfo.setVideoId("wLR8VJJk7W8");
        dipsInfo.setDescription(Arrays.asList(
                "Chwyć poręcze równoległe i unieś ciało, prostując ramiona.",
                "Zginaj łokcie, opuszczając ciało w dół, utrzymując tułów lekko pochylony do przodu.",
                "Schodź w dół, aż ramiona będą równoległe do podłoża.",
                "Wypchnij ciało do góry, prostując ramiona."
        ));
        dipsInfo.setDifficultyLevel(2);

// BACK
        ExerciseInfo deadliftInfo = new ExerciseInfo();
        deadliftInfo.setVideoId("0_igODjLiXM");
        deadliftInfo.setDescription(Arrays.asList(
                "Ustaw stopy pod sztangą na szerokość bioder.",
                "Pochyl się i chwyć sztangę nachwytem lub chwytem mieszanym.",
                "Utrzymuj plecy proste i głowę uniesioną.",
                "Prostuj nogi i tułów jednocześnie, unosząc sztangę wzdłuż ciała.",
                "Wróć do pozycji wyjściowej, kontrolując ruch w dół."
        ));
        deadliftInfo.setDifficultyLevel(4);

        ExerciseInfo pullUpsInfo = new ExerciseInfo();
        pullUpsInfo.setVideoId("ZgK85mzxm5Y");
        pullUpsInfo.setDescription(Arrays.asList(
                "Chwyć drążek nachwytem na szerokość barków lub szerzej.",
                "Wisząc swobodnie, napnij mięśnie grzbietu.",
                "Podciągnij ciało, aż broda znajdzie się nad drążkiem.",
                "Powoli opuść ciało do pozycji wyjściowej."
        ));
        pullUpsInfo.setDifficultyLevel(1);

        ExerciseInfo latPulldownInfo = new ExerciseInfo();
        latPulldownInfo.setVideoId("NqD6Mh_zrzY");
        latPulldownInfo.setDescription(Arrays.asList(
                "Usiądź przy maszynie do ściągania drążka.",
                "Chwyć drążek nachwytem na szerokość barków lub szerzej.",
                "Ściągnij drążek w dół do klatki piersiowej, napinając mięśnie grzbietu.",
                "Powoli pozwól drążkowi wrócić do pozycji wyjściowej."
        ));
        latPulldownInfo.setDifficultyLevel(2);

        ExerciseInfo bentOverRowInfo = new ExerciseInfo();
        bentOverRowInfo.setVideoId("KFVOo0eLvkI");
        bentOverRowInfo.setDescription(Arrays.asList(
                "Stań z nogami na szerokość bioder, lekko ugięte kolana.",
                "Pochyl tułów do przodu, utrzymując plecy proste.",
                "Chwyć sztangę nachwytem.",
                "Przyciągnij sztangę do brzucha, zginając łokcie.",
                "Powoli opuść sztangę do pozycji wyjściowej."
        ));
        bentOverRowInfo.setDifficultyLevel(3);

        ExerciseInfo tBarRowInfo = new ExerciseInfo();
        tBarRowInfo.setVideoId("yPis7nlbqdY");
        tBarRowInfo.setDescription(Arrays.asList(
                "Stań nad sztangą T-Bar, stopy na szerokość bioder.",
                "Pochyl się do przodu, plecy proste.",
                "Chwyć uchwyt T-Bar.",
                "Przyciągnij uchwyt do klatki piersiowej, zginając łokcie.",
                "Powoli opuść uchwyt do pozycji wyjściowej."
        ));
        tBarRowInfo.setDifficultyLevel(4);

// BICEPS
        ExerciseInfo barbellCurlInfo = new ExerciseInfo();
        barbellCurlInfo.setVideoId("wHbgdQ5rS7g");
        barbellCurlInfo.setDescription(Arrays.asList(
                "Stań prosto, stopy na szerokość bioder, trzymając sztangę podchwytem.",
                "Utrzymuj łokcie blisko ciała.",
                "Unosź sztangę do przodu, zginając łokcie.",
                "Napnij bicepsy na górze ruchu.",
                "Powoli opuszczaj sztangę do pozycji wyjściowej."
        ));

        barbellCurlInfo.setDifficultyLevel(1);

        ExerciseInfo dumbbellCurlInfo = new ExerciseInfo();
        dumbbellCurlInfo.setVideoId("RTYQSbHTwOg");
        dumbbellCurlInfo.setDescription(Arrays.asList(
                "Stań prosto z hantlami w dłoniach, ramiona wzdłuż ciała.",
                "Utrzymuj łokcie blisko ciała.",
                "Unosź hantle do przodu, zginając łokcie.",
                "Napnij bicepsy na górze ruchu.",
                "Powoli opuszczaj hantle do pozycji wyjściowej."
        ));
        dumbbellCurlInfo.setDifficultyLevel(1);

        ExerciseInfo concentrationCurlInfo = new ExerciseInfo();
        concentrationCurlInfo.setVideoId("t-dVA6SGLGU");
        concentrationCurlInfo.setDescription(Arrays.asList(
                "Usiądź na ławce, nogi rozstawione szeroko.",
                "Oprzyj łokieć prawej ręki o wewnętrzną część prawego uda.",
                "Chwyć hantlę i unosź ją, zginając łokieć.",
                "Napnij biceps na górze ruchu.",
                "Powoli opuszczaj hantlę do pozycji wyjściowej.",
                "Powtórz na drugą rękę."
        ));
        concentrationCurlInfo.setDifficultyLevel(2);

        ExerciseInfo hammerCurlInfo = new ExerciseInfo();
        hammerCurlInfo.setVideoId("s_ubLsRZ59I");
        hammerCurlInfo.setDescription(Arrays.asList(
                "Stań prosto z hantlami w dłoniach, uchwyt młotkowy (kciuki skierowane do góry).",
                "Utrzymuj łokcie blisko ciała.",
                "Unosź hantle, zginając łokcie.",
                "Napnij bicepsy na górze ruchu.",
                "Powoli opuszczaj hantle do pozycji wyjściowej."
        ));
        hammerCurlInfo.setDifficultyLevel(1);

        ExerciseInfo preacherCurlInfo = new ExerciseInfo();
        preacherCurlInfo.setVideoId("VeSNU4DBkPw");
        preacherCurlInfo.setDescription(Arrays.asList(
                "Usiądź na maszynie do uginania przedramion na modlitewniku.",
                "Oprzyj ramiona na poduszce, chwytając sztangę podchwytem.",
                "Unosź sztangę, zginając łokcie.",
                "Napnij bicepsy na górze ruchu.",
                "Powoli opuszczaj sztangę do pozycji wyjściowej."
        ));
        preacherCurlInfo.setDifficultyLevel(1);

// TRICEPS
        ExerciseInfo tricepsPushdownInfo = new ExerciseInfo();
        tricepsPushdownInfo.setVideoId("1OqWx8xmeRs");
        tricepsPushdownInfo.setDescription(Arrays.asList(
                "Stań przed wyciągiem górnym, chwyć uchwyt nachwytem.",
                "Utrzymuj łokcie blisko ciała.",
                "Prostuj ramiona, pchając uchwyt w dół.",
                "Napnij tricepsy na dole ruchu.",
                "Powoli zginaj łokcie, wracając do pozycji wyjściowej."
        ));
        tricepsPushdownInfo.setDifficultyLevel(2);

        ExerciseInfo overheadTricepsExtensionInfo = new ExerciseInfo();
        overheadTricepsExtensionInfo.setVideoId("J2aSNEdb7qY");
        overheadTricepsExtensionInfo.setDescription(Arrays.asList(
                "Stań prosto, trzymając hantlę oburącz nad głową.",
                "Utrzymuj ramiona prosto w górze, łokcie blisko głowy.",
                "Zginaj łokcie, opuszczając hantlę za głowę.",
                "Prostuj ramiona, unosząc hantlę z powrotem nad głowę.",
                "Napnij tricepsy na górze ruchu."
        ));
        overheadTricepsExtensionInfo.setDifficultyLevel(3);

        ExerciseInfo skullCrusherInfo = new ExerciseInfo();
        skullCrusherInfo.setVideoId("UlLhdI3KOTg");
        skullCrusherInfo.setDescription(Arrays.asList(
                "Połóż się na ławce, trzymając sztangę prosto nad klatką piersiową.",
                "Utrzymuj ramiona nieruchomo, zginaj łokcie, opuszczając sztangę w kierunku czoła.",
                "Prostuj łokcie, unosząc sztangę z powrotem do pozycji wyjściowej.",
                "Napnij tricepsy na górze ruchu."
        ));
        skullCrusherInfo.setDifficultyLevel(3);

        ExerciseInfo tricepsDipsInfo = new ExerciseInfo();
        tricepsDipsInfo.setVideoId("flSs1CfQS6k");
        tricepsDipsInfo.setDescription(Arrays.asList(
                "Usiądź na krawędzi ławki, dłonie po bokach bioder.",
                "Wyprostuj nogi przed sobą.",
                "Przesuń biodra poza ławkę, podpierając się rękami.",
                "Zginaj łokcie, opuszczając ciało w dół.",
                "Prostuj ramiona, unosząc ciało z powrotem do góry."
        ));
        tricepsDipsInfo.setDifficultyLevel(2);

        ExerciseInfo closeGripBenchPressInfo = new ExerciseInfo();
        closeGripBenchPressInfo.setVideoId("b3qYZeDywgI");
        closeGripBenchPressInfo.setDescription(Arrays.asList(
                "Połóż się na ławce, chwyć sztangę wąsko (ok. 15 cm między dłońmi).",
                "Unieś sztangę nad klatką piersiową.",
                "Opuść sztangę do dolnej części klatki piersiowej, zginając łokcie blisko ciała.",
                "Wypchnij sztangę do góry, prostując ramiona."
        ));
        closeGripBenchPressInfo.setDifficultyLevel(2);

// SHOULDERS
        ExerciseInfo shoulderPressInfo = new ExerciseInfo();
        shoulderPressInfo.setVideoId("EC7NuM2gmB0");
        shoulderPressInfo.setDescription(Arrays.asList(
                "Usiądź na ławce z oparciem, trzymając hantle na wysokości ramion.",
                "Wypchnij hantle do góry, prostując ramiona.",
                "Powoli opuszczaj hantle do pozycji wyjściowej.",
                "Utrzymuj plecy proste i napięte mięśnie brzucha."
        ));
        shoulderPressInfo.setDifficultyLevel(3);

        ExerciseInfo lateralRaiseInfo = new ExerciseInfo();
        lateralRaiseInfo.setVideoId("EC7NuM2gmB0");
        lateralRaiseInfo.setDescription(Arrays.asList(
                "Stań prosto z hantlami w dłoniach, ramiona wzdłuż ciała.",
                "Unosź ramiona na boki, aż będą równoległe do podłoża.",
                "Utrzymuj lekkie zgięcie w łokciach.",
                "Powoli opuszczaj ramiona do pozycji wyjściowej."
        ));
        lateralRaiseInfo.setDifficultyLevel(3);

        ExerciseInfo frontRaiseInfo = new ExerciseInfo();
        frontRaiseInfo.setVideoId("tzNuowdBtUw");
        frontRaiseInfo.setDescription(Arrays.asList(
                "Stań prosto z hantlami w dłoniach przed udami.",
                "Unoś ramię do przodu, aż będzie równoległe do podłoża.",
                "Powoli opuszczaj ramię do pozycji wyjściowej.",
                "Powtórz na drugą rękę."
        ));
        frontRaiseInfo.setDifficultyLevel(3);

        ExerciseInfo arnoldPressInfo = new ExerciseInfo();
        arnoldPressInfo.setVideoId("HId_yBfTGgQ");
        arnoldPressInfo.setDescription(Arrays.asList(
                "Usiądź na ławce z oparciem, trzymając hantle przed klatką piersiową, dłonie skierowane do ciała.",
                "Wypchnij hantle do góry, jednocześnie obracając nadgarstki, aż dłonie będą skierowane na zewnątrz.",
                "Powoli opuszczaj hantle do pozycji wyjściowej, obracając nadgarstki.",
                "Utrzymuj kontrolowany ruch przez cały czas."
        ));
        arnoldPressInfo.setDifficultyLevel(3);

// ABS
        ExerciseInfo crunchesInfo = new ExerciseInfo();
        crunchesInfo.setVideoId("XgQXdzxE2hU");
        crunchesInfo.setDescription(Arrays.asList(
                "Połóż się na plecach, kolana ugięte, stopy na podłodze.",
                "Ręce skrzyżowane na klatce piersiowej lub za głową.",
                "Napnij mięśnie brzucha i unieś górną część tułowia.",
                "Powoli opuszczaj tułów do pozycji wyjściowej."
        ));
        crunchesInfo.setDifficultyLevel(1);

        ExerciseInfo legRaiseInfo = new ExerciseInfo();
        legRaiseInfo.setVideoId("_zEXEoVDxvk");
        legRaiseInfo.setDescription(Arrays.asList(
                "Połóż się na plecach, ręce wzdłuż ciała.",
                "Unieś nogi prosto w górę, stopy skierowane do sufitu.",
                "Powoli opuszczaj nogi, nie dotykając podłogi.",
                "Powtórz ruch unoszenia nóg."
        ));
        legRaiseInfo.setDifficultyLevel(1);


        ExerciseInfo plankInfo = new ExerciseInfo();
        plankInfo.setVideoId("qN9skuMi6es");
        plankInfo.setDescription(Arrays.asList(
                "Przyjmij pozycję jak do pompki, opierając się na przedramionach.",
                "Utrzymuj ciało prosto od głowy do pięt.",
                "Napnij mięśnie brzucha i pośladków.",
                "Utrzymaj pozycję przez określony czas."
        ));
        plankInfo.setDifficultyLevel(2);

        ExerciseInfo russianTwistInfo = new ExerciseInfo();
        russianTwistInfo.setVideoId("AwoiVWAi4bo");
        russianTwistInfo.setDescription(Arrays.asList(
                "Usiądź na podłodze, kolana ugięte, stopy uniesione.",
                "Pochyl tułów lekko do tyłu, utrzymując równowagę.",
                "Trzymaj ręce złączone przed sobą.",
                "Skręcaj tułów w lewo i prawo, dotykając podłogi po obu stronach."
        ));
        russianTwistInfo.setDifficultyLevel(2);

        ExerciseInfo bicycleCrunchInfo = new ExerciseInfo();
        bicycleCrunchInfo.setVideoId("UFxFYg0sdIw");
        bicycleCrunchInfo.setDescription(Arrays.asList(
                "Połóż się na plecach, ręce za głową.",
                "Unieś nogi zgięte w kolanach pod kątem 90 stopni.",
                "Przyciągnij lewe kolano do prawego łokcia, prostując prawą nogę.",
                "Zmień stronę, przyciągając prawe kolano do lewego łokcia.",
                "Kontynuuj naprzemienny ruch."
        ));
        bicycleCrunchInfo.setDifficultyLevel(2);


        List<Exercise> exercises = Arrays.asList(
                // LEGS
                new Exercise(null, "Przysiad ze sztangą", CategoryName.NOGI, squatInfo),
                new Exercise(null, "Wykroki", CategoryName.NOGI, lungesInfo),
                new Exercise(null, "Wyciskanie na suwnicy", CategoryName.NOGI, legPressInfo),
                new Exercise(null, "Uginanie nóg na maszynie", CategoryName.NOGI, legCurlInfo),
                new Exercise(null, "Wspięcia na palcach", CategoryName.NOGI, calfRaiseInfo),

                // CHEST
                new Exercise(null, "Wyciskanie sztangi leżąc", CategoryName.KLATKA_PIERSIOWA, benchPressInfo),
                new Exercise(null, "Wyciskanie hantli na ławce skośnej", CategoryName.KLATKA_PIERSIOWA, inclineDumbbellPressInfo),
                new Exercise(null, "Rozpiętki", CategoryName.KLATKA_PIERSIOWA, chestFlyInfo),
                new Exercise(null, "Pompki", CategoryName.KLATKA_PIERSIOWA, pushUpsInfo),
                new Exercise(null, "Dipy na klatkę", CategoryName.KLATKA_PIERSIOWA, dipsInfo),

                // BACK
                new Exercise(null, "Martwy ciąg", CategoryName.PLECY, deadliftInfo),
                new Exercise(null, "Podciągnięcia", CategoryName.PLECY, pullUpsInfo),
                new Exercise(null, "Ściąganie drążka", CategoryName.PLECY, latPulldownInfo),
                new Exercise(null, "Wiosłowanie w opadzie", CategoryName.PLECY, bentOverRowInfo),
                new Exercise(null, "Wiosłowanie T-bar", CategoryName.PLECY, tBarRowInfo),

                // BICEPS
                new Exercise(null, "Uginanie ramion ze sztangą", CategoryName.BICEPS, barbellCurlInfo),
                new Exercise(null, "Uginanie ramion z hantlami", CategoryName.BICEPS, dumbbellCurlInfo),
                new Exercise(null, "Zginanie przedramienia na udzie", CategoryName.BICEPS, concentrationCurlInfo),
                new Exercise(null, "Młotki", CategoryName.BICEPS, hammerCurlInfo),
                new Exercise(null, "Zginanie przedraiom na modlitewniku", CategoryName.BICEPS, preacherCurlInfo),

                // TRICEPS
                new Exercise(null, "Prostowanie ramion na wyciągu górnym", CategoryName.TRICEPS, tricepsPushdownInfo),
                new Exercise(null, "Wyprost ramion nad głową", CategoryName.TRICEPS, overheadTricepsExtensionInfo),
                new Exercise(null, "Wyciskanie francuskie", CategoryName.TRICEPS, skullCrusherInfo),
                new Exercise(null, "Dipy na triceps", CategoryName.TRICEPS, tricepsDipsInfo),
                new Exercise(null, "Wyciskanie sztangi wąskim chwytem", CategoryName.TRICEPS, closeGripBenchPressInfo),

                // SHOULDERS
                new Exercise(null, "Wyciskanie hantli siedząc", CategoryName.BARKI, shoulderPressInfo),
                new Exercise(null, "Wznosy bokiem", CategoryName.BARKI, lateralRaiseInfo),
                new Exercise(null, "Wznosy przodem", CategoryName.BARKI, frontRaiseInfo),
                new Exercise(null, "Arnoldki", CategoryName.BARKI, arnoldPressInfo),

                // ABS
                new Exercise(null, "Brzuszki", CategoryName.BRZUCH, crunchesInfo),
                new Exercise(null, "Unoszenie nóg", CategoryName.BRZUCH, legRaiseInfo),
                new Exercise(null, "Deska", CategoryName.BRZUCH, plankInfo),
                new Exercise(null, "Russian twist", CategoryName.BRZUCH, russianTwistInfo),
                new Exercise(null, "Rowerek", CategoryName.BRZUCH, bicycleCrunchInfo)
        );

        exerciseRepository.saveAll(exercises);
    }


    public void addWorkoutSession() {
        User user = userRepository.findByEmail("kowalski@wp.pl")
                .orElseThrow(() -> new RuntimeException("User not found: kowalski@wp.pl"));

        List<WorkoutSessionDTO> workoutSessions = new ArrayList<>();
        LocalDate startDate = LocalDate.of(2024, 9, 10);

        String[] exercises = {
                "Przysiad ze sztangą", "Martwy ciąg", "Podciągnięcia",
                "Wyciskanie sztangi leżąc", "Rozpiętki",
                "Wiosłowanie w opadzie", "Wyciskanie hantli siedząc",
                "Prostowanie ramion na wyciągu górnym", "Pompki"
        };

        double baseWeight = 40.0;
        int baseReps = 10;
        int baseSets = 3;


        for (int i = 0; i < 30; i++) {
            LocalDate sessionDate = startDate.plusDays(i * 2);
            List<ExerciseSessionDTO> exerciseSessions = new ArrayList<>();

            for (int j = 0; j < 5; j++) {
                String exerciseName = exercises[(i + j) % exercises.length];
                double weight = baseWeight + ((double) i / 2);
                int reps = baseReps + (i / 5);

                List<ExerciseSetDTO> sets = new ArrayList<>();
                for (int k = 0; k < baseSets; k++) {
                    sets.add(new ExerciseSetDTO(reps - k, weight - (k * 2.5)));
                }

                exerciseSessions.add(createExerciseSession(exerciseName, sets));
            }

            workoutSessions.add(new WorkoutSessionDTO(
                    sessionDate,
                    "Szybki trening po pracy, bardzo dobre samopoczucie.",
                    exerciseSessions
            ));
        }

        for (WorkoutSessionDTO workoutSessionDTO : workoutSessions) {
            WorkoutSession workoutSession = workoutMapper.mapToEntity(workoutSessionDTO);
            workoutSession.setUser(user);
            workoutSessionRepository.save(workoutSession);
        }
    }

    private ExerciseSessionDTO createExerciseSession(String exerciseName, List<ExerciseSetDTO> sets) {
        Exercise exercise = exerciseRepository.findByName(exerciseName)
                .orElseThrow(() -> new RuntimeException("Exercise not found: " + exerciseName));
        return new ExerciseSessionDTO(exercise.getId(), exercise.getName(), sets);
    }


    public void addTrainingPlans() {
        User user = userRepository.findByEmail("kowalski@wp.pl")
                .orElseThrow(() -> new RuntimeException("User not found: kowalski@wp.pl"));

        List<Exercise> allExercises = exerciseRepository.findAll();
        Map<String, Long> exerciseMap = new HashMap<>();
        for (Exercise exercise : allExercises) {
            exerciseMap.put(exercise.getName(), exercise.getId());
        }

        // Example training plans
        CreateTrainingPlanDTO plan1 = new CreateTrainingPlanDTO(
                "Plan Siłowy",
                "Plan skupiający się na zwiększeniu siły mięśniowej.",
                Arrays.asList(
                        new ExerciseInPlanDTO(exerciseMap.get("Przysiad ze sztangą"), "3 serie x 5 powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Wyciskanie sztangi leżąc"), "3 serie x 5 powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Martwy ciąg"), "1 seria x 5 powtórzeń")
                )
        );

        CreateTrainingPlanDTO plan2 = new CreateTrainingPlanDTO(
                "Plan Masowy",
                "Plan dla osób chcących zbudować masę mięśniową.",
                Arrays.asList(
                        new ExerciseInPlanDTO(exerciseMap.get("Wyciskanie sztangi leżąc"), "4 serie x 8 powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Wiosłowanie w opadzie"), "4 serie x 8 powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Przysiad ze sztangą"), "4 serie x 8 powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Wyciskanie hantli siedząc"), "4 serie x 10 powtórzeń")
                )
        );

        CreateTrainingPlanDTO plan3 = new CreateTrainingPlanDTO(
                "Plan Redukcyjny",
                "Plan dla osób chcących zredukować tkankę tłuszczową.",
                Arrays.asList(
                        new ExerciseInPlanDTO(exerciseMap.get("Pompki"), "3 serie x maksymalna liczba powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Podciągnięcia"), "3 serie x maksymalna liczba powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Brzuszki"), "3 serie x 20 powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Przysiad ze sztangą"), "3 serie x 12 powtórzeń")
                )
        );

        CreateTrainingPlanDTO plan4 = new CreateTrainingPlanDTO(
                "Plan na Siłę i Masę",
                "Zaawansowany plan łączący trening siłowy i masowy.",
                Arrays.asList(
                        new ExerciseInPlanDTO(exerciseMap.get("Martwy ciąg"), "5 serii x 5 powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Wyciskanie sztangi leżąc"), "5 serii x 5 powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Przysiad ze sztangą"), "5 serii x 5 powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Wiosłowanie T-bar"), "5 serii x 5 powtórzeń")
                )
        );

        CreateTrainingPlanDTO plan5 = new CreateTrainingPlanDTO(
                "Plan FBW",
                "Trening całego ciała na jednej sesji.",
                Arrays.asList(
                        new ExerciseInPlanDTO(exerciseMap.get("Przysiad ze sztangą"), "3 serie x 10 powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Wyciskanie sztangi leżąc"), "3 serie x 10 powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Wiosłowanie w opadzie"), "3 serie x 10 powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Wyciskanie hantli siedząc"), "3 serie x 12 powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Uginanie ramion ze sztangą"), "3 serie x 12 powtórzeń"),
                        new ExerciseInPlanDTO(exerciseMap.get("Prostowanie ramion na wyciągu górnym"), "3 serie x 12 powtórzeń")
                )
        );


        List<CreateTrainingPlanDTO> plans = Arrays.asList(plan1, plan2, plan3, plan4, plan5);

        List<User> users = userRepository.findAll();

        Random random = new Random();

        for (CreateTrainingPlanDTO planDTO : plans) {
            User randomUser = users.get(random.nextInt(users.size()));
            userService.setCurrentUser(user);
            trainingPlanService.saveTrainingPlan(planDTO);
        }
    }

    public void addPersonalInfo() throws IOException {
        PersonalInfo personalInfo = personalInfoRepository.findById(1L).orElseThrow();
        personalInfo.setBio("Cześć wszystkim! Sportem zajmuję się od 10 lat. Na początku skupiony byłem na bieganiu. Od 4 lat ćwiczę na siłowni, średnio 4/5 razy w tygodniu. Mój obecny cel to 150kg na klatę :)");
        personalInfo.setWeight(85.0);
        personalInfo.setGender(Gender.MALE);
        personalInfo.setHeight(188.0);

        Path imagePath = Path.of("src/main/resources/static/images/man_avatar.jpg");
        byte[] avatarBytes = Files.readAllBytes(imagePath);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(avatarBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(inputStream)
                .size(360, 360)
                .outputFormat("jpeg")
                .toOutputStream(outputStream);


        personalInfo.setAvatar(outputStream.toByteArray());

        personalInfoRepository.save(personalInfo);
    }

    public void addPost() {
        User user = userRepository.findByEmail("kowalski@wp.pl")
                .orElseThrow(() -> new RuntimeException("User not found: kowalski@wp.pl"));


        String[] paths = {
                "http://localhost:8080/api/v1/posts/images/1732189061756_biceps.jpg",
                "http://localhost:8080/api/v1/posts/images/1732189061854_plecy.jpg",
                "http://localhost:8080/api/v1/posts/images/1732189061891_klata.jpg"
        };

        for (int i = 0; i < 3; i++) {
            Post post = new Post();
            post.setUser(user);
            post.setImagePath(paths[i]);

            WorkoutSession workoutSession = workoutSessionRepository.findById((long) (i + 1)).orElseThrow();
            post.setWorkoutSession(workoutSession);
            post.setTimestamp(LocalDate.of(2024, 9, 10 + (2 * i)));
            post.setDescription("Pozdro z treningu. Progress mały, ale zawsze do przodu :D");

            postRepository.save(post);
        }


    }


}

