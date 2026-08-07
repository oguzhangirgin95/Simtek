package models.operations.request;

public class TaskListRequest {
    public String cityId;

    public String unitId;

    public String policeId;

    public String type;

    public String status;

    public Boolean onlyOverLimit;

    public Integer pageNumber;

    public Integer pageSize;

    public TaskListRequest() {
    }
}
