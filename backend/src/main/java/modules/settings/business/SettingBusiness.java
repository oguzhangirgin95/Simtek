package modules.settings.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import models.settings.entity.AppSetting;
import models.settings.request.SettingGetRequest;
import models.settings.request.SettingSaveRequest;
import models.settings.response.SettingResponse;
import modules.settings.repositories.AppSettingRepository;

@Service
public class SettingBusiness {

    private static final String DEFAULT_LANGUAGE = "tr";

    private static final Integer DEFAULT_PAGE_SIZE = 20;

    private static final Integer DEFAULT_REFRESH_SECONDS = 0;

    private final AppSettingRepository appSettingRepository;

    public SettingBusiness(AppSettingRepository appSettingRepository) {
        this.appSettingRepository = appSettingRepository;
    }

    @Transactional(readOnly = true)
    public SettingResponse SettingGet(SettingGetRequest settingGetRequest) {

        SettingResponse response = new SettingResponse();
        response.success = true;
        response.language = DEFAULT_LANGUAGE;
        response.pageSize = DEFAULT_PAGE_SIZE;
        response.defaultCityId = "";
        response.refreshSeconds = DEFAULT_REFRESH_SECONDS;
        response.message = "Varsayilan ayarlar.";

        if (settingGetRequest == null || settingGetRequest.token == null) {
            return response;
        }

        AppSetting setting = appSettingRepository.findById(settingGetRequest.token).orElse(null);
        if (setting == null) {
            return response;
        }

        response.language = setting.language;
        response.pageSize = setting.pageSize;
        response.defaultCityId = setting.defaultCityId;
        response.refreshSeconds = setting.refreshSeconds;
        response.message = "Ayarlar yuklendi.";

        return response;
    }

    @Transactional
    public SettingResponse SettingSave(SettingSaveRequest settingSaveRequest) {

        SettingResponse response = new SettingResponse();

        if (settingSaveRequest == null || settingSaveRequest.token == null || settingSaveRequest.token.isEmpty()) {
            response.message = "Oturum bulunamadi.";
            return response;
        }

        AppSetting setting = appSettingRepository.findById(settingSaveRequest.token).orElseGet(AppSetting::new);
        setting.token = settingSaveRequest.token;
        setting.language = settingSaveRequest.language == null || settingSaveRequest.language.isEmpty()
                ? DEFAULT_LANGUAGE
                : settingSaveRequest.language;
        setting.pageSize = settingSaveRequest.pageSize == null || settingSaveRequest.pageSize < 1
                ? DEFAULT_PAGE_SIZE
                : settingSaveRequest.pageSize;
        setting.defaultCityId = settingSaveRequest.defaultCityId == null ? "" : settingSaveRequest.defaultCityId;
        setting.refreshSeconds = settingSaveRequest.refreshSeconds == null || settingSaveRequest.refreshSeconds < 0
                ? DEFAULT_REFRESH_SECONDS
                : settingSaveRequest.refreshSeconds;

        appSettingRepository.save(setting);

        response.success = true;
        response.language = setting.language;
        response.pageSize = setting.pageSize;
        response.defaultCityId = setting.defaultCityId;
        response.refreshSeconds = setting.refreshSeconds;
        response.message = "Ayarlar kaydedildi.";

        return response;
    }
}
