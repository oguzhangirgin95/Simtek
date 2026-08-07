package models.regions.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "city")
public class City {

    @Id
    @Column(name = "id", length = 10)
    public String id;

    @Column(name = "name", length = 50, nullable = false)
    public String name;

    @Column(name = "plate_code", length = 5, nullable = false)
    public String plateCode;

    @Column(name = "map_x", nullable = false)
    public Double x;

    @Column(name = "map_y", nullable = false)
    public Double y;

    @Column(name = "sort_order", nullable = false)
    public Integer sortOrder;

    public City() {
    }
}
