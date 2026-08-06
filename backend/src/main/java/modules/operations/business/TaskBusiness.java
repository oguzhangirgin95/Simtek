package modules.operations.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import models.operations.entity.Task;
import models.operations.request.TaskAssignRequest;
import models.operations.request.TaskDeleteRequest;
import models.operations.request.TaskListRequest;
import models.operations.request.TaskTypeListRequest;
import models.operations.response.TaskAssignConfirmResponse;
import models.operations.response.TaskAssignExecuteResponse;
import models.operations.response.TaskDeleteResponse;
import models.operations.response.TaskListItem;
import models.operations.response.TaskListResponse;
import models.operations.response.TaskTypeItem;
import models.operations.response.TaskTypeListResponse;
import models.personnel.entity.Police;
import models.regions.entity.City;
import models.units.entity.Unit;
import modules.operations.repositories.TaskRepository;
import modules.operations.repositories.TaskTypeCountProjection;
import modules.personnel.business.PoliceBusiness;
import modules.personnel.repositories.PoliceRepository;
import modules.regions.repositories.CityRepository;
import modules.units.repositories.UnitRepository;

@Service
public class TaskBusiness {

    private final TaskRepository taskRepository;
    private final PoliceRepository policeRepository;
    private final CityRepository cityRepository;
    private final UnitRepository unitRepository;

