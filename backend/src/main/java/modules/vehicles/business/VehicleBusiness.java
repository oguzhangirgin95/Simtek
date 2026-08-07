package modules.vehicles.business;

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
import models.regions.entity.City;
import models.units.entity.Unit;
import models.vehicles.entity.Vehicle;
import models.vehicles.request.VehicleDeleteRequest;
import models.vehicles.request.VehicleDetailRequest;
import models.vehicles.request.VehicleListRequest;
import models.vehicles.request.VehicleSaveRequest;
import models.vehicles.request.VehicleTypeListRequest;
import models.vehicles.response.VehicleDeleteResponse;
import models.vehicles.response.VehicleDetailResponse;
import models.vehicles.response.VehicleListItem;
import models.vehicles.response.VehicleListResponse;
import models.vehicles.response.VehicleSaveResponse;
import models.vehicles.response.VehicleTypeItem;
import models.vehicles.response.VehicleTypeListResponse;
import modules.personnel.repositories.PoliceRepository;
import modules.regions.repositories.CityRepository;
import modules.units.repositories.UnitRepository;
import modules.vehicles.repositories.VehicleRepository;
import modules.vehicles.repositories.VehicleTypeCountProjection;

@Service
public class VehicleBusiness {

    private final VehicleRepository vehicleRepository;
    private final PoliceRepository policeRepository;
    private final CityRepository cityRepository;
    private final UnitRepository unitRepository;

    public VehicleBusiness(VehicleRepository vehicleRepository, PoliceRepository policeRepository,
            CityRepository cityRepository, UnitRepository unitRepository) {
        this.vehicleRepository = vehicleRepository;
        this.policeRepository = policeRepository;
        this.cityRepository = cityRepository;
        this.unitRepository = unitRepository;
    }

    @Transactional(readOnly = true)
    public VehicleListResponse VehicleList(VehicleListRequest vehicleListRequest) {

        VehicleListRequest request = vehicleListRequest == null ? new VehicleListRequest() : vehicleListRequest;

        List<String> policeIds = null;
        if (HasText(request.cityId) || HasText(request.unitId)) {
            policeIds = new ArrayList<>();
            for (Police police : policeRepository.findAll(BuildPoliceSpecification(request))) {
                policeIds.add(police.id);
            }
        }

        Specification<Vehicle> specification = BuildSpecification(request, policeIds);
        Sort sort = Sort.by(Sort.Direction.ASC, "plate");

        VehicleListResponse response = new VehicleListResponse();
        List<Vehicle> vehicles;

        int pageNumber = request.pageNumber == null ? 0 : request.pageNumber;
        int pageSize = request.pageSize == null ? 0 : request.pageSize;

        if (pageNumber > 0 && pageSize > 0) {
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
            Page<Vehicle> page = vehicleRepository.findAll(specification, pageable);

            vehicles = page.getContent();
            response.totalCount = (int) page.getTotalElements();
            response.pageNumber = pageNumber;
            response.pageSize = pageSize;
        } else {
            vehicles = vehicleRepository.findAll(specification, sort);
            response.totalCount = vehicles.size();
        }

        Map<String, Police> policeMap = GetPoliceMap();
        Map<String, String> cityNames = GetCityNames();
        Map<String, String> unitNames = GetUnitNames();

        for (Vehicle vehicle : vehicles) {
            Police police = policeMap.get(vehicle.policeId);

            VehicleListItem item = new VehicleListItem();
            item.plate = vehicle.plate;
            item.policeId = vehicle.policeId;
            item.policeName = police == null ? "" : police.fullName;
            item.brand = vehicle.brand;
            item.model = vehicle.model;
            item.modelYear = vehicle.modelYear;
            item.type = vehicle.type;
            item.kilometers = vehicle.kilometers;
            item.cityName = police == null ? "" : cityNames.getOrDefault(police.cityId, "");
            item.unitName = police == null ? "" : unitNames.getOrDefault(police.unitId, "");
            item.photoUrl = vehicle.photoUrl;

            response.vehicles.add(item);
        }

        return response;
    }

