package com.example.gymrat.database;

import com.example.gymrat.DTO.auth.RegisterRequest;
import com.example.gymrat.DTO.workout.ExerciseSessionDTO;
import com.example.gymrat.DTO.workout.ExerciseSetDTO;
import com.example.gymrat.DTO.workout.WorkoutSessionDTO;
import com.example.gymrat.mapper.WorkoutMapper;
import com.example.gymrat.model.*;
import com.example.gymrat.repository.ExerciseRepository;
import com.example.gymrat.repository.UserRepository;
import com.example.gymrat.repository.WorkoutSessionRepository;
import com.example.gymrat.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserService userService;
    private final FriendService friendService;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutMapper workoutMapper;

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
        // LEGS
        ExerciseInfo squatInfo = new ExerciseInfo();
        squatInfo.setVideoId("xqvCmoLULNY");
        squatInfo.setDescription(Arrays.asList(
                "Ustaw stopy na szerokość barków, palce skierowane lekko na zewnątrz.",
                "Utrzymuj klatkę piersiową w górze i patrz przed siebie.",
                "Zginaj kolana i biodra, obniżając ciało jakbyś chciał usiąść na krześle.",
                "Schodź w dół aż uda będą równoległe do podłoża lub niżej.",
                "Wróć do pozycji wyjściowej, prostując nogi i napinając pośladki."
        ));

        ExerciseInfo lungesInfo = new ExerciseInfo();
        lungesInfo.setVideoId("wrwwXE_x-pQ");
        lungesInfo.setDescription(Arrays.asList(
                "Stój prosto z nogami rozstawionymi na szerokość bioder.",
                "Zrób duży krok do przodu jedną nogą.",
                "Zginaj oba kolana, obniżając ciało, aż tylne kolano prawie dotknie podłogi.",
                "Wróć do pozycji wyjściowej, odpychając się przednią stopą.",
                "Powtórz na drugą nogę."
        ));

        ExerciseInfo legPressInfo = new ExerciseInfo();
        legPressInfo.setVideoId("BI32qWAjSzc");
        legPressInfo.setDescription(Arrays.asList(
                "Usiądź na maszynie do wyciskania nogami i oprzyj stopy na platformie.",
                "Ustaw stopy na szerokość barków.",
                "Odblokuj zabezpieczenia maszyny.",
                "Zginaj kolana, opuszczając platformę w kontrolowany sposób.",
                "Wypchnij platformę do góry, prostując nogi, ale nie blokując kolan."
        ));

        ExerciseInfo legCurlInfo = new ExerciseInfo();
        legCurlInfo.setVideoId("xFvmc42YfMQ");
        legCurlInfo.setDescription(Arrays.asList(
                "Połóż się na maszynie do uginania nóg leżąc na brzuchu.",
                "Ustaw wałek nad kostkami.",
                "Uginaj nogi w kolanach, przyciągając wałek w kierunku pośladków.",
                "Zatrzymaj ruch na chwilę na górze.",
                "Powoli opuszczaj nogi do pozycji wyjściowej."
        ));

        ExerciseInfo calfRaiseInfo = new ExerciseInfo();
        calfRaiseInfo.setVideoId("ydPylCo-T7U");
        calfRaiseInfo.setDescription(Arrays.asList(
                "Stań na platformie maszyny do wspięć na palce, stopy na szerokość bioder.",
                "Opuszczaj pięty poniżej poziomu platformy, rozciągając łydki.",
                "Wspinaj się na palce, unosząc pięty tak wysoko, jak to możliwe.",
                "Zatrzymaj ruch na chwilę na górze.",
                "Powoli opuszczaj pięty do pozycji wyjściowej."
        ));

        // CHEST
        ExerciseInfo benchPressInfo = new ExerciseInfo();
        benchPressInfo.setVideoId("3R0SOJ3alTA");
        benchPressInfo.setDescription(Arrays.asList(
                "Połóż się na ławce, nogi ustawione stabilnie na podłożu.",
                "Chwyć sztangę na szerokość barków i unieś ją nad klatką piersiową.",
                "Opuść sztangę powoli do klatki, zginając łokcie.",
                "Wypchnij sztangę do góry, prostując ramiona."
        ));

        ExerciseInfo inclineDumbbellPressInfo = new ExerciseInfo();
        inclineDumbbellPressInfo.setVideoId("CnGWry8yekI");
        inclineDumbbellPressInfo.setDescription(Arrays.asList(
                "Ustaw ławkę pod kątem 30-45 stopni.",
                "Połóż się na ławce z hantlami w dłoniach na wysokości klatki piersiowej.",
                "Wypchnij hantle do góry, prostując ramiona.",
                "Powoli opuszczaj hantle do pozycji wyjściowej."
        ));

        ExerciseInfo chestFlyInfo = new ExerciseInfo();
        chestFlyInfo.setVideoId("http://localhost:8080/images/chestfly.jpg");
        chestFlyInfo.setDescription(Arrays.asList(
                "Połóż się na ławce z hantlami trzymanymi nad klatką piersiową, ramiona lekko zgięte.",
                "Rozłóż ramiona na boki, opuszczając hantle w kontrolowany sposób.",
                "Poczuj rozciąganie w mięśniach klatki piersiowej.",
                "Przywiedź ramiona z powrotem do pozycji wyjściowej."
        ));

        ExerciseInfo pushUpsInfo = new ExerciseInfo();
        pushUpsInfo.setVideoId("http://localhost:8080/images/pushups.jpg");
        pushUpsInfo.setDescription(Arrays.asList(
                "Przyjmij pozycję deski, dłonie na szerokość barków.",
                "Utrzymuj ciało prosto od głowy do pięt.",
                "Zginaj łokcie, opuszczając ciało w kierunku podłogi.",
                "Wypchnij ciało do góry, prostując ramiona."
        ));

        ExerciseInfo dipsInfo = new ExerciseInfo();
        dipsInfo.setVideoId("http://localhost:8080/images/dips.jpg");
        dipsInfo.setDescription(Arrays.asList(
                "Chwyć poręcze równoległe i unieś ciało, prostując ramiona.",
                "Zginaj łokcie, opuszczając ciało w dół, utrzymując tułów lekko pochylony do przodu.",
                "Schodź w dół, aż ramiona będą równoległe do podłoża.",
                "Wypchnij ciało do góry, prostując ramiona."
        ));

