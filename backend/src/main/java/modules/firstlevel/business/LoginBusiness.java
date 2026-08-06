package modules.firstlevel.business;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import models.firstlevel.request.CurrentUserRequest;
import models.firstlevel.request.LoginRequest;
import models.firstlevel.request.LogoutRequest;
import models.firstlevel.response.CurrentUserResponse;
import models.firstlevel.response.LoginResponse;
import models.firstlevel.response.LogoutResponse;

public class LoginBusiness {

    /** gecerli oturumlar: token -> kullanici adi */
    private static final Map<String, String> TOKENS = new ConcurrentHashMap<>();

    public LoginResponse Login(LoginRequest loginRequest) {

        LoginResponse loginResponse = new LoginResponse(false);

        if(loginRequest != null && loginRequest.username != null && loginRequest.password != null) {

            loginResponse.success = true;
            loginResponse.token = "TOKEN-" + loginRequest.username.toUpperCase();

            TOKENS.put(loginResponse.token, loginRequest.username);
        }

        return loginResponse;
    }

    /** Cikis: token gecersiz kilinir */
    public LogoutResponse Logout(LogoutRequest logoutRequest) {

        if (logoutRequest == null || logoutRequest.token == null) {
            return new LogoutResponse(false, "Token bulunamadi.");
        }

        String username = TOKENS.remove(logoutRequest.token);

        return username == null
                ? new LogoutResponse(false, "Oturum zaten kapali.")
                : new LogoutResponse(true, "Cikis yapildi.");
    }

    /** Token gecerli mi; sayfa yenilendiginde oturumu dogrulamak icin */
    public CurrentUserResponse CurrentUser(CurrentUserRequest currentUserRequest) {

        CurrentUserResponse currentUserResponse = new CurrentUserResponse();
        currentUserResponse.valid = false;

        if (currentUserRequest == null || currentUserRequest.token == null) {
            return currentUserResponse;
        }

        String username = TOKENS.get(currentUserRequest.token);
        if (username == null) {
            return currentUserResponse;
        }

        currentUserResponse.valid = true;
        currentUserResponse.username = username;
        currentUserResponse.token = currentUserRequest.token;

        return currentUserResponse;
    }
}
