package modules.firstlevel.controllers; 

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.firstlevel.request.LoginRequest;
import modules.firstlevel.business.LoginBusiness;


@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping("/eligable")
    public Boolean Login(LoginRequest loginRequest) {
        LoginBusiness tokenBusiness = new LoginBusiness();
        return tokenBusiness.Login(loginRequest);
    }
}