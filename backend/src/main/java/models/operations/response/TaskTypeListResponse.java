package models.operations.response;

import java.util.ArrayList;
import java.util.List;

public class TaskTypeListResponse {

    public List<TaskTypeItem> types = new ArrayList<>();

    public Integer totalTaskCount = 0;

    public TaskTypeListResponse() {
    }
}
