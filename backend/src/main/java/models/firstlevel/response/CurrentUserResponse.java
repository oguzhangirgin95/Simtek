package models.firstlevel.response;

public class CurrentUserResponse {

    /** token gecerli mi; false ise frontend login ekranina donmeli */
    public Boolean valid;

    public String username;

    public String token;

    public CurrentUserResponse() {
    }
}
