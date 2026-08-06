package modules.operations.business;

import java.util.ArrayList;
import java.util.List;

import models.operations.entity.Task;
import models.operations.request.TaskListRequest;
import models.operations.request.TaskTypeListRequest;
import models.operations.response.TaskListItem;
import models.operations.response.TaskListResponse;
import models.operations.response.TaskTypeItem;
import models.operations.response.TaskTypeListResponse;
import models.personnel.entity.Police;
import modules.personnel.business.PoliceBusiness;

public class TaskBusiness {

    /** Her polisin gunluk gorevleri; polisin dailyTaskCount degeri kadar kayit uretilir. */
    private static final List<Task> TASKS = new ArrayList<>();

    private static final String[] LOCATIONS = {
            "D-100 Karayolu", "Cevre Yolu 12. km", "Merkez Kavsagi", "Istasyon Caddesi",
            "Universite Kavsagi", "Sanayi Bulvari", "Sahil Yolu", "Otogar Cikisi"
    };

    private static final String[] TASK_TYPES = {
            "DEVRIYE", "RADAR", "MOTOSIKLET", "OKUL_GECIDI", "KAZA_INCELEME"
    };

    static {
        int index = 0;

        for (Police police : PoliceBusiness.GetPoliceList()) {

            int taskCount = police.dailyTaskCount == null ? 0 : police.dailyTaskCount;

            for (int i = 0; i < taskCount; i++) {

                Task task = new Task();
                task.id = police.id + "-G" + (i + 1);
                task.policeId = police.id;
                task.cityId = police.cityId;
                task.unitId = police.unitId;

                // ilk gorev polisin ana gorev tipi, digerleri sirayla dagitilir
                task.type = i == 0 ? police.taskType : TASK_TYPES[(index + i) % TASK_TYPES.length];
                task.location = LOCATIONS[(index + i) % LOCATIONS.length];
                task.startTime = String.format("%02d:00", 8 + i);
                task.endTime = String.format("%02d:00", 9 + i);

                if (i < taskCount - 2) {
                    task.status = "TAMAMLANDI";
                } else if (i == taskCount - 2) {
                    task.status = "DEVAM";
                } else {
                    task.status = "PLANLANDI";
                }

                TASKS.add(task);
            }

            index++;
        }
    }

    public static List<Task> GetTasks() {
        return TASKS;
    }

    public static String GetStatusName(String status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case "TAMAMLANDI":
                return "Tamamlandi";
            case "DEVAM":
                return "Devam ediyor";
            case "PLANLANDI":
                return "Planlandi";
            default:
                return status;
        }
    }

    /** Gorev listesi: sehir / birim / polis / tip / durum filtreleriyle */
    public TaskListResponse TaskList(TaskListRequest taskListRequest) {

        String cityId = taskListRequest == null || taskListRequest.cityId == null ? "" : taskListRequest.cityId;
        String unitId = taskListRequest == null || taskListRequest.unitId == null ? "" : taskListRequest.unitId;
        String policeId = taskListRequest == null || taskListRequest.policeId == null ? "" : taskListRequest.policeId;
        String type = taskListRequest == null || taskListRequest.type == null ? "" : taskListRequest.type;
        String status = taskListRequest == null || taskListRequest.status == null ? "" : taskListRequest.status;
        boolean onlyOverLimit = taskListRequest != null && Boolean.TRUE.equals(taskListRequest.onlyOverLimit);

        TaskListResponse taskListResponse = new TaskListResponse();
        List<TaskListItem> filtered = new ArrayList<>();

        for (Task task : TASKS) {

            Police police = PoliceBusiness.GetPolice(task.policeId);
            if (police == null) {
                continue;
            }
            if (!cityId.isEmpty() && !task.cityId.equals(cityId)) {
                continue;
            }
            if (!unitId.isEmpty() && !task.unitId.equals(unitId)) {
                continue;
            }
            if (!policeId.isEmpty() && !task.policeId.equals(policeId)) {
                continue;
            }
            if (!type.isEmpty() && !task.type.equals(type)) {
                continue;
            }
            if (!status.isEmpty() && !task.status.equals(status)) {
                continue;
            }
            if (onlyOverLimit && !PoliceBusiness.IsOverDailyLimit(police)) {
                continue;
            }

            TaskListItem item = new TaskListItem();
            item.id = task.id;
            item.policeId = task.policeId;
            item.policeName = police.fullName;
            item.badgeNumber = police.badgeNumber;
            item.cityName = police.cityName;
            item.unitName = police.unitName;
            item.type = task.type;
            item.typeName = PoliceBusiness.GetTaskTypeName(task.type);
            item.location = task.location;
            item.startTime = task.startTime;
            item.endTime = task.endTime;
            item.status = task.status;
            item.statusName = GetStatusName(task.status);

            filtered.add(item);
        }

        taskListResponse.totalCount = filtered.size();

        int pageNumber = taskListRequest == null || taskListRequest.pageNumber == null
                ? 0
                : taskListRequest.pageNumber;
        int pageSize = taskListRequest == null || taskListRequest.pageSize == null ? 0 : taskListRequest.pageSize;

        if (pageNumber < 1 || pageSize < 1) {
            taskListResponse.tasks = filtered;
            return taskListResponse;
        }

        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, filtered.size());

        if (fromIndex < filtered.size()) {
            taskListResponse.tasks = new ArrayList<>(filtered.subList(fromIndex, toIndex));
        }

        taskListResponse.pageNumber = pageNumber;
        taskListResponse.pageSize = pageSize;

        return taskListResponse;
    }

    /** Gorev tipi filtresinin secenekleri ve adetleri (grafik icin de kullanilabilir) */
    public TaskTypeListResponse TaskTypeList(TaskTypeListRequest taskTypeListRequest) {

        String cityId = taskTypeListRequest == null || taskTypeListRequest.cityId == null
                ? ""
                : taskTypeListRequest.cityId;

        TaskTypeListResponse taskTypeListResponse = new TaskTypeListResponse();

        for (String type : TASK_TYPES) {

            int count = 0;
            for (Task task : TASKS) {
                if (!cityId.isEmpty() && !task.cityId.equals(cityId)) {
                    continue;
                }
                if (task.type.equals(type)) {
                    count++;
                }
            }

            taskTypeListResponse.types.add(new TaskTypeItem(type, PoliceBusiness.GetTaskTypeName(type), count));
            taskTypeListResponse.totalTaskCount += count;
        }

        return taskTypeListResponse;
    }
}