    public TaskBusiness(TaskRepository taskRepository, PoliceRepository policeRepository,
            CityRepository cityRepository, UnitRepository unitRepository) {
        this.taskRepository = taskRepository;
        this.policeRepository = policeRepository;
        this.cityRepository = cityRepository;
        this.unitRepository = unitRepository;
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

    /* ---------------- liste ---------------- */

    @Transactional(readOnly = true)
    public TaskListResponse TaskList(TaskListRequest taskListRequest) {

        TaskListRequest request = taskListRequest == null ? new TaskListRequest() : taskListRequest;

        // limit asanlar filtresi personel uzerinden geldigi icin once o personeller bulunur
        List<String> overLimitPoliceIds = null;
        if (Boolean.TRUE.equals(request.onlyOverLimit)) {
            overLimitPoliceIds = new ArrayList<>();
            Specification<Police> overLimit = (root, query, builder) -> builder
                    .greaterThan(root.get("dailyTaskCount"), root.get("dailyTaskLimit"));
            for (Police police : policeRepository.findAll(overLimit)) {
                overLimitPoliceIds.add(police.id);
            }
        }

        Specification<Task> specification = BuildSpecification(request, overLimitPoliceIds);
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        TaskListResponse response = new TaskListResponse();
        List<Task> tasks;

        int pageNumber = request.pageNumber == null ? 0 : request.pageNumber;
        int pageSize = request.pageSize == null ? 0 : request.pageSize;

        if (pageNumber > 0 && pageSize > 0) {
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
            Page<Task> page = taskRepository.findAll(specification, pageable);

            tasks = page.getContent();
            response.totalCount = (int) page.getTotalElements();
            response.pageNumber = pageNumber;
            response.pageSize = pageSize;
        } else {
            tasks = taskRepository.findAll(specification, sort);
            response.totalCount = tasks.size();
        }

        Map<String, Police> policeMap = GetPoliceMap();
        Map<String, String> cityNames = GetCityNames();
        Map<String, String> unitNames = GetUnitNames();

        for (Task task : tasks) {
            Police police = policeMap.get(task.policeId);

            TaskListItem item = new TaskListItem();
            item.id = task.id;
            item.policeId = task.policeId;
            item.policeName = police == null ? "" : police.fullName;
            item.badgeNumber = police == null ? "" : police.badgeNumber;
            item.cityName = cityNames.getOrDefault(task.cityId, "");
            item.unitName = unitNames.getOrDefault(task.unitId, "");
            item.type = task.type;
            item.typeName = PoliceBusiness.GetTaskTypeName(task.type);
            item.location = task.location;
            item.startTime = task.startTime;
            item.endTime = task.endTime;
            item.status = task.status;
            item.statusName = GetStatusName(task.status);

            response.tasks.add(item);
        }

        return response;
    }

    private Specification<Task> BuildSpecification(TaskListRequest request, List<String> overLimitPoliceIds) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (overLimitPoliceIds != null) {
                if (overLimitPoliceIds.isEmpty()) {
                    return builder.disjunction();
                }
                predicates.add(root.get("policeId").in(overLimitPoliceIds));
            }
            if (HasText(request.cityId)) {
                predicates.add(builder.equal(root.get("cityId"), request.cityId));
            }
            if (HasText(request.unitId)) {
                predicates.add(builder.equal(root.get("unitId"), request.unitId));
            }
            if (HasText(request.policeId)) {
                predicates.add(builder.equal(root.get("policeId"), request.policeId));
            }
            if (HasText(request.type)) {
                predicates.add(builder.equal(root.get("type"), request.type));
            }
            if (HasText(request.status)) {
                predicates.add(builder.equal(root.get("status"), request.status));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /** Gorev tipi filtresinin secenekleri ve adetleri */
    @Transactional(readOnly = true)
    public TaskTypeListResponse TaskTypeList(TaskTypeListRequest taskTypeListRequest) {

        String cityId = taskTypeListRequest == null || !HasText(taskTypeListRequest.cityId)
                ? null
                : taskTypeListRequest.cityId;

        TaskTypeListResponse response = new TaskTypeListResponse();

        for (TaskTypeCountProjection projection : taskRepository.typeCounts(cityId)) {
            int count = projection.getTypeCount().intValue();
            response.types.add(new TaskTypeItem(projection.getTaskType(),
                    PoliceBusiness.GetTaskTypeName(projection.getTaskType()), count));
            response.totalTaskCount += count;
        }

        return response;
    }

    /* ---------------- gorev atama ---------------- */

    @Transactional(readOnly = true)
    public TaskAssignConfirmResponse TaskAssignConfirm(TaskAssignRequest taskAssignRequest) {

        TaskAssignConfirmResponse response = new TaskAssignConfirmResponse();

        if (taskAssignRequest == null || !HasText(taskAssignRequest.policeId)) {
            response.message = "Personel secilmedi.";
            return response;
        }

        Optional<Police> found = policeRepository.findById(taskAssignRequest.policeId);
        if (found.isEmpty()) {
            response.message = "Personel bulunamadi.";
            return response;
        }
        if (!HasText(taskAssignRequest.type)) {
            response.message = "Gorev tipi secilmedi.";
            return response;
        }

        Police police = found.get();

        response.valid = true;
        response.policeId = police.id;
        response.policeName = police.fullName;
        response.badgeNumber = police.badgeNumber;
        response.unitName = GetUnitNames().getOrDefault(police.unitId, "");
        response.cityName = GetCityNames().getOrDefault(police.cityId, "");
        response.typeName = PoliceBusiness.GetTaskTypeName(taskAssignRequest.type);
        response.location = taskAssignRequest.location;
        response.timeRange = taskAssignRequest.startTime + " - " + taskAssignRequest.endTime;
        response.currentTaskCount = (int) taskRepository.countByPoliceId(police.id);
        response.dailyTaskLimit = police.dailyTaskLimit;
        response.willExceedLimit = response.currentTaskCount + 1 > police.dailyTaskLimit;
        response.message = response.willExceedLimit
                ? "Bu atama ile personel gunluk gorev limitini asacak."
                : "Atama yapilabilir.";

        return response;
    }

    @Transactional
    public TaskAssignExecuteResponse TaskAssignExecute(TaskAssignRequest taskAssignRequest) {

        TaskAssignExecuteResponse response = new TaskAssignExecuteResponse();

        if (taskAssignRequest == null || !HasText(taskAssignRequest.policeId)) {
            response.message = "Personel secilmedi.";
            return response;
        }

        Optional<Police> found = policeRepository.findById(taskAssignRequest.policeId);
        if (found.isEmpty()) {
            response.message = "Personel bulunamadi.";
            return response;
        }

        Police police = found.get();
        int taskCount = (int) taskRepository.countByPoliceId(police.id);

        Task task = new Task();
        task.id = police.id + "-G" + (taskCount + 1);
        task.policeId = police.id;
        task.cityId = police.cityId;
        task.unitId = police.unitId;
        task.type = taskAssignRequest.type;
        task.location = taskAssignRequest.location;
        task.startTime = taskAssignRequest.startTime;
        task.endTime = taskAssignRequest.endTime;
        task.status = "PLANLANDI";

        taskRepository.save(task);

        police.dailyTaskCount = taskCount + 1;
        policeRepository.save(police);

        response.success = true;
        response.taskId = task.id;
        response.policeName = police.fullName;
        response.typeName = PoliceBusiness.GetTaskTypeName(task.type);
        response.timeRange = task.startTime + " - " + task.endTime;
        response.newTaskCount = police.dailyTaskCount;
        response.message = "Gorev atandi.";

        return response;
    }

    /** Gorev silme; personelin gunluk sayaci guncellenir */
    @Transactional
    public TaskDeleteResponse TaskDelete(TaskDeleteRequest request) {

        TaskDeleteResponse response = new TaskDeleteResponse();

        if (request == null || !HasText(request.taskId)) {
            response.message = "Gorev secilmedi.";
            return response;
        }

        Optional<Task> found = taskRepository.findById(request.taskId);
        if (found.isEmpty()) {
            response.message = "Gorev bulunamadi.";
            return response;
        }

        Task task = found.get();
        taskRepository.delete(task);

        Police police = policeRepository.findById(task.policeId).orElse(null);
        if (police != null) {
            police.dailyTaskCount = (int) taskRepository.countByPoliceId(police.id);
            policeRepository.save(police);
            response.newTaskCount = police.dailyTaskCount;
        }

        response.success = true;
        response.message = "Gorev silindi.";

        return response;
    }

    /* ---------------- yardimcilar ---------------- */

    private Map<String, Police> GetPoliceMap() {
        Map<String, Police> map = new HashMap<>();
        for (Police police : policeRepository.findAll()) {
            map.put(police.id, police);
        }
        return map;
    }

    private Map<String, String> GetCityNames() {
        Map<String, String> names = new HashMap<>();
        for (City city : cityRepository.findAll()) {
            names.put(city.id, city.name);
        }
        return names;
    }

    private Map<String, String> GetUnitNames() {
        Map<String, String> names = new HashMap<>();
        for (Unit unit : unitRepository.findAll()) {
            names.put(unit.id, unit.name);
        }
        return names;
    }

    private static boolean HasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
