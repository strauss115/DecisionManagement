package at.jku.se.decisiondocu.restclient;

/**
 * Created by martin on 07.12.15.
 */
public class TokenResponse {
    private String access_token;
    private int expires_in;

    // needed
    public TokenResponse() {}

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "TokenResponse{" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                '}';
    }
}
