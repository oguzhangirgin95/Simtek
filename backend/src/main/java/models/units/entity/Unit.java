package models.units.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "unit")
public class Unit {

    @Id
    @Column(name = "id", length = 20)
    public String id;

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @Column(name = "city_id", length = 10, nullable = false)
    public String cityId;

    @Column(name = "seq", nullable = false)
    public Integer seq;

    public Unit() {
    }
}
