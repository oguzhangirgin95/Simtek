package models.operations.response;

import java.util.ArrayList;
import java.util.List;

public class TaskListResponse {

    public List<TaskListItem> tasks = new ArrayList<>();

    public Integer totalCount = 0;

    public Integer pageNumber = 0;

    public Integer pageSize = 0;

    public TaskListResponse() {
    }
}
