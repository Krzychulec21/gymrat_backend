package com.example.gymrat.database;

import com.example.gymrat.DTO.auth.RegisterRequest;
import com.example.gymrat.model.User;
import com.example.gymrat.repository.UserRepository;
import com.example.gymrat.service.ChatService;
import com.example.gymrat.service.FriendService;
import com.example.gymrat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

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
        RegisterRequest user1 = new RegisterRequest("Jan", "Kowalski", "kowalczyk", "kowalski@wp.pl", "password");
        RegisterRequest user2 = new RegisterRequest("Michal", "Barylka", "essunia", "nowak@wp.pl", "password");
        RegisterRequest user3 = new RegisterRequest("John", "Doe", "johndoe", "johndoe@xample.com", "password");
        RegisterRequest user4 = new RegisterRequest("Anna", "Nowak", "annaN", "anna@xample.com", "password");
        RegisterRequest user5 = new RegisterRequest("Ewa", "Zalewska", "ewaz", "ewa@wp.pl", "password");
        RegisterRequest user6 = new RegisterRequest("Adam", "Malinowski", "adamM", "adam@xample.com", "password");
        RegisterRequest user7 = new RegisterRequest("Paweł", "Piotrowski", "pawelP", "pawel@xample.com", "password");
        RegisterRequest user8 = new RegisterRequest("Katarzyna", "Kwiatkowska", "katK", "katarzyna@wp.pl", "password");
        RegisterRequest user9 = new RegisterRequest("Tomasz", "Jankowski", "tomJ", "tomasz@xample.com", "password");
        RegisterRequest user10 = new RegisterRequest("Aleksandra", "Zawisza", "aleksandraZ", "aleksandra@xample.com", "password");
        RegisterRequest user11 = new RegisterRequest("Piotr", "Grabowski", "piotrG", "piotr@xample.com", "password");
        RegisterRequest user12 = new RegisterRequest("Monika", "Szymanska", "monikaS", "monika@xample.com", "password");
        RegisterRequest user13 = new RegisterRequest("Wojciech", "Mazur", "wojciechM", "wojciech@xample.com", "password");
        RegisterRequest user14 = new RegisterRequest("Marek", "Pietrzak", "marekP", "marek@xample.com", "password");
        RegisterRequest user15 = new RegisterRequest("Magdalena", "Olszewska", "magdaO", "magda@xample.com", "password");
        RegisterRequest user16 = new RegisterRequest("Dariusz", "Zych", "darekZ", "dariusz@xample.com", "password");
        RegisterRequest user17 = new RegisterRequest("Agata", "Lewandowska", "agataL", "agata@xample.com", "password");
        RegisterRequest user18 = new RegisterRequest("Damian", "Sikorski", "damianS", "damian@xample.com", "password");
        RegisterRequest user19 = new RegisterRequest("Łukasz", "Kamiński", "lukaszK", "lukasz@xample.com", "password");
        RegisterRequest user20 = new RegisterRequest("Zuzanna", "Wróbel", "zuzannaW", "zuzanna@xample.com", "password");
        RegisterRequest user21 = new RegisterRequest("Kacper", "Czerwiński", "kacperC", "kacper@xample.com", "password");
        RegisterRequest user22 = new RegisterRequest("Marta", "Chmielewska", "martaC", "marta@xample.com", "password");
        RegisterRequest user23 = new RegisterRequest("Rafał", "Wysocki", "rafalW", "rafal@xample.com", "password");
        RegisterRequest user24 = new RegisterRequest("Justyna", "Sikora", "justynaS", "justyna@xample.com", "password");
        RegisterRequest user25 = new RegisterRequest("Grzegorz", "Górski", "gregG", "grzegorz@xample.com", "password");
        RegisterRequest user26 = new RegisterRequest("Karolina", "Michalska", "karolinaM", "karolina@xample.com", "password");
        RegisterRequest user27 = new RegisterRequest("Patryk", "Sadowski", "patrykS", "patryk@xample.com", "password");
        RegisterRequest user28 = new RegisterRequest("Izabela", "Wiśniewska", "izabelaW", "izabela@xample.com", "password");
        RegisterRequest user29 = new RegisterRequest("Artur", "Głowacki", "arturG", "artur@xample.com", "password");
        RegisterRequest user30 = new RegisterRequest("Joanna", "Kozłowska", "joannaK", "joanna@xample.com", "password");
        RegisterRequest user31 = new RegisterRequest("Krystian", "Sowa", "krystianS", "krystian@xample.com", "password");
        RegisterRequest user32 = new RegisterRequest("Natalia", "Wesołowska", "nataliaW", "natalia@xample.com", "password");
        RegisterRequest user33 = new RegisterRequest("Maciej", "Stasiak", "maciejS", "maciej@xample.com", "password");
        RegisterRequest user34 = new RegisterRequest("Oliwia", "Zając", "oliwiaZ", "oliwia@xample.com", "password");
        RegisterRequest user35 = new RegisterRequest("Dominik", "Król", "dominikK", "dominik@xample.com", "password");

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
        User user1 = userRepository.findByEmail("kowalski@wp.pl").orElseThrow();

        String[] userEmails = {
                "nowak@wp.pl",
                "johndoe@xample.com",
                "anna@xample.com",
                "ewa@wp.pl",
                "adam@xample.com",
                "pawel@xample.com",
                "katarzyna@wp.pl",
                "tomasz@xample.com",
                "aleksandra@xample.com",
                "piotr@xample.com",
                "monika@xample.com",
                "wojciech@xample.com",
                "marek@xample.com",
                "magda@xample.com",
                "dariusz@xample.com",
                "agata@xample.com",
                "damian@xample.com",
                "lukasz@xample.com",
                "zuzanna@xample.com",
                "kacper@xample.com",
                "marta@xample.com",
                "rafal@xample.com",
                "justyna@xample.com",
                "grzegorz@xample.com",
                "karolina@xample.com",
                "patryk@xample.com",
                "izabela@xample.com",
                "artur@xample.com",
                "joanna@xample.com",
                "krystian@xample.com",
                "natalia@xample.com",
                "maciej@xample.com",
                "oliwia@xample.com",
                "dominik@xample.com"
        };

        for (int i = 0; i < userEmails.length; i++) {
            String email = userEmails[i];
            User otherUser = userRepository.findByEmail(email).orElseThrow();

            friendService.sendFriendRequest(user1.getEmail(), otherUser.getEmail());

            friendService.respondToFriendRequest((long) (i + 1), true);
        }
    }


}
