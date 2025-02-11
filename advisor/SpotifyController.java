package advisor;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.function.BinaryOperator;

public class SpotifyController {

    SpotifyModel model;
    String path;

    SpotifyGetCategoriesResponse categoriesResponse;
    SpotifyGetNewReleasesResponse newReleasesResponse;


    public SpotifyController(String path) throws IOException, InterruptedException {
        this.path = path;
        this.model = new SpotifyModel(path);
    }

    public void start() throws IOException, InterruptedException {
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
                      newReleasesResponse = model.spotifyGetNewRelease();
                      newReleasesResponse.albums().items().forEach(album -> {
                          System.out.println(album.name());
                          System.out.println(album.artists().stream().map(artist -> artist.name()).toList());
                          System.out.println(album.external_urls().spotify());
                          System.out.println();

                      });
                        break;
                    case "categories":
                       categoriesResponse = model.spotifyGetCategories();
                       categoriesResponse.categories().items().forEach(category -> System.out.println(category.name()));
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
                        System.out.println("use this link to request the access code:");
                        model.startServer();
                        System.out.println(path + "/authorize?client_id=e60acaa8b277447e8e1a382432bfd094&redirect_uri=http://localhost:8080&response_type=code");
                        System.out.println("waiting for code...");
                        while (model.code == null) {
                            Thread.sleep(100);
                        }
                        model.server.stop(1);
                        model.getToken();
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