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


public class SpotifyModel{

    String path;
    SpotifyRequestHandler handler;
    HttpServer server;

    String code;

    public SpotifyModel(String path) throws IOException {
        this.path = path;
        this.handler = new SpotifyRequestHandler(path, this);
    }


    public void startServer() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(8080), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(handler);
        this.server.start();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SpotifyTokenResponse getToken() throws IOException, InterruptedException {
        String clientSecret = "014a4983329d433a9115692556797be5";
        String clientId = "e60acaa8b277447e8e1a382432bfd094";
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()))
                .uri(URI.create(path + "/api/token?grant_type=authorization_code&code=" + code))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpClient httpClient = HttpClient.newBuilder().build();
        System.out.println("making http request for access_token...");
        HttpResponse<?> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("response:");
        System.out.println(response.body());
        System.out.println("---SUCCESS---");
        return new Gson().fromJson((String) response.body(), SpotifyTokenResponse.class);
    }

}