    private Specification<Police> BuildPoliceSpecification(VehicleListRequest request) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (HasText(request.cityId)) {
                predicates.add(builder.equal(root.get("cityId"), request.cityId));
            }
            if (HasText(request.unitId)) {
                predicates.add(builder.equal(root.get("unitId"), request.unitId));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<Vehicle> BuildSpecification(VehicleListRequest request, List<String> policeIds) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (policeIds != null) {
                if (policeIds.isEmpty()) {
                    return builder.disjunction();
                }
                predicates.add(root.get("policeId").in(policeIds));
            }
            if (HasText(request.type)) {
                predicates.add(builder.equal(root.get("type"), request.type));
            }
            if (HasText(request.searchText)) {
                String pattern = "%" + request.searchText.trim().toLowerCase() + "%";
                predicates.add(builder.or(
                        builder.like(builder.lower(root.get("plate")), pattern),
                        builder.like(builder.lower(root.get("brand")), pattern),
                        builder.like(builder.lower(root.get("model")), pattern)));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional(readOnly = true)
    public VehicleDetailResponse VehicleDetail(VehicleDetailRequest vehicleDetailRequest) {

        VehicleDetailResponse response = new VehicleDetailResponse();

        if (vehicleDetailRequest == null) {
            return response;
        }

        Optional<Vehicle> found = HasText(vehicleDetailRequest.plate)
                ? vehicleRepository.findById(vehicleDetailRequest.plate)
                : HasText(vehicleDetailRequest.policeId)
                        ? vehicleRepository.findByPoliceId(vehicleDetailRequest.policeId)
                        : Optional.empty();

        if (found.isEmpty()) {
            return response;
        }

        Vehicle vehicle = found.get();
        Police police = vehicle.policeId == null ? null : policeRepository.findById(vehicle.policeId).orElse(null);

        response.found = true;
        response.plate = vehicle.plate;
        response.policeId = vehicle.policeId;
        response.brand = vehicle.brand;
        response.model = vehicle.model;
        response.modelYear = vehicle.modelYear;
        response.type = vehicle.type;
        response.kilometers = vehicle.kilometers;
        response.lastMaintenanceDate = vehicle.lastMaintenanceDate == null
                ? null
                : vehicle.lastMaintenanceDate.toString();
        response.photoUrl = vehicle.photoUrl;
        response.cityName = police == null ? "" : GetCityNames().getOrDefault(police.cityId, "");
        response.unitName = police == null ? "" : GetUnitNames().getOrDefault(police.unitId, "");

        return response;
    }

    @Transactional(readOnly = true)
    public VehicleTypeListResponse VehicleTypeList(VehicleTypeListRequest vehicleTypeListRequest) {

        VehicleTypeListResponse response = new VehicleTypeListResponse();

        for (VehicleTypeCountProjection projection : vehicleRepository.typeCounts()) {
            response.types.add(new VehicleTypeItem(projection.getVehicleType(), projection.getVehicleType(),
                    projection.getTypeCount().intValue()));
        }

        return response;
    }


    @Transactional
    public VehicleSaveResponse VehicleSave(VehicleSaveRequest request) {

        VehicleSaveResponse response = new VehicleSaveResponse();

        if (request == null || !HasText(request.plate)) {
            response.message = "Plaka girilmeli.";
            return response;
        }
        if (HasText(request.policeId) && policeRepository.findById(request.policeId).isEmpty()) {
            response.message = "Personel bulunamadi.";
            return response;
        }

        Vehicle vehicle = vehicleRepository.findById(request.plate.trim()).orElseGet(Vehicle::new);
        vehicle.plate = request.plate.trim();
        vehicle.policeId = request.policeId;
        vehicle.brand = request.brand;
        vehicle.model = request.model;
        vehicle.modelYear = request.modelYear;
        vehicle.type = HasText(request.type) ? request.type : "Otomobil";
        vehicle.kilometers = request.kilometers;
        vehicle.lastMaintenanceDate = HasText(request.lastMaintenanceDate)
                ? LocalDate.parse(request.lastMaintenanceDate)
                : vehicle.lastMaintenanceDate;
        vehicle.photoUrl = "/images/vehicle/" + ("Motosiklet".equals(vehicle.type) ? "motosiklet" : "otomobil")
                + "-1.jpg";

        vehicleRepository.save(vehicle);

        response.success = true;
        response.plate = vehicle.plate;
        response.message = "Arac kaydedildi.";

        return response;
    }

    @Transactional
    public VehicleDeleteResponse VehicleDelete(VehicleDeleteRequest request) {

        VehicleDeleteResponse response = new VehicleDeleteResponse();

        if (request == null || !HasText(request.plate)) {
            response.message = "Arac secilmedi.";
            return response;
        }

        Optional<Vehicle> vehicle = vehicleRepository.findById(request.plate);
        if (vehicle.isEmpty()) {
            response.message = "Arac bulunamadi.";
            return response;
        }

        vehicleRepository.delete(vehicle.get());

        response.success = true;
        response.message = "Arac silindi.";

        return response;
    }


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
