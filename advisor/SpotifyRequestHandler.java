package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;


public class SpotifyRequestHandler implements HttpHandler {
    String path;
    SpotifyModel model;

    public SpotifyRequestHandler(String path, SpotifyModel model) {
        this.path = path;
        this.model = model;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String responseString = "";
        if (!exchange.getRequestURI().toString().contains("code")) {
            responseString = "Authorization code not found. Try again.";
        } else {
            String code = exchange.getRequestURI().toString().substring(7);
            model.setCode(code);
            System.out.println("code received");
            responseString = "Got the code. Return back to your program.";
        }
        exchange.sendResponseHeaders(200, responseString.length());
        exchange.getResponseBody().write(responseString.getBytes());
        exchange.getResponseBody().close();
    }
}
