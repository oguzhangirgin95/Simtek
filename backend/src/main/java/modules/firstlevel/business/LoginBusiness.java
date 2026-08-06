package modules.firstlevel.business;

import models.firstlevel.request.LoginRequest;
import models.firstlevel.response.LoginResponse;

public class LoginBusiness {
    public Boolean Login(LoginRequest loginRequest) {

        LoginResponse loginResponse = new LoginResponse(false);

        if(loginRequest != null && loginRequest.username != null && loginRequest.password != null && loginRequest.password != null) {

            loginResponse.success =true;

            return loginResponse.success;
        }

        return loginResponse.success;
    }
}
