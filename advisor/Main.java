package advisor;


import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        SpotifyController controller = new SpotifyController(args.length == 2 ? args[1] : "https://accounts.spotify.com");
        controller.start();


    }

}


