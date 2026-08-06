package models.firstlevel.response;

public class LogoutResponse {

    public Boolean success;

    public String message;

    public LogoutResponse() {
    }

    public LogoutResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
