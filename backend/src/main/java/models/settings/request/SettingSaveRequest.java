package models.settings.request;

public class SettingSaveRequest {
    public String token;

    /** tr, en */
    public String language;

    /** listelerde sayfa basina kayit */
    public Integer pageSize;

    /** ekranlar acilirken secili gelecek sehir */
    public String defaultCityId;

    /** panoda otomatik yenileme suresi (saniye), 0 ise kapali */
    public Integer refreshSeconds;

    public SettingSaveRequest() {
    }
}
