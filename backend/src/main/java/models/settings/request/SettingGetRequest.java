package models.settings.request;

public class SettingGetRequest {
    /** oturum token'i; ayarlar kullaniciya gore tutulur */
    public String token;

    public SettingGetRequest() {
    }

    public SettingGetRequest(String token) {
        this.token = token;
    }
}
