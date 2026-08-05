package modules.firstlevel.controllers; 

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.extended.firstlevel.request.LoginExtendedRequest;
import modules.firstlevel.business.TokenBusiness;


@RestController
@RequestMapping("/token")
public class TokenController {

    @PostMapping("/login")
    public Boolean Login(LoginExtendedRequest loginRequest) {
        TokenBusiness tokenBusiness = new TokenBusiness();
        return tokenBusiness.Login(loginRequest);
    }
}