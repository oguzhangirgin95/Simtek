package modules.firstlevel.controllers;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.firstlevel.request.LoginRequest;
import models.firstlevel.response.CurrentUserResponse;
import models.firstlevel.response.LoginResponse;
import modules.firstlevel.business.LoginBusiness;


@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginBusiness loginBusiness;

    public LoginController(LoginBusiness loginBusiness) {
        this.loginBusiness = loginBusiness;
    }

    /** Giris. Henuz token olmadigi icin bu uc dogrulama istemiyor. */
    @PostMapping(path = "/eligable", produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse Login(@RequestBody LoginRequest loginRequest) {
        return loginBusiness.Login(loginRequest);
    }

    /** Cikis ucu kaldirildi; token sunucuda tutulmadigi icin elden silmek yeterli. */
    @PostMapping(path = "/currentuser", produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrentUserResponse CurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return loginBusiness.CurrentUser(jwt);
    }
}
