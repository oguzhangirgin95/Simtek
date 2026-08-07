package models.vehicles.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @Column(name = "plate", length = 20)
    public String plate;

    @Column(name = "police_id", length = 20)
    public String policeId;

    @Column(name = "brand", length = 50)
    public String brand;

    @Column(name = "model", length = 50)
    public String model;

    @Column(name = "model_year")
    public Integer modelYear;

    @Column(name = "vehicle_type", length = 20)
    public String type;

    @Column(name = "kilometers")
    public Integer kilometers;

    @Column(name = "last_maintenance_date")
    public LocalDate lastMaintenanceDate;

    @Column(name = "photo_url", length = 200)
    public String photoUrl;

    public Vehicle() {
    }
}
