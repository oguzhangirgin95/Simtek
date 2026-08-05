package models.extended.firstlevel.request;

import models.common.firstlevel.request.LoginCommonRequest;

public class LoginExtendedRequest extends LoginCommonRequest {
    
    public LoginExtendedRequest(String username, String password) {
        super(username, password);
    }
}
