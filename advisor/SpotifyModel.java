package advisor;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SpotifyModel {


    private String tokenPath;
    private String resourcePath;
    private SpotifyTokenResponse token;
    private String code;
    private SpotifyGetFeaturedResponse featuredResponse;
    private SpotifyGetCategoriesResponse categoriesResponse;
    private SpotifyGetCategoryPlaylistsResponse spotifyGetCategoryPlaylistsResponse;
    private SpotifyGetNewReleasesResponse newReleasesResponse;
    private SpotifyGetCategoryPlaylistsResponse categoryPlaylistsResponse;


    public SpotifyModel(String tokenPath, String resourcePath) throws IOException {
        this.tokenPath = tokenPath;
        this.resourcePath = resourcePath;
    }


    public SpotifyGetNewReleasesResponse getNewReleasesResponse() {
        return newReleasesResponse;
    }

    public void setNewReleasesResponse(SpotifyGetNewReleasesResponse newReleasesResponse) {
        this.newReleasesResponse = newReleasesResponse;
    }

    public SpotifyGetCategoryPlaylistsResponse getCategoryPlaylistsResponse() {
        return categoryPlaylistsResponse;
    }

    public void setCategoryPlaylistsResponse(SpotifyGetCategoryPlaylistsResponse categoryPlaylistsResponse) {
        this.categoryPlaylistsResponse = categoryPlaylistsResponse;
    }


    public SpotifyGetCategoryPlaylistsResponse getSpotifyGetCategoryPlaylistsResponse() {
        return spotifyGetCategoryPlaylistsResponse;
    }

    public void setSpotifyGetCategoryPlaylistsResponse(SpotifyGetCategoryPlaylistsResponse spotifyGetCategoryPlaylistsResponse) {
        this.spotifyGetCategoryPlaylistsResponse = spotifyGetCategoryPlaylistsResponse;
    }

    public SpotifyGetFeaturedResponse getFeaturedResponse() {
        return featuredResponse;
    }

    public void setFeaturedResponse(SpotifyGetFeaturedResponse featuredResponse) {
        this.featuredResponse = featuredResponse;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTokenPath() {
        return tokenPath;
    }

    public String getResourcePath() {
        return resourcePath;
    }


    public String getCode() {
        return code;
    }

    public void setToken(SpotifyTokenResponse token) {
        this.token = token;
    }

    public SpotifyTokenResponse getToken() {
        return token;
    }


    public SpotifyGetCategoriesResponse getCategoriesResponse() {
        return categoriesResponse;
    }

    public void setCategoriesResponse(SpotifyGetCategoriesResponse categoriesResponse) {
        this.categoriesResponse = categoriesResponse;
    }

}
