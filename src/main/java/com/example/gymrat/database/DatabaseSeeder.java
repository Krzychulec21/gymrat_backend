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

            for (int j = 0; j < 6; j++) {
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
                    "Trening " + (i + 1),
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