// BACK
        ExerciseInfo deadliftInfo = new ExerciseInfo();
        deadliftInfo.setVideoId("http://localhost:8080/images/deadlift.jpg");
        deadliftInfo.setDescription(Arrays.asList(
                "Ustaw stopy pod sztangą na szerokość bioder.",
                "Pochyl się i chwyć sztangę nachwytem lub chwytem mieszanym.",
                "Utrzymuj plecy proste i głowę uniesioną.",
                "Prostuj nogi i tułów jednocześnie, unosząc sztangę wzdłuż ciała.",
                "Wróć do pozycji wyjściowej, kontrolując ruch w dół."
        ));

        ExerciseInfo pullUpsInfo = new ExerciseInfo();
        pullUpsInfo.setVideoId("http://localhost:8080/images/pullups.jpg");
        pullUpsInfo.setDescription(Arrays.asList(
                "Chwyć drążek nachwytem na szerokość barków lub szerzej.",
                "Wisząc swobodnie, napnij mięśnie grzbietu.",
                "Podciągnij ciało, aż broda znajdzie się nad drążkiem.",
                "Powoli opuść ciało do pozycji wyjściowej."
        ));

        ExerciseInfo latPulldownInfo = new ExerciseInfo();
        latPulldownInfo.setVideoId("http://localhost:8080/images/latpulldown.jpg");
        latPulldownInfo.setDescription(Arrays.asList(
                "Usiądź przy maszynie do ściągania drążka.",
                "Chwyć drążek nachwytem na szerokość barków lub szerzej.",
                "Ściągnij drążek w dół do klatki piersiowej, napinając mięśnie grzbietu.",
                "Powoli pozwól drążkowi wrócić do pozycji wyjściowej."
        ));

        ExerciseInfo bentOverRowInfo = new ExerciseInfo();
        bentOverRowInfo.setVideoId("http://localhost:8080/images/bentoverrow.jpg");
        bentOverRowInfo.setDescription(Arrays.asList(
                "Stań z nogami na szerokość bioder, lekko ugięte kolana.",
                "Pochyl tułów do przodu, utrzymując plecy proste.",
                "Chwyć sztangę nachwytem.",
                "Przyciągnij sztangę do brzucha, zginając łokcie.",
                "Powoli opuść sztangę do pozycji wyjściowej."
        ));

        ExerciseInfo tBarRowInfo = new ExerciseInfo();
        tBarRowInfo.setVideoId("http://localhost:8080/images/tbarrow.jpg");
        tBarRowInfo.setDescription(Arrays.asList(
                "Stań nad sztangą T-Bar, stopy na szerokość bioder.",
                "Pochyl się do przodu, plecy proste.",
                "Chwyć uchwyt T-Bar.",
                "Przyciągnij uchwyt do klatki piersiowej, zginając łokcie.",
                "Powoli opuść uchwyt do pozycji wyjściowej."
        ));

