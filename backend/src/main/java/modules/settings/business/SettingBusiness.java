package modules.settings.business;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import models.settings.request.SettingGetRequest;
import models.settings.request.SettingSaveRequest;
import models.settings.response.SettingResponse;

public class SettingBusiness {

    /** token -> kullanicinin ayarlari */
    private static final Map<String, SettingResponse> SETTINGS = new ConcurrentHashMap<>();

    private static final String DEFAULT_LANGUAGE = "tr";

    private static final Integer DEFAULT_PAGE_SIZE = 20;

    private static final Integer DEFAULT_REFRESH_SECONDS = 0;

    /** Kullanicinin ayarlari; kayit yoksa varsayilanlar doner */
    public SettingResponse SettingGet(SettingGetRequest settingGetRequest) {

        String token = settingGetRequest == null || settingGetRequest.token == null ? "" : settingGetRequest.token;

        SettingResponse saved = SETTINGS.get(token);
        if (saved != null) {
            return saved;
        }

        SettingResponse settingResponse = new SettingResponse();
        settingResponse.success = true;
        settingResponse.language = DEFAULT_LANGUAGE;
        settingResponse.pageSize = DEFAULT_PAGE_SIZE;
        settingResponse.defaultCityId = "";
        settingResponse.refreshSeconds = DEFAULT_REFRESH_SECONDS;
        settingResponse.message = "Varsayilan ayarlar.";

        return settingResponse;
    }

    /** Ayarlari kaydeder */
    public SettingResponse SettingSave(SettingSaveRequest settingSaveRequest) {

        SettingResponse settingResponse = new SettingResponse();

        if (settingSaveRequest == null || settingSaveRequest.token == null || settingSaveRequest.token.isEmpty()) {
            settingResponse.message = "Oturum bulunamadi.";
            return settingResponse;
        }

        settingResponse.success = true;
        settingResponse.language = settingSaveRequest.language == null || settingSaveRequest.language.isEmpty()
                ? DEFAULT_LANGUAGE
                : settingSaveRequest.language;
        settingResponse.pageSize = settingSaveRequest.pageSize == null || settingSaveRequest.pageSize < 1
                ? DEFAULT_PAGE_SIZE
                : settingSaveRequest.pageSize;
        settingResponse.defaultCityId = settingSaveRequest.defaultCityId == null
                ? ""
                : settingSaveRequest.defaultCityId;
        settingResponse.refreshSeconds = settingSaveRequest.refreshSeconds == null || settingSaveRequest.refreshSeconds < 0
                ? DEFAULT_REFRESH_SECONDS
                : settingSaveRequest.refreshSeconds;
        settingResponse.message = "Ayarlar kaydedildi.";

        SETTINGS.put(settingSaveRequest.token, settingResponse);

        return settingResponse;
    }
}
