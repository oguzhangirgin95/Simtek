package modules.firstlevel.business;

import models.extended.firstlevel.request.LoginExtendedRequest;
import models.extended.firstlevel.response.LoginExtendedResponse;

public class TokenBusiness {
    public Boolean Login(LoginExtendedRequest loginRequest) {

        LoginExtendedResponse LoginExtendedResponse = new LoginExtendedResponse(false);

        if(loginRequest != null && loginRequest.username != null && loginRequest.password != null && loginRequest.password != null) {

            LoginExtendedResponse.success =true;

            return LoginExtendedResponse.success;
        }

        return LoginExtendedResponse.success;
    }
}
