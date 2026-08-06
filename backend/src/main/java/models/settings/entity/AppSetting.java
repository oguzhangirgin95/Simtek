package models.settings.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_setting")
public class AppSetting {

    @Id
    @Column(name = "token", length = 100)
    public String token;

    @Column(name = "language", length = 5, nullable = false)
    public String language;

    @Column(name = "page_size", nullable = false)
    public Integer pageSize;

    @Column(name = "default_city_id", length = 10)
    public String defaultCityId;

    @Column(name = "refresh_seconds", nullable = false)
    public Integer refreshSeconds;

    public AppSetting() {
    }
}
