package modules.settings.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.settings.request.SettingGetRequest;
import models.settings.request.SettingSaveRequest;
import models.settings.response.SettingResponse;
import modules.settings.business.SettingBusiness;


@RestController
@RequestMapping("/setting")
public class SettingController {

    /** Kullanicinin ayarlari */
    @PostMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public SettingResponse SettingGet(@RequestBody SettingGetRequest settingGetRequest) {
        SettingBusiness settingBusiness = new SettingBusiness();
        return settingBusiness.SettingGet(settingGetRequest);
    }

    /** Ayarlari kaydeder */
    @PostMapping(path = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public SettingResponse SettingSave(@RequestBody SettingSaveRequest settingSaveRequest) {
        SettingBusiness settingBusiness = new SettingBusiness();
        return settingBusiness.SettingSave(settingSaveRequest);
    }
}
