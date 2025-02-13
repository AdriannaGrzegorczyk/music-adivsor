package advisor;

import java.util.List;

    public record SpotifyGetFeaturedResponse(SpotifyGetFeaturedResponsePlaylists playlists) {

        public record SpotifyGetFeaturedResponsePlaylists (List<SpotifyGetFeaturedResponsePlaylist> items, Owner owner) {
        }

        public record SpotifyGetFeaturedResponsePlaylist (String name, ExternalUrls external_urls) {
        }

        public record Owner (ExternalUrls external_urls ) {
        }


        public record ExternalUrls (String spotify) {
        }

    }