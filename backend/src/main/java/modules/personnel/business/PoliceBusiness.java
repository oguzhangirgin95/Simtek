package modules.personnel.business;

import java.time.LocalDate;
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
import models.personnel.entity.Police;
import models.personnel.request.PoliceDeleteRequest;
import models.personnel.request.PoliceDetailRequest;
import models.personnel.request.PoliceListRequest;
import models.personnel.request.PoliceSaveRequest;
import models.personnel.request.PoliceStatusListRequest;
import models.personnel.response.PoliceDeleteResponse;
import models.personnel.response.PoliceDetailResponse;
import models.personnel.response.PoliceListItem;
import models.personnel.response.PoliceListResponse;
import models.personnel.response.PoliceSaveResponse;
import models.personnel.response.PoliceStatusItem;
import models.personnel.response.PoliceStatusListResponse;
import models.regions.entity.City;
import models.units.entity.Unit;
import modules.operations.repositories.TaskRepository;
import modules.personnel.repositories.PoliceRepository;
import modules.regions.repositories.CityRepository;
import modules.units.repositories.UnitRepository;
import modules.vehicles.repositories.VehicleRepository;

@Service
public class PoliceBusiness {

    private final PoliceRepository policeRepository;
    private final CityRepository cityRepository;
    private final UnitRepository unitRepository;
    private final TaskRepository taskRepository;
    private final VehicleRepository vehicleRepository;

    public PoliceBusiness(PoliceRepository policeRepository, CityRepository cityRepository,
            UnitRepository unitRepository, TaskRepository taskRepository, VehicleRepository vehicleRepository) {
        this.policeRepository = policeRepository;
        this.cityRepository = cityRepository;
        this.unitRepository = unitRepository;
        this.taskRepository = taskRepository;
        this.vehicleRepository = vehicleRepository;
    }


    public static Boolean IsOverDailyLimit(Police police) {
        return police.dailyTaskCount > police.dailyTaskLimit;
    }

