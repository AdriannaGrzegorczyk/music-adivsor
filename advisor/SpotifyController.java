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

        String clientSecret = "014a4983329d433a9115692556797be5";
        String clientId = "e60acaa8b277447e8e1a382432bfd094";
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()))
                .uri(URI.create(tokenPath + "/api/token?grant_type=authorization_code&code=" + model.getCode() + "&redirect_uri=http://localhost:8080"))
                //.uri(URI.create(tokenPath + "/api/token?grant_type=authorization_code&code=" + model.getCode()))
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

    public SpotifyGetCategoriesResponse spotifyGetCategories() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + model.getToken().access_token())
                .uri(URI.create(resourcePath + "/v1/browse/categories?limit="+this.pageSize))
                .GET()
                .build();
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpResponse<?> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        SpotifyGetCategoriesResponse responseCategories = new Gson().fromJson((String) response.body(), SpotifyGetCategoriesResponse.class);
        model.setCategoriesResponse(responseCategories);
        return responseCategories;
    }

    public SpotifyGetCategoriesResponse spotifyGetCategories(String paginatedLink) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + model.getToken().access_token())
                .uri(URI.create(paginatedLink))
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
        model.getCategoriesResponse().categories().items().forEach(category -> System.out.println(category.name()));

        int totalNumberOfItems = model.getCategoriesResponse().categories().total();
        int totalPages = totalNumberOfItems % this.pageSize == 0 ? totalNumberOfItems / this.pageSize : (totalNumberOfItems) / this.pageSize + 1;
        System.out.println("---PAGE " + ((model.getCategoriesResponse().categories().offset() / this.pageSize) + 1) + " OF " +  totalPages + "---");

        while(true) {
            String input = new Scanner(System.in).nextLine();
            if (input.equals("next")) {
                if (model.getCategoriesResponse().categories().next() == null) {
                    System.out.println("No more pages.");
                } else {
                    spotifyGetCategories(model.getCategoriesResponse().categories().next());
                    model.getCategoriesResponse().categories().items().forEach(category -> System.out.println(category.name()));
                    System.out.println("---PAGE " + ((model.getCategoriesResponse().categories().offset() / this.pageSize) + 1) + " OF " + totalPages + "---");
                }
            } else if (input.equals("prev")) {
                if (model.getCategoriesResponse().categories().previous() == null) {
                    System.out.println("No more pages.");
                } else {
                    spotifyGetCategories(model.getCategoriesResponse().categories().previous());
                    model.getCategoriesResponse().categories().items().forEach(category -> System.out.println(category.name()));
                    System.out.println("---PAGE " + ((model.getCategoriesResponse().categories().offset() / this.pageSize) + 1) + " OF " + totalPages + "---");
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
       // return responseNewRelease;

    }

    public SpotifyGetCategoryPlaylistsResponse spotifyGetCategoryPlaylistsResponse(String categoryId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + model.getToken().access_token())
                .uri(URI.create(resourcePath + "/v1/browse/categories/" + categoryId + "/playlists"))
                .GET()
                .build();
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpResponse<?> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        SpotifyGetCategoryPlaylistsResponse playlistsResponse = new Gson().fromJson((String) response.body(), SpotifyGetCategoryPlaylistsResponse.class);
        model.setSpotifyGetCategoryPlaylistsResponse(playlistsResponse);
        return playlistsResponse;
    }



}
