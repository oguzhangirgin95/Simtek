package modules.operations.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.operations.request.TaskAssignRequest;
import models.operations.request.TaskDeleteRequest;
import models.operations.request.TaskListRequest;
import models.operations.request.TaskTypeListRequest;
import models.operations.response.TaskAssignConfirmResponse;
import models.operations.response.TaskAssignExecuteResponse;
import models.operations.response.TaskDeleteResponse;
import models.operations.response.TaskListResponse;
import models.operations.response.TaskTypeListResponse;
import modules.operations.business.TaskBusiness;


@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskBusiness taskBusiness;

    public TaskController(TaskBusiness taskBusiness) {
        this.taskBusiness = taskBusiness;
    }

    /** Gorev listesi */
    @PostMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskListResponse TaskList(@RequestBody TaskListRequest taskListRequest) {
        return taskBusiness.TaskList(taskListRequest);
    }

    /** Gorev atama onay adimi */
    @PostMapping(path = "/assignconfirm", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskAssignConfirmResponse TaskAssignConfirm(@RequestBody TaskAssignRequest taskAssignRequest) {
        return taskBusiness.TaskAssignConfirm(taskAssignRequest);
    }

    /** Gorev atama gerceklestirme adimi */
    @PostMapping(path = "/assignexecute", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskAssignExecuteResponse TaskAssignExecute(@RequestBody TaskAssignRequest taskAssignRequest) {
        return taskBusiness.TaskAssignExecute(taskAssignRequest);
    }

    /** Gorev silme */
    @PostMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskDeleteResponse TaskDelete(@RequestBody TaskDeleteRequest taskDeleteRequest) {
        return taskBusiness.TaskDelete(taskDeleteRequest);
    }

    /** Gorev tipi filtresi ve tip bazli gorev adetleri */
    @PostMapping(path = "/typelist", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskTypeListResponse TaskTypeList(@RequestBody TaskTypeListRequest taskTypeListRequest) {
        return taskBusiness.TaskTypeList(taskTypeListRequest);
    }
}
