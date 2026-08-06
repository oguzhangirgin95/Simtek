package models.operations.request;

public class TaskAssignRequest {
    public String policeId;

    /** DEVRIYE, RADAR, MOTOSIKLET, OKUL_GECIDI, KAZA_INCELEME */
    public String type;

    public String location;

    public String startTime;

    public String endTime;

    public TaskAssignRequest() {
    }
}
