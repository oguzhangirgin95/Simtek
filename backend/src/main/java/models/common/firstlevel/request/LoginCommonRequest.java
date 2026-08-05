package models.common.firstlevel.request;

public class LoginCommonRequest {
    public String username;

    public String password;

    public LoginCommonRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
