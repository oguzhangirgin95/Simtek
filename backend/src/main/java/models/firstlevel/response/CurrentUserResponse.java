package models.firstlevel.response;

import java.util.ArrayList;
import java.util.List;

public class CurrentUserResponse {

    public Boolean valid;

    public String username;

    public String token;

    public List<String> features = new ArrayList<>();

    public CurrentUserResponse() {
    }
}