// BICEPS
        ExerciseInfo barbellCurlInfo = new ExerciseInfo();
        barbellCurlInfo.setVideoId("http://localhost:8080/images/barbellcurl.jpg");
        barbellCurlInfo.setDescription(Arrays.asList(
                "Stań prosto, stopy na szerokość bioder, trzymając sztangę podchwytem.",
                "Utrzymuj łokcie blisko ciała.",
                "Unosź sztangę do przodu, zginając łokcie.",
                "Napnij bicepsy na górze ruchu.",
                "Powoli opuszczaj sztangę do pozycji wyjściowej."
        ));

        ExerciseInfo dumbbellCurlInfo = new ExerciseInfo();
        dumbbellCurlInfo.setVideoId("http://localhost:8080/images/dumbbellcurl.jpg");
        dumbbellCurlInfo.setDescription(Arrays.asList(
                "Stań prosto z hantlami w dłoniach, ramiona wzdłuż ciała.",
                "Utrzymuj łokcie blisko ciała.",
                "Unosź hantle do przodu, zginając łokcie.",
                "Napnij bicepsy na górze ruchu.",
                "Powoli opuszczaj hantle do pozycji wyjściowej."
        ));

        ExerciseInfo concentrationCurlInfo = new ExerciseInfo();
        concentrationCurlInfo.setVideoId("http://localhost:8080/images/concentrationcurl.jpg");
        concentrationCurlInfo.setDescription(Arrays.asList(
                "Usiądź na ławce, nogi rozstawione szeroko.",
                "Oprzyj łokieć prawej ręki o wewnętrzną część prawego uda.",
                "Chwyć hantlę i unosź ją, zginając łokieć.",
                "Napnij biceps na górze ruchu.",
                "Powoli opuszczaj hantlę do pozycji wyjściowej.",
                "Powtórz na drugą rękę."
        ));

        ExerciseInfo hammerCurlInfo = new ExerciseInfo();
        hammerCurlInfo.setVideoId("http://localhost:8080/images/hammercurl.jpg");
        hammerCurlInfo.setDescription(Arrays.asList(
                "Stań prosto z hantlami w dłoniach, uchwyt młotkowy (kciuki skierowane do góry).",
                "Utrzymuj łokcie blisko ciała.",
                "Unosź hantle, zginając łokcie.",
                "Napnij bicepsy na górze ruchu.",
                "Powoli opuszczaj hantle do pozycji wyjściowej."
        ));

        ExerciseInfo preacherCurlInfo = new ExerciseInfo();
        preacherCurlInfo.setVideoId("http://localhost:8080/images/preachercurl.jpg");
        preacherCurlInfo.setDescription(Arrays.asList(
                "Usiądź na maszynie do uginania przedramion na modlitewniku.",
                "Oprzyj ramiona na poduszce, chwytając sztangę podchwytem.",
                "Unosź sztangę, zginając łokcie.",
                "Napnij bicepsy na górze ruchu.",
                "Powoli opuszczaj sztangę do pozycji wyjściowej."
        ));

