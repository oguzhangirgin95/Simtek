package models.firstlevel.request;

public class LogoutRequest {
    public String token;

    public LogoutRequest() {
    }

    public LogoutRequest(String token) {
        this.token = token;
    }
}