    public static String GetStatusName(String status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case "SAHADA":
                return "Sahada";
            case "MERKEZDE":
                return "Merkezde";
            case "IZINDE":
                return "Izinde";
            case "RAPORLU":
                return "Raporlu";
            default:
                return status;
        }
    }

    public static String GetTaskTypeName(String taskType) {
        if (taskType == null) {
            return "";
        }
        switch (taskType) {
            case "DEVRIYE":
                return "Devriye";
            case "RADAR":
                return "Radar";
            case "MOTOSIKLET":
                return "Motosiklet";
            case "OKUL_GECIDI":
                return "Okul Gecidi";
            case "KAZA_INCELEME":
                return "Kaza Inceleme";
            default:
                return taskType;
        }
    }


    @Transactional(readOnly = true)
    public PoliceListResponse PoliceList(PoliceListRequest policeListRequest) {

        PoliceListRequest request = policeListRequest == null ? new PoliceListRequest() : policeListRequest;

        Specification<Police> specification = BuildSpecification(request);
        Sort sort = BuildSort(request);

        PoliceListResponse policeListResponse = new PoliceListResponse();
        List<Police> policeList;

        int pageNumber = request.pageNumber == null ? 0 : request.pageNumber;
        int pageSize = request.pageSize == null ? 0 : request.pageSize;

        if (pageNumber > 0 && pageSize > 0) {
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
            Page<Police> page = policeRepository.findAll(specification, pageable);

            policeList = page.getContent();
            policeListResponse.totalCount = (int) page.getTotalElements();
            policeListResponse.pageNumber = pageNumber;
            policeListResponse.pageSize = pageSize;
        } else {
            policeList = policeRepository.findAll(specification, sort);
            policeListResponse.totalCount = policeList.size();
        }

        Map<String, String> cityNames = GetCityNames();
        Map<String, String> unitNames = GetUnitNames();

        for (Police police : policeList) {
            PoliceListItem item = new PoliceListItem();
            item.id = police.id;
            item.badgeNumber = police.badgeNumber;
            item.fullName = police.fullName;
            item.rank = police.rank;
            item.cityName = cityNames.getOrDefault(police.cityId, "");
            item.unitName = unitNames.getOrDefault(police.unitId, "");
            item.status = police.status;
            item.statusName = GetStatusName(police.status);
            item.taskTypeName = GetTaskTypeName(police.taskType);
            item.score = police.score;
            item.photoUrl = police.photoUrl;
            item.overDailyLimit = IsOverDailyLimit(police);

            policeListResponse.policeList.add(item);
        }

        return policeListResponse;
    }

    private Specification<Police> BuildSpecification(PoliceListRequest request) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (HasText(request.cityId)) {
                predicates.add(builder.equal(root.get("cityId"), request.cityId));
            }
            if (HasText(request.unitId)) {
                predicates.add(builder.equal(root.get("unitId"), request.unitId));
            }
            if (HasText(request.status)) {
                predicates.add(builder.equal(root.get("status"), request.status));
            }
            if (HasText(request.searchText)) {
                String pattern = "%" + request.searchText.trim().toLowerCase() + "%";
                predicates.add(builder.or(
                        builder.like(builder.lower(root.get("fullName")), pattern),
                        builder.like(builder.lower(root.get("badgeNumber")), pattern)));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Sort BuildSort(PoliceListRequest request) {

        String field = HasText(request.sortField) ? request.sortField : "badgeNumber";

        switch (field) {
            case "cityName":
                field = "cityId";
                break;
            case "unitName":
                field = "unitId";
                break;
            case "fullName":
            case "score":
            case "badgeNumber":
            case "age":
            case "dailyTaskCount":
                break;
            default:
                field = "badgeNumber";
                break;
        }

        boolean descending = request.sortDirection != null && request.sortDirection.equalsIgnoreCase("DESC");

        return Sort.by(descending ? Sort.Direction.DESC : Sort.Direction.ASC, field);
    }


    @Transactional(readOnly = true)
    public PoliceDetailResponse PoliceDetail(PoliceDetailRequest policeDetailRequest) {

        PoliceDetailResponse response = new PoliceDetailResponse();

        if (policeDetailRequest == null || policeDetailRequest.policeId == null) {
            return response;
        }

        Optional<Police> found = policeRepository.findById(policeDetailRequest.policeId);
        if (found.isEmpty()) {
            return response;
        }

        Police police = found.get();
        Map<String, String> cityNames = GetCityNames();
        Map<String, String> unitNames = GetUnitNames();

        response.found = true;
        response.id = police.id;
        response.badgeNumber = police.badgeNumber;
        response.fullName = police.fullName;
        response.age = police.age;
        response.rank = police.rank;
        response.score = police.score;
        response.photoUrl = police.photoUrl;
        response.phone = police.phone;
        response.startDate = police.startDate == null ? null : police.startDate.toString();
        response.cityId = police.cityId;
        response.cityName = cityNames.getOrDefault(police.cityId, "");
        response.unitId = police.unitId;
        response.unitName = unitNames.getOrDefault(police.unitId, "");
        response.status = police.status;
        response.statusName = GetStatusName(police.status);
        response.taskType = police.taskType;
        response.taskTypeName = GetTaskTypeName(police.taskType);
        response.dailyTaskCount = police.dailyTaskCount;
        response.dailyTaskLimit = police.dailyTaskLimit;
        response.overDailyLimit = IsOverDailyLimit(police);
        response.vehiclePlate = police.vehiclePlate;

        return response;
    }

    public PoliceStatusListResponse PoliceStatusList(PoliceStatusListRequest policeStatusListRequest) {

        PoliceStatusListResponse response = new PoliceStatusListResponse();

        response.statuses.add(new PoliceStatusItem("SAHADA", "Sahada"));
        response.statuses.add(new PoliceStatusItem("MERKEZDE", "Merkezde"));
        response.statuses.add(new PoliceStatusItem("IZINDE", "Izinde"));
        response.statuses.add(new PoliceStatusItem("RAPORLU", "Raporlu"));

        return response;
    }


    @Transactional
    public PoliceSaveResponse PoliceSave(PoliceSaveRequest request) {

        PoliceSaveResponse response = new PoliceSaveResponse();

        if (request == null || !HasText(request.fullName)) {
            response.message = "Ad soyad girilmeli.";
            return response;
        }
        if (!HasText(request.cityId) || cityRepository.findById(request.cityId).isEmpty()) {
            response.message = "Gecerli bir sehir secilmeli.";
            return response;
        }

        Optional<Unit> unit = HasText(request.unitId) ? unitRepository.findById(request.unitId) : Optional.empty();
        if (unit.isEmpty() || !unit.get().cityId.equals(request.cityId)) {
            response.message = "Birim secilen sehre ait olmali.";
            return response;
        }

        Police police;

        if (HasText(request.id)) {
            police = policeRepository.findById(request.id).orElse(null);
            if (police == null) {
                response.message = "Personel bulunamadi.";
                return response;
            }
        } else {
            if (!HasText(request.badgeNumber)) {
                response.message = "Sicil numarasi girilmeli.";
                return response;
            }
            police = new Police();
            police.id = request.cityId + "-" + request.badgeNumber;
            police.dailyTaskCount = 0;
        }

        police.badgeNumber = HasText(request.badgeNumber) ? request.badgeNumber : police.badgeNumber;
        police.fullName = request.fullName.trim();
        police.age = request.age;
        police.rank = request.rank;
        police.score = request.score;
        police.phone = request.phone;
        police.startDate = HasText(request.startDate) ? LocalDate.parse(request.startDate) : police.startDate;
        police.cityId = request.cityId;
        police.unitId = request.unitId;
        police.status = HasText(request.status) ? request.status : "MERKEZDE";
        police.taskType = request.taskType;
        police.dailyTaskLimit = request.dailyTaskLimit == null ? 8 : request.dailyTaskLimit;
        police.photoUrl = "/images/police/" + police.id + ".jpg";

        policeRepository.save(police);

        response.success = true;
        response.id = police.id;
        response.message = "Personel kaydedildi.";

        return response;
    }

    @Transactional
    public PoliceDeleteResponse PoliceDelete(PoliceDeleteRequest request) {

        PoliceDeleteResponse response = new PoliceDeleteResponse();

        if (request == null || !HasText(request.policeId)) {
            response.message = "Personel secilmedi.";
            return response;
        }

        Optional<Police> police = policeRepository.findById(request.policeId);
        if (police.isEmpty()) {
            response.message = "Personel bulunamadi.";
            return response;
        }

        taskRepository.deleteByPoliceId(request.policeId);
        vehicleRepository.deleteByPoliceId(request.policeId);
        policeRepository.delete(police.get());

        response.success = true;
        response.message = "Personel silindi.";

        return response;
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
