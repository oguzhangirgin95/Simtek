package models.settings.request;

public class SettingSaveRequest {
    public String token;

    public String language;

    public Integer pageSize;

    public String defaultCityId;

    public Integer refreshSeconds;

    public SettingSaveRequest() {
    }
}
