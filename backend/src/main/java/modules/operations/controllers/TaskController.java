package modules.operations.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.operations.request.TaskAssignRequest;
import models.operations.request.TaskListRequest;
import models.operations.request.TaskTypeListRequest;
import models.operations.response.TaskAssignConfirmResponse;
import models.operations.response.TaskAssignExecuteResponse;
import models.operations.response.TaskListResponse;
import models.operations.response.TaskTypeListResponse;
import modules.operations.business.TaskBusiness;


@RestController
@RequestMapping("/task")
public class TaskController {

    /** Gorev listesi; polis secilince gunluk gorevleri, onlyOverLimit ile limit asanlar */
    @PostMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskListResponse TaskList(@RequestBody TaskListRequest taskListRequest) {
        TaskBusiness taskBusiness = new TaskBusiness();
        return taskBusiness.TaskList(taskListRequest);
    }

    /** Gorev atama onay adimi */
    @PostMapping(path = "/assignconfirm", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskAssignConfirmResponse TaskAssignConfirm(@RequestBody TaskAssignRequest taskAssignRequest) {
        TaskBusiness taskBusiness = new TaskBusiness();
        return taskBusiness.TaskAssignConfirm(taskAssignRequest);
    }

    /** Gorev atama gerceklestirme adimi */
    @PostMapping(path = "/assignexecute", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskAssignExecuteResponse TaskAssignExecute(@RequestBody TaskAssignRequest taskAssignRequest) {
        TaskBusiness taskBusiness = new TaskBusiness();
        return taskBusiness.TaskAssignExecute(taskAssignRequest);
    }

    /** Gorev tipi filtresi ve tip bazli gorev adetleri */
    @PostMapping(path = "/typelist", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskTypeListResponse TaskTypeList(@RequestBody TaskTypeListRequest taskTypeListRequest) {
        TaskBusiness taskBusiness = new TaskBusiness();
        return taskBusiness.TaskTypeList(taskTypeListRequest);
    }
}
