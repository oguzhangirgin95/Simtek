package modules.settings.controllers;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.settings.request.SettingSaveRequest;
import models.settings.response.SettingResponse;
import modules.settings.business.SettingBusiness;


@RestController
@RequestMapping("/setting")
public class SettingController {

    private final SettingBusiness settingBusiness;

    public SettingController(SettingBusiness settingBusiness) {
        this.settingBusiness = settingBusiness;
    }

    @PostMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public SettingResponse SettingGet(@AuthenticationPrincipal Jwt jwt) {
        return settingBusiness.SettingGet(jwt.getClaimAsString("preferred_username"));
    }

    @PostMapping(path = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public SettingResponse SettingSave(@RequestBody SettingSaveRequest settingSaveRequest,
            @AuthenticationPrincipal Jwt jwt) {
        return settingBusiness.SettingSave(settingSaveRequest, jwt.getClaimAsString("preferred_username"));
    }
}
