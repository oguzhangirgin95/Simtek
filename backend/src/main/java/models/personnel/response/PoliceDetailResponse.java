package models.personnel.response;

public class PoliceDetailResponse {

    /** kayit bulundu mu; false ise diger alanlar bostur */
    public Boolean found = false;

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

    public String status;

    public String statusName;

    public String taskType;

    public String taskTypeName;

    public Integer dailyTaskCount;

    public Integer dailyTaskLimit;

    public Boolean overDailyLimit;

    /** arac detayi icin /vehicle/detail servisine gonderilir */
    public String vehiclePlate;

    public PoliceDetailResponse() {
    }
}
