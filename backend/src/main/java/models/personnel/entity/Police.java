package models.personnel.entity;

public class Police {
    public String id;

    public String badgeNumber;

    public String fullName;

    public Integer age;

    public String rank;

    public Integer score;

    public String photoUrl;

    public String phone;

    public String startDate;

    public String cityId;

    public String cityName;

    public String unitId;

    public String unitName;

    /** SAHADA, MERKEZDE, IZINDE, RAPORLU */
    public String status;

    /** DEVRIYE, RADAR, MOTOSIKLET, OKUL_GECIDI, KAZA_INCELEME */
    public String taskType;

    public Integer dailyTaskCount;

    public Integer dailyTaskLimit;

    public String vehiclePlate;

    public Police() {
    }
}
