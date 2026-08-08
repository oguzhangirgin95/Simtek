package modules.features.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import models.features.entity.FeatureFlag;
import modules.features.repositories.FeatureFlagRepository;

@Service
public class FeatureBusiness {

    private final FeatureFlagRepository featureFlagRepository;

    public FeatureBusiness(FeatureFlagRepository featureFlagRepository) {
        this.featureFlagRepository = featureFlagRepository;
    }

    /**
     * Bu ortamda acik olan ozellik kodlari.
     *
     * Bayrak tablosu okunamazsa bos liste doner: bilinmeyen bayrak kapali
     * sayildigi icin en kotu ihtimalle yeni ozellikler gorunmez, uygulama
     * calismaya devam eder.
     */
    @Transactional(readOnly = true)
    public List<String> EnabledFeatures() {

        List<String> codes = new ArrayList<>();

        for (FeatureFlag flag : featureFlagRepository.findByEnabledTrue()) {
            codes.add(flag.code);
        }

        return codes;
    }

    /**
     * Tek bir ozellik bu ortamda acik mi.
     *
     * Sunucu tarafi mantigi bayraga baglamak icin; kod Features sabitinden
     * verilir. Taninmayan kod kapali sayilir: yanlis yazilmis bir bayrak,
     * ozelligi yanlislikla acmaktansa kapali birakir.
     */
    @Transactional(readOnly = true)
    public boolean IsEnabled(String code) {

        if (code == null || code.isBlank()) {
            return false;
        }

        return featureFlagRepository.findById(code.trim())
                .map(flag -> Boolean.TRUE.equals(flag.enabled))
                .orElse(false);
    }
}
