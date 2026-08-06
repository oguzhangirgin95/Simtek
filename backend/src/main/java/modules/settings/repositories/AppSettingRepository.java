package modules.settings.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import models.settings.entity.AppSetting;

public interface AppSettingRepository extends JpaRepository<AppSetting, String> {
}
