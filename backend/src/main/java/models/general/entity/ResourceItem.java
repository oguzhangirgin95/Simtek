package models.general.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "resource")
public class ResourceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    /** general, login, dashboard, policelist ... */
    @Column(name = "transaction_name", length = 50, nullable = false)
    public String transactionName;

    @Column(name = "resource_key", length = 80, nullable = false)
    public String key;

    @Column(name = "resource_value", length = 300, nullable = false)
    public String value;

    public ResourceItem() {
    }
}
