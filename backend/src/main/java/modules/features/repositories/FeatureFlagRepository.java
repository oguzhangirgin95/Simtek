package modules.features.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import models.features.entity.FeatureFlag;

/** Ozellik bayraklarinin veritabani erisimi. */
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, String> {

    /** Bu ortamda acik olan bayraklar. */
    List<FeatureFlag> findByEnabledTrue();
}
