package dto;

public class AuthData {
    public final String token;
    public final String username;
    public final long expiresAt;

    public AuthData(String token, String username, long expiresAt) {
        this.token = token;
        this.username = username;
        this.expiresAt = expiresAt;
    }
}
