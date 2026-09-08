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

    private final LoginBusiness loginBusiness;

    public LoginController(LoginBusiness loginBusiness) {
        this.loginBusiness = loginBusiness;
    }

    @PostMapping(path = "/eligable", produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse Login(@RequestBody LoginRequest loginRequest) {
        return loginBusiness.Login(loginRequest);
    }

    @PostMapping(path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public LogoutResponse Logout(@RequestBody LogoutRequest logoutRequest) {
        return loginBusiness.Logout(logoutRequest);
    }

    @PostMapping(path = "/currentuser", produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrentUserResponse CurrentUser(@RequestBody CurrentUserRequest currentUserRequest) {
        return loginBusiness.CurrentUser(currentUserRequest);
    }
}
