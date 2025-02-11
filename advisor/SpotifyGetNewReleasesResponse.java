package advisor;

import java.util.List;

public record SpotifyGetNewReleasesResponse(SpotifyGetNewReleaseObjectItems albums) {
    public record SpotifyGetNewReleaseObjectItems(String href, Integer limit, String next, Integer offset,
                                                  String previous, Integer total, List<Album> items) {
        public record Album(String name, List<Artist> artists, ExternalUrls external_urls) {
            public record Artist(String name) {
            }
            public record ExternalUrls(String spotify) {

            }
        }
    }


}

//nazwa albumu, artysta, link do alnumu