package advisor;

import java.awt.print.PageFormat;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleView {
    SpotifyModel model;
    SpotifyController controller;

    boolean isAuth = false;
    boolean running = true;

    public ConsoleView(SpotifyController controller, SpotifyModel model) {
        this.controller = controller;
        this.model = model;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public void start() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while (this.running) {
            String userInput = scanner.nextLine();
            if (!this.isAuth && !userInput.equals("auth") && !userInput.equals("exit")) {
                System.out.println("Please, provide access for application.");
            } else {
                manageActions(userInput);

            }
        }
    }

    public void manageActions(String userInput) throws IOException, InterruptedException {

        if (userInput.equals("auth")) {
            setAuth(true);
            System.out.println("use this link to request the access code:");
            controller.startServer();
            System.out.println(model.getTokenPath() + "/authorize?client_id=e60acaa8b277447e8e1a382432bfd094&redirect_uri=http://localhost:8080&response_type=code");
            System.out.println("waiting for code...");
            while (model.getCode() == null) {
                Thread.sleep(100);
            }
            controller.server.stop(1);
            controller.getToken();
        } else if (userInput.equals("featured")) {
            controller.getFeaturedResponsePaginated();
        } else if (userInput.equals("categories")) {
            controller.getCategoriesPaginated();
        } else if (userInput.equals("new")) {
            controller.getNewReleasePaginated();
        } else if (userInput.equals("exit")) {
            this.running = false;
            System.out.println("---GOODBYE!---");

        } else if (userInput.startsWith("playlists")) {
            String categoryName = userInput.substring(10);
            if (model.getCategoriesResponse() == null) {
                controller.spotifyGetCategories();
            }
            Optional<SpotifyGetCategoriesResponse.SpotifyGetCategoriesObject.Category> optional =
                    model.getCategoriesResponse().categories().items()
                            .stream()
                            .filter(category -> category.name().equalsIgnoreCase(categoryName))
                            .findFirst();
            if (!optional.isPresent()) {
                System.out.println("Unknown category name.");
            } else {
                    controller.getCategoriesPlaylistsResponsePaginated(optional.get().id());
                }
            }
        }
    }
