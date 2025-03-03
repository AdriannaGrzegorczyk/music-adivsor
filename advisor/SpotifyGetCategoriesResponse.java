package advisor;

import java.util.List;

public record SpotifyGetCategoriesResponse (SpotifyGetCategoriesObject categories) {
    public record SpotifyGetCategoriesObject(String href, Integer limit, String next, Integer offset, String previous,
                                             Integer total, List<Category> items) {
        public record Category(String href, String id, String name) {

        }
    }
}