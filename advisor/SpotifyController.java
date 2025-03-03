package advisor;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class SpotifyController {

    SpotifyModel model;
    String tokenPath;
    String resourcePath;
    HttpServer server;
    Integer pageSize;
    SpotifyRequestHandler handler;


    public SpotifyController(String tokenPath, String resourcePath, Integer pageSize, SpotifyModel model) {
        this.tokenPath = tokenPath;
        this.resourcePath = resourcePath;
        this.model = model;
        this.pageSize = pageSize;
        this.handler = new SpotifyRequestHandler(tokenPath, model);
    }


    public void startServer() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(8080), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(handler);
        this.server.start();
    }

    public void getToken() throws IOException, InterruptedException {

        String clientSecret = "abc";
        String clientId = "abc";
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()))
               //.uri(URI.create(tokenPath + "/api/token?grant_type=authorization_code&code=" + model.getCode() + "&redirect_uri=http://localhost:8080"))
               .uri(URI.create(tokenPath + "/api/token?grant_type=authorization_code&code=" + model.getCode()))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpClient httpClient = HttpClient.newBuilder().build();
        System.out.println("making http request for access_token...");
        HttpResponse<?> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("response:");
        System.out.println(response.body());
        System.out.println("---SUCCESS---");
        model.setToken(new Gson().fromJson((String) response.body(), SpotifyTokenResponse.class));
    }


    public SpotifyGetFeaturedResponse spotifyGetFeaturedResponse() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + model.getToken().access_token())
                .uri(URI.create(resourcePath + "/v1/browse/featured-playlists"))
                .GET()
                .build();
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpResponse<?> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        SpotifyGetFeaturedResponse responseFeatured = new Gson().fromJson((String) response.body(), SpotifyGetFeaturedResponse.class);
        model.setFeaturedResponse(responseFeatured);
        return responseFeatured;
    }

    public void getFeaturedResponsePaginated () throws IOException, InterruptedException {
        spotifyGetFeaturedResponse();
        int currentPage = 1;
        int totalNumberOfItems = model.getFeaturedResponse().playlists().items().size();
        int totalPages = totalNumberOfItems % this.pageSize == 0 ? totalNumberOfItems / this.pageSize : (totalNumberOfItems) / this.pageSize + 1;
        int toIndex = Math.min(currentPage * pageSize, totalNumberOfItems);

        model.getFeaturedResponse().playlists().items().subList(0, toIndex).forEach(playlist -> {
            System.out.println(playlist.name());
            System.out.println(playlist.external_urls().spotify());
        });

        System.out.println("---PAGE " + currentPage + " OF " + totalPages + "---");

        while (true) {
            String input = new Scanner(System.in).nextLine();
            if (input.equals("next")) {
                if (currentPage == totalPages) {
                    System.out.println("No more pages.");
                } else {
                    currentPage++;
                    toIndex = Math.min(currentPage * pageSize, totalNumberOfItems);
                    model.getFeaturedResponse().playlists().items()
                            .subList((currentPage - 1) * this.pageSize, toIndex)
                            .forEach(album -> {
                                System.out.println(album.name());
                                System.out.println(album.external_urls().spotify());
                            });
                    System.out.println("---PAGE " + currentPage + " OF " + totalPages + "---");
                }
            } else if (input.equals("prev")) {
                if (currentPage == 1) {
                    System.out.println("No more pages.");
                } else {
                    currentPage--;
                    model.getFeaturedResponse().playlists().items()
                            .subList((currentPage - 1) * this.pageSize, (currentPage) * this.pageSize)
                            .forEach(album -> {
                                System.out.println(album.name());
                                System.out.println(album.external_urls().spotify());
                            });
                    System.out.println("---PAGE " + currentPage + " OF " + totalPages + "---");
                }
            } else {
                break;
            }
        }
    }
    public SpotifyGetCategoriesResponse spotifyGetCategories() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + model.getToken().access_token())
                .uri(URI.create(resourcePath + "/v1/browse/categories"))
                .GET()
                .build();
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpResponse<?> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        SpotifyGetCategoriesResponse responseCategories = new Gson().fromJson((String) response.body(), SpotifyGetCategoriesResponse.class);
        model.setCategoriesResponse(responseCategories);
        return responseCategories;
    }


    public void getCategoriesPaginated() throws IOException, InterruptedException {

        spotifyGetCategories();
        int currentPage=1;
        int totalNumberOfItems = model.getCategoriesResponse().categories().items().size();
        int totalPages = totalNumberOfItems % this.pageSize == 0 ? totalNumberOfItems / this.pageSize : (totalNumberOfItems) / this.pageSize + 1;
        int toIndex = Math.min(currentPage * pageSize, totalNumberOfItems);
        model.getCategoriesResponse().categories().items().subList(0, toIndex).forEach(category -> System.out.println(category.name()));

        System.out.println("---PAGE " + (currentPage) + " OF " + totalPages + "---");

        while (true) {
            String input = new Scanner(System.in).nextLine();
            if (input.equals("next")) {
                if (currentPage==totalPages) {
                    System.out.println("No more pages.");
                } else {
                    currentPage++;
                    toIndex = Math.min(currentPage * pageSize, totalNumberOfItems);
                    model.getCategoriesResponse().categories().items().subList((currentPage-1)*this.pageSize, toIndex).forEach(category -> System.out.println(category.name()));
                    System.out.println("---PAGE " + (currentPage) + " OF " + totalPages + "---");
                }
            } else if (input.equals("prev")) {
                if (currentPage==1) {
                    System.out.println("No more pages.");
                } else {
                    currentPage--;
                    model.getCategoriesResponse().categories().items().subList((currentPage-1)*this.pageSize, (currentPage)*this.pageSize).forEach(category -> System.out.println(category.name()));
                    System.out.println("---PAGE " + (currentPage) + " OF " + totalPages + "---");
                }
            } else {
                break;
            }
        }
    }

    public void spotifyGetNewRelease() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + model.getToken().access_token())
                .uri(URI.create(resourcePath + "/v1/browse/new-releases"))
                .GET()
                .build();
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpResponse<?> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        SpotifyGetNewReleasesResponse responseNewRelease = new Gson().fromJson((String) response.body(), SpotifyGetNewReleasesResponse.class);
        model.setNewReleasesResponse(responseNewRelease);
    }

    public void getNewReleasePaginated () throws IOException, InterruptedException {

        spotifyGetNewRelease();
        int currentPage=1;
        int totalNumberOfItems = model.getNewReleasesResponse().albums().items().size();
        int totalPages = totalNumberOfItems % this.pageSize == 0 ? (totalNumberOfItems / this.pageSize) : ((totalNumberOfItems) / this.pageSize + 1);
        int toIndex = Math.min(currentPage * pageSize, totalNumberOfItems);

        model.getNewReleasesResponse().albums().items().subList(0, toIndex).forEach(album ->{
        System.out.println(album.name());
        System.out.println(album.artists().stream().map(artist -> artist.name()).toList());
        System.out.println(album.external_urls().spotify());
        System.out.println();
        });
        System.out.println("---PAGE " + currentPage + " OF " + totalPages +"---");

        while (true) {
            String input = new Scanner(System.in).nextLine();
            if (input.equals("next")){
                if(currentPage == totalPages){
                    System.out.println("No more pages.");
                } else {
                    currentPage++;
                    toIndex = Math.min(currentPage * pageSize, totalNumberOfItems);
                    model.getNewReleasesResponse().albums().items()
                            .subList((currentPage-1)*this.pageSize, toIndex)
                            .forEach(album -> {
                                System.out.println(album.name());
                                System.out.println(album.artists().stream().map(artist -> artist.name()).toList());
                                System.out.println(album.external_urls().spotify());
                                System.out.println();
                            });
                    System.out.println("---PAGE " + currentPage + " OF " + totalPages +"---");
                }
            }else if (input.equals("prev")){
                if (currentPage==1){
                    System.out.println("No more pages.");
                } else {
                    currentPage--;
                    model.getNewReleasesResponse().albums().items()
                            .subList((currentPage-1)*this.pageSize, (currentPage)*this.pageSize)
                            .forEach(album -> {
                                System.out.println(album.name());
                                System.out.println(album.artists().stream().map(artist -> artist.name()).toList());
                                System.out.println(album.external_urls().spotify());
                                System.out.println();
                            });
                    System.out.println("---PAGE " + currentPage + " OF " + totalPages +"---");
                }
            }else {
                break;
            }
        }
    }

    public SpotifyGetCategoryPlaylistsResponse spotifyGetCategoryPlaylistsResponse (String categoryId) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + model.getToken().access_token())
                .uri(URI.create(resourcePath + "/v1/browse/categories/" + categoryId + "/playlists"))
                .GET()
                .build();
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpResponse<?> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        SpotifyGetCategoryPlaylistsResponse playlistsResponse = new Gson().fromJson((String) response.body(), SpotifyGetCategoryPlaylistsResponse.class);
        model.setCategoryPlaylistsResponse(playlistsResponse);
        return playlistsResponse;
    }

    public void getCategoriesPlaylistsResponsePaginated (String categoryId) throws IOException, InterruptedException {

        spotifyGetCategoryPlaylistsResponse(categoryId);

        if (model.getCategoryPlaylistsResponse()== null || model.getCategoryPlaylistsResponse().playlists() == null) {
            System.out.println("Specified id doesn't exist");
            return;
        }

        int currentPage=1;
        int totalNumberOfItems = model.getCategoryPlaylistsResponse().playlists().items().size();
        int totalPages = totalNumberOfItems % this.pageSize == 0 ? (totalNumberOfItems / this.pageSize) : ((totalNumberOfItems) / this.pageSize + 1);
        int toIndex = Math.min(currentPage * pageSize, totalNumberOfItems);

        model.getCategoryPlaylistsResponse().playlists().items().subList(0,toIndex).forEach(item -> {
            System.out.println(item.name());
            System.out.println(item.external_urls().spotify());
            System.out.println();
        });

        System.out.println("---PAGE " + currentPage + " OF " + totalPages +"---");
        while (true) {
            String input = new Scanner(System.in).nextLine();
            if (input.equals("next")){
                if(currentPage == totalPages){
                    System.out.println("No more pages.");
                } else {
                    currentPage++;
                    toIndex = Math.min(currentPage * pageSize, totalNumberOfItems);
                    model.getCategoryPlaylistsResponse().playlists().items()
                            .subList((currentPage-1)*this.pageSize, toIndex)
                            .forEach(item -> {
                        System.out.println(item.name());
                        System.out.println(item.external_urls().spotify());
                        System.out.println();
                    });
                    System.out.println("---PAGE " + currentPage + " OF " + totalPages +"---");
                }
            }else if (input.equals("prev")){
                if (currentPage==1){
                    System.out.println("No more pages.");
                } else {
                    currentPage--;
                    model.getCategoryPlaylistsResponse().playlists().items()
                            .subList((currentPage-1)*this.pageSize, (currentPage)*this.pageSize)
                            .forEach(item -> {
                                System.out.println(item.name());
                                System.out.println(item.external_urls().spotify());
                                System.out.println();
                            });
                    System.out.println("---PAGE " + currentPage + " OF " + totalPages +"---");
                }
            }else {
                break;
            }
        }

    }

}
