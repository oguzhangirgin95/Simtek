package models.personnel.response;

public class PoliceListItem {
    public String id;

    public String badgeNumber;

    public String fullName;

    public String rank;

    public String cityName;

    public String unitName;

    public String status;

    public String statusName;

    public String taskTypeName;

    public Integer score;

    public String photoUrl;

    /** gunluk gorev limitini asti mi */
    public Boolean overDailyLimit;

    public PoliceListItem() {
    }
}
