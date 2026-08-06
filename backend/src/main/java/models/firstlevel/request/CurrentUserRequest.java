package models.firstlevel.request;

public class CurrentUserRequest {
    public String token;

    public CurrentUserRequest() {
    }

    public CurrentUserRequest(String token) {
        this.token = token;
    }
}
