package models.operations.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @Column(name = "id", length = 30)
    public String id;

    @Column(name = "police_id", length = 20, nullable = false)
    public String policeId;

    @Column(name = "city_id", length = 10, nullable = false)
    public String cityId;

    @Column(name = "unit_id", length = 20, nullable = false)
    public String unitId;

    /** DEVRIYE, RADAR, MOTOSIKLET, OKUL_GECIDI, KAZA_INCELEME */
    @Column(name = "task_type", length = 20, nullable = false)
    public String type;

    @Column(name = "location", length = 100)
    public String location;

    @Column(name = "start_time", length = 5)
    public String startTime;

    @Column(name = "end_time", length = 5)
    public String endTime;

    /** TAMAMLANDI, DEVAM, PLANLANDI */
    @Column(name = "task_status", length = 20, nullable = false)
    public String status;

    public Task() {
    }
}
