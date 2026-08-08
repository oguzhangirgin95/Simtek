package models.firstlevel.response;

import java.util.ArrayList;
import java.util.List;

public class LoginResponse {

    public Boolean success;

    public String token;

    public List<String> features = new ArrayList<>();

    public LoginResponse(Boolean success) {
        this.success = success;
    }
}