// TRICEPS
        ExerciseInfo tricepsPushdownInfo = new ExerciseInfo();
        tricepsPushdownInfo.setVideoId("http://localhost:8080/images/tricepspushdown.jpg");
        tricepsPushdownInfo.setDescription(Arrays.asList(
                "Stań przed wyciągiem górnym, chwyć uchwyt nachwytem.",
                "Utrzymuj łokcie blisko ciała.",
                "Prostuj ramiona, pchając uchwyt w dół.",
                "Napnij tricepsy na dole ruchu.",
                "Powoli zginaj łokcie, wracając do pozycji wyjściowej."
        ));

        ExerciseInfo overheadTricepsExtensionInfo = new ExerciseInfo();
        overheadTricepsExtensionInfo.setVideoId("http://localhost:8080/images/overheadtricepsextension.jpg");
        overheadTricepsExtensionInfo.setDescription(Arrays.asList(
                "Stań prosto, trzymając hantlę oburącz nad głową.",
                "Utrzymuj ramiona prosto w górze, łokcie blisko głowy.",
                "Zginaj łokcie, opuszczając hantlę za głowę.",
                "Prostuj ramiona, unosząc hantlę z powrotem nad głowę.",
                "Napnij tricepsy na górze ruchu."
        ));

        ExerciseInfo skullCrusherInfo = new ExerciseInfo();
        skullCrusherInfo.setVideoId("http://localhost:8080/images/skullcrusher.jpg");
        skullCrusherInfo.setDescription(Arrays.asList(
                "Połóż się na ławce, trzymając sztangę prosto nad klatką piersiową.",
                "Utrzymuj ramiona nieruchomo, zginaj łokcie, opuszczając sztangę w kierunku czoła.",
                "Prostuj łokcie, unosząc sztangę z powrotem do pozycji wyjściowej.",
                "Napnij tricepsy na górze ruchu."
        ));

        ExerciseInfo tricepsDipsInfo = new ExerciseInfo();
        tricepsDipsInfo.setVideoId("http://localhost:8080/images/tricepsdips.jpg");
        tricepsDipsInfo.setDescription(Arrays.asList(
                "Usiądź na krawędzi ławki, dłonie po bokach bioder.",
                "Wyprostuj nogi przed sobą.",
                "Przesuń biodra poza ławkę, podpierając się rękami.",
                "Zginaj łokcie, opuszczając ciało w dół.",
                "Prostuj ramiona, unosząc ciało z powrotem do góry."
        ));

        ExerciseInfo closeGripBenchPressInfo = new ExerciseInfo();
        closeGripBenchPressInfo.setVideoId("http://localhost:8080/images/closegripbenchpress.jpg");
        closeGripBenchPressInfo.setDescription(Arrays.asList(
                "Połóż się na ławce, chwyć sztangę wąsko (ok. 15 cm między dłońmi).",
                "Unieś sztangę nad klatką piersiową.",
                "Opuść sztangę do dolnej części klatki piersiowej, zginając łokcie blisko ciała.",
                "Wypchnij sztangę do góry, prostując ramiona."
        ));

// SHOULDERS
        ExerciseInfo shoulderPressInfo = new ExerciseInfo();
        shoulderPressInfo.setVideoId("http://localhost:8080/images/shoulderpress.jpg");
        shoulderPressInfo.setDescription(Arrays.asList(
                "Usiądź na ławce z oparciem, trzymając hantle na wysokości ramion.",
                "Wypchnij hantle do góry, prostując ramiona.",
                "Powoli opuszczaj hantle do pozycji wyjściowej.",
                "Utrzymuj plecy proste i napięte mięśnie brzucha."
        ));

        ExerciseInfo lateralRaiseInfo = new ExerciseInfo();
        lateralRaiseInfo.setVideoId("http://localhost:8080/images/lateralraise.jpg");
        lateralRaiseInfo.setDescription(Arrays.asList(
                "Stań prosto z hantlami w dłoniach, ramiona wzdłuż ciała.",
                "Unosź ramiona na boki, aż będą równoległe do podłoża.",
                "Utrzymuj lekkie zgięcie w łokciach.",
                "Powoli opuszczaj ramiona do pozycji wyjściowej."
        ));

        ExerciseInfo frontRaiseInfo = new ExerciseInfo();
        frontRaiseInfo.setVideoId("http://localhost:8080/images/frontraise.jpg");
        frontRaiseInfo.setDescription(Arrays.asList(
                "Stań prosto z hantlami w dłoniach przed udami.",
                "Unosź jedno ramię do przodu, aż będzie równoległe do podłoża.",
                "Powoli opuszczaj ramię do pozycji wyjściowej.",
                "Powtórz na drugą rękę."
        ));

        ExerciseInfo arnoldPressInfo = new ExerciseInfo();
        arnoldPressInfo.setVideoId("http://localhost:8080/images/arnoldpress.jpg");
        arnoldPressInfo.setDescription(Arrays.asList(
                "Usiądź na ławce z oparciem, trzymając hantle przed klatką piersiową, dłonie skierowane do ciała.",
                "Wypchnij hantle do góry, jednocześnie obracając nadgarstki, aż dłonie będą skierowane na zewnątrz.",
                "Powoli opuszczaj hantle do pozycji wyjściowej, obracając nadgarstki.",
                "Utrzymuj kontrolowany ruch przez cały czas."
        ));

        ExerciseInfo reversePecDeckInfo = new ExerciseInfo();
        reversePecDeckInfo.setVideoId("http://localhost:8080/images/reversepecdeck.jpg");
        reversePecDeckInfo.setDescription(Arrays.asList(
                "Usiądź na maszynie do rozpiętek odwrotnych, twarzą do oparcia.",
                "Chwyć uchwyty, ramiona na wysokości barków.",
                "Rozszerzaj ramiona na boki, ściskając łopatki.",
                "Powoli wracaj do pozycji wyjściowej."
        ));

