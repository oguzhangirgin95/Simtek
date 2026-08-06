package models.operations.entity;

public class Task {
    public String id;

    public String policeId;

    public String cityId;

    public String unitId;

    /** DEVRIYE, RADAR, MOTOSIKLET, OKUL_GECIDI, KAZA_INCELEME */
    public String type;

    public String location;

    public String startTime;

    public String endTime;

    /** TAMAMLANDI, DEVAM, PLANLANDI */
    public String status;

    public Task() {
    }
}
