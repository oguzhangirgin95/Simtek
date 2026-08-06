package modules.firstlevel.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.firstlevel.request.CurrentUserRequest;
import models.firstlevel.request.LoginRequest;
import models.firstlevel.request.LogoutRequest;
import models.firstlevel.response.CurrentUserResponse;
import models.firstlevel.response.LoginResponse;
import models.firstlevel.response.LogoutResponse;
import modules.firstlevel.business.LoginBusiness;


@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping(path = "/eligable", produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse Login(@RequestBody LoginRequest loginRequest) {
        LoginBusiness tokenBusiness = new LoginBusiness();
        return tokenBusiness.Login(loginRequest);
    }

    /** Cikis */
    @PostMapping(path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public LogoutResponse Logout(@RequestBody LogoutRequest logoutRequest) {
        LoginBusiness loginBusiness = new LoginBusiness();
        return loginBusiness.Logout(logoutRequest);
    }

    /** Token gecerli mi */
    @PostMapping(path = "/currentuser", produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrentUserResponse CurrentUser(@RequestBody CurrentUserRequest currentUserRequest) {
        LoginBusiness loginBusiness = new LoginBusiness();
        return loginBusiness.CurrentUser(currentUserRequest);
    }
}
