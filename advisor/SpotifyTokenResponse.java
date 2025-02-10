package advisor;

public record SpotifyTokenResponse(String access_token, String token_type, int expires_in, String refresh_token ) {
}
