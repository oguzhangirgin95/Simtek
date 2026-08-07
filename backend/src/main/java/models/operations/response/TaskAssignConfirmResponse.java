package models.operations.response;

public class TaskAssignConfirmResponse {

    public Boolean valid = false;

    public String policeId;

    public String policeName;

    public String badgeNumber;

    public String unitName;

    public String cityName;

    public String typeName;

    public String location;

    public String timeRange;

    public Integer currentTaskCount;

    public Integer dailyTaskLimit;

    public Boolean willExceedLimit = false;

    public String message;

    public TaskAssignConfirmResponse() {
    }
}
