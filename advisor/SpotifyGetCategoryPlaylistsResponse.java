package advisor;

import java.util.List;

public record SpotifyGetCategoryPlaylistsResponse(Playlist playlists) {

    public record Playlist (List<Item> items) {

    }
    public record Item(String name, ExternalUrls external_urls){

    }
    public record ExternalUrls (String spotify) {
    }
}
