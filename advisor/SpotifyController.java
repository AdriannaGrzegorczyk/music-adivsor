package advisor;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class SpotifyController {

    SpotifyModel model;
    String tokenPath;
    String resourcePath;

    SpotifyGetCategoriesResponse categoriesResponse;
    SpotifyGetNewReleasesResponse newReleasesResponse;

    SpotifyGetFeaturedResponse featuredResponse;
    SpotifyGetCategoryPlaylistsResponse categoryPlaylistsResponse;


    public SpotifyController(String tokenPath, String resourcePath) throws IOException, InterruptedException {
        this.tokenPath = tokenPath;
        this.resourcePath = resourcePath;
        this.model = new SpotifyModel(tokenPath, resourcePath);
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
                        featuredResponse = model.spotifyGetFeaturedResponse();
                        featuredResponse.playlists().items().forEach(playlist -> {

                            System.out.println(playlist.name());
                            System.out.println(playlist.external_urls().spotify());
                        });
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
                    case "auth":
                        System.out.println("use this link to request the access code:");
                        model.startServer();
                        System.out.println(tokenPath + "/authorize?client_id=e60acaa8b277447e8e1a382432bfd094&redirect_uri=http://localhost:8080&response_type=code");
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

                //TODO FIX PRINTING
                if (userInput.startsWith("playlists")) {
                    String categoryName = userInput.substring(10);
                    if (categoriesResponse == null) {
                        categoriesResponse = model.spotifyGetCategories();
                    }
                    Optional<SpotifyGetCategoriesResponse.SpotifyGetCategoriesObject.Category> optional =
                            categoriesResponse.categories().items()
                                    .stream()
                                    .filter(category -> category.name().equalsIgnoreCase(categoryName))
                                    .findFirst();

                    if (!optional.isPresent()) {
                        System.out.println("Unknown category name.");
                    } else {
                        categoryPlaylistsResponse = model.spotifyGetCategoryPlaylistsResponse(optional.get().id());
                        if (categoryPlaylistsResponse.playlists() == null) {
                            System.out.println("Specified id doesn't exist");
                        } else {
                            categoryPlaylistsResponse.playlists().items().forEach(item -> {
                                System.out.println(item.name());
                                System.out.println(item.external_urls().spotify());
                                System.out.println();
                            });
                        }
                    }
                }
            }
        }

    }
}