package advisor;


import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        String accessValue = null;
        String apiServerPath = null;
        Integer pageSize = null;

        for (int i = 0; i < args.length; i++) {
            if ("-access".equals(args[i]) && i + 1 < args.length) {
                accessValue = args[i + 1];
            }
            if("-resource".equals(args[i]) && i+1 < args.length){
                apiServerPath = args[i+1];
            }
            if("-page".equals(args[i]) && i+1 < args.length){
                pageSize = Integer.parseInt(args[i+1]);
            }
        }

        if (accessValue == null){
            accessValue = "https://accounts.spotify.com";
        }
        if(apiServerPath == null){
            apiServerPath = "https://api.spotify.com";
        }
        if(pageSize == null){
            pageSize = 5;
        }


        SpotifyModel model = new SpotifyModel(accessValue,
                apiServerPath);


        SpotifyController controller = new SpotifyController(accessValue,
                apiServerPath, pageSize, model);
        ConsoleView console = new ConsoleView(controller, model);
        console.start();


    }

}


