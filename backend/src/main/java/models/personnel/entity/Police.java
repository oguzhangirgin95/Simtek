package models.personnel.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "police")
public class Police {

    @Id
    @Column(name = "id", length = 20)
    public String id;

    @Column(name = "badge_number", length = 20, nullable = false)
    public String badgeNumber;

    @Column(name = "full_name", length = 100, nullable = false)
    public String fullName;

    @Column(name = "age")
    public Integer age;

    @Column(name = "police_rank", length = 50)
    public String rank;

    @Column(name = "score")
    public Integer score;

    @Column(name = "photo_url", length = 200)
    public String photoUrl;

    @Column(name = "phone", length = 20)
    public String phone;

    @Column(name = "start_date")
    public LocalDate startDate;

    @Column(name = "city_id", length = 10, nullable = false)
    public String cityId;

    @Column(name = "unit_id", length = 20, nullable = false)
    public String unitId;

    @Column(name = "status", length = 20, nullable = false)
    public String status;

    @Column(name = "task_type", length = 20)
    public String taskType;

    @Column(name = "daily_task_count", nullable = false)
    public Integer dailyTaskCount;

    @Column(name = "daily_task_limit", nullable = false)
    public Integer dailyTaskLimit;

    @Column(name = "vehicle_plate", length = 20)
    public String vehiclePlate;

    public Police() {
    }
}
