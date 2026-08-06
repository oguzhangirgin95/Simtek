package modules.firstlevel.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.firstlevel.request.LoginRequest;
import models.firstlevel.response.LoginResponse;
import modules.firstlevel.business.LoginBusiness;


@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping(path = "/eligable", produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse Login(@RequestBody LoginRequest loginRequest) {
        LoginBusiness tokenBusiness = new LoginBusiness();
        return tokenBusiness.Login(loginRequest);
    }
}
