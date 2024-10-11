package com.example.gymrat.database;

import com.example.gymrat.DTO.auth.RegisterRequest;
import com.example.gymrat.repository.UserRepository;
import com.example.gymrat.service.ChatService;
import com.example.gymrat.service.FriendService;
import com.example.gymrat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import com.example.gymrat.model.User;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserService userService;
    private final ChatService chatService;
    private final FriendService friendService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedUsers();
        addFriends();
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



}
