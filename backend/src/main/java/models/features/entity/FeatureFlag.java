package models.features.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Tek bir ozellik bayragi.
 *
 * Her ortam kendi veritabanina bagli oldugu icin ayni kod test'te acik,
 * prod'da kapali olabilir.
 */
@Entity
@Table(name = "feature_flag")
public class FeatureFlag {

    /** Ozellik numarasi, genelde JIRA kodu: 'SIM-123'. */
    @Id
    @Column(name = "code", length = 50)
    public String code;

    /** Ne oldugunu hatirlatan kisa aciklama. */
    @Column(name = "description", length = 200)
    public String description;

    /** Bu ortamda acik mi. */
    @Column(name = "enabled", nullable = false)
    public Boolean enabled;

    public FeatureFlag() {
    }
}
