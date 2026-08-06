package models.operations.request;

public class TaskListRequest {
    public String cityId;

    public String unitId;

    /** tek bir polisin gunluk gorevleri icin */
    public String policeId;

    /** DEVRIYE, RADAR, MOTOSIKLET, OKUL_GECIDI, KAZA_INCELEME */
    public String type;

    /** TAMAMLANDI, DEVAM, PLANLANDI */
    public String status;

    /** true ise sadece gunluk gorev limitini asan polislerin gorevleri */
    public Boolean onlyOverLimit;

    public Integer pageNumber;

    public Integer pageSize;

    public TaskListRequest() {
    }
}
