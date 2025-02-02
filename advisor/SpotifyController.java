package advisor;

import java.util.Scanner;
import java.util.function.BinaryOperator;

public class SpotifyController {


    public SpotifyController() {
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        boolean isAuth = false;

        while (running) {
            String userInput = scanner.nextLine();
            if (!isAuth && !userInput.equals("auth") && !userInput.equals("exit")) {
                System.out.println("Please, provide access for application.");
            } else {
                switch (userInput) {
                    case "featured":
                        System.out.println("---FEATURED---\n" +
                                "Mellow Morning\n" +
                                "Wake Up and Smell the Coffee\n" +
                                "Monday Motivation\n" +
                                "Songs to Sing in the Shower");
                        break;
                    case "new":
                        System.out.println("---NEW RELEASES---\n" +
                                "Mountains [Sia, Diplo, Labrinth]\n" +
                                "Runaway [Lil Peep]\n" +
                                "The Greatest Show [Panic! At The Disco]\n" +
                                "All Out Life [Slipknot]");
                        break;
                    case "categories":
                        System.out.println("---CATEGORIES---\n" +
                                "Top Lists\n" +
                                "Pop\n" +
                                "Mood\n" +
                                "Latin");
                        break;
                    case "playlists Mood":
                        System.out.println("---MOOD PLAYLISTS---\n" +
                                "Walk Like A Badass  \n" +
                                "Rage Beats  \n" +
                                "Arab Mood Booster  \n" +
                                "Sunday Stroll");
                        break;
                    case "playlists Latin":
                        System.out.println("---LATIN PLAYLISTS---\n" +
                                "Walk Like A Badass  \n" +
                                "Rage Beats  \n" +
                                "Arab Mood Booster  \n" +
                                "Sunday Stroll");
                        break;
                    case "playlists Pop":
                        System.out.println("---POP PLAYLISTS---\n" +
                                "Walk Like A Badass  \n" +
                                "Rage Beats  \n" +
                                "Arab Mood Booster  \n" +
                                "Sunday Stroll");
                        break;
                    case "playlists Top Lists":
                        System.out.println("---TOP LISTS PLAYLISTS---\n" +
                                "Walk Like A Badass  \n" +
                                "Rage Beats  \n" +
                                "Arab Mood Booster  \n" +
                                "Sunday Stroll");
                        break;
                    case "auth":
                        System.out.println("https://accounts.spotify.com/authorize?client_id=e60acaa8b277447e8e1a382432bfd094&redirect_uri=localhost:8080&response_type=code");
                        System.out.println("---SUCCESS---");
                        isAuth = true;
                        break;
                    case "exit":
                        running = false;
                        System.out.println("---GOODBYE!---");
                        break;
                }
            }
        }
    }


}