// ABS
        ExerciseInfo crunchesInfo = new ExerciseInfo();
        crunchesInfo.setVideoId("http://localhost:8080/images/crunches.jpg");
        crunchesInfo.setDescription(Arrays.asList(
                "Połóż się na plecach, kolana ugięte, stopy na podłodze.",
                "Ręce skrzyżowane na klatce piersiowej lub za głową.",
                "Napnij mięśnie brzucha i unieś górną część tułowia.",
                "Powoli opuszczaj tułów do pozycji wyjściowej."
        ));

        ExerciseInfo legRaiseInfo = new ExerciseInfo();
        legRaiseInfo.setVideoId("http://localhost:8080/images/legraise.jpg");
        legRaiseInfo.setDescription(Arrays.asList(
                "Połóż się na plecach, ręce wzdłuż ciała.",
                "Unieś nogi prosto w górę, stopy skierowane do sufitu.",
                "Powoli opuszczaj nogi, nie dotykając podłogi.",
                "Powtórz ruch unoszenia nóg."
        ));

        ExerciseInfo plankInfo = new ExerciseInfo();
        plankInfo.setVideoId("http://localhost:8080/images/plank.jpg");
        plankInfo.setDescription(Arrays.asList(
                "Przyjmij pozycję jak do pompki, opierając się na przedramionach.",
                "Utrzymuj ciało prosto od głowy do pięt.",
                "Napnij mięśnie brzucha i pośladków.",
                "Utrzymaj pozycję przez określony czas."
        ));

        ExerciseInfo russianTwistInfo = new ExerciseInfo();
        russianTwistInfo.setVideoId("http://localhost:8080/images/russiantwist.jpg");
        russianTwistInfo.setDescription(Arrays.asList(
                "Usiądź na podłodze, kolana ugięte, stopy uniesione.",
                "Pochyl tułów lekko do tyłu, utrzymując równowagę.",
                "Trzymaj ręce złączone przed sobą.",
                "Skręcaj tułów w lewo i prawo, dotykając podłogi po obu stronach."
        ));

        ExerciseInfo bicycleCrunchInfo = new ExerciseInfo();
        bicycleCrunchInfo.setVideoId("http://localhost:8080/images/bicyclecrunch.jpg");
        bicycleCrunchInfo.setDescription(Arrays.asList(
                "Połóż się na plecach, ręce za głową.",
                "Unieś nogi zgięte w kolanach pod kątem 90 stopni.",
                "Przyciągnij lewe kolano do prawego łokcia, prostując prawą nogę.",
                "Zmień stronę, przyciągając prawe kolano do lewego łokcia.",
                "Kontynuuj naprzemienny ruch."
        ));


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
                new Exercise(null, "Wiosłowanie T--bar", CategoryName.PLECY, tBarRowInfo),

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
                new Exercise(null, "Rozpiętki w siadzie na maszynie", CategoryName.BARKI, reversePecDeckInfo),

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

        User user = userRepository.findByEmail("kowalski@wp.pl").orElseThrow();
        // Example exercises (assuming these already exist in your DB)
        Long exercise1Id = 1L; // ID for "Squat"
        Long exercise2Id = 2L; // ID for "Bench Press"

        // Create sets for the first exercise session (e.g., Squat)
        ExerciseSetDTO set1 = new ExerciseSetDTO(12, 80.0); // 12 reps, 80 kg
        ExerciseSetDTO set2 = new ExerciseSetDTO(10, 85.0); // 10 reps, 85 kg
        List<ExerciseSetDTO> squatSets = Arrays.asList(set1, set2);

        // Create the first exercise session (Squat)
        ExerciseSessionDTO squatSession = new ExerciseSessionDTO(exercise1Id, "Squat", squatSets);

        // Create sets for the second exercise session (e.g., Bench Press)
        ExerciseSetDTO set3 = new ExerciseSetDTO(10, 60.0); // 10 reps, 60 kg
        ExerciseSetDTO set4 = new ExerciseSetDTO(8, 65.0);  // 8 reps, 65 kg
        List<ExerciseSetDTO> benchPressSets = Arrays.asList(set3, set4);

        // Create the second exercise session (Bench Press)
        ExerciseSessionDTO benchPressSession = new ExerciseSessionDTO(exercise2Id, "Bench press", benchPressSets);

        // Create a workout session with both exercise sessions
        WorkoutSessionDTO workoutSessionDTO = new WorkoutSessionDTO(
                LocalDate.now(),            // Current date
                "Morning workout",          // Note
                Arrays.asList(squatSession, benchPressSession) // List of exercise sessions
        );

        WorkoutSession workoutSession = workoutMapper.mapToEntity(workoutSessionDTO);
        workoutSession.setUser(user);

        workoutSessionRepository.save(workoutSession);
    }


}
