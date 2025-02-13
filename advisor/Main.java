package advisor;


import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        SpotifyController controller = new SpotifyController(args.length == 4 ? args[1] : "https://accounts.spotify.com",
                args.length == 4 ? args[3] : "https://api.spotify.com");
        controller.start();


    }

}


