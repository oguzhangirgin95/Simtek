package models.firstlevel.response;

public class LoginResponse {
    
    public Boolean success;

    public String token;

    public LoginResponse(Boolean success) {
        this.success = success;
    }
}
