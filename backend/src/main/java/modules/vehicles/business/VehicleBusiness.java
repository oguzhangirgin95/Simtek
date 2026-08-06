package modules.vehicles.business;

import java.util.ArrayList;
import java.util.List;

import models.personnel.entity.Police;
import models.vehicles.entity.Vehicle;
import models.vehicles.request.VehicleDetailRequest;
import models.vehicles.request.VehicleListRequest;
import models.vehicles.request.VehicleTypeListRequest;
import models.vehicles.response.VehicleDetailResponse;
import models.vehicles.response.VehicleListItem;
import models.vehicles.response.VehicleListResponse;
import models.vehicles.response.VehicleTypeItem;
import models.vehicles.response.VehicleTypeListResponse;
import modules.personnel.business.PoliceBusiness;

public class VehicleBusiness {

    private static final List<Vehicle> VEHICLES = new ArrayList<>();

    private static final String[][] CARS = {
            { "Fiat", "Egea" },
            { "Renault", "Megane" },
            { "Ford", "Focus" },
            { "Toyota", "Corolla" },
            { "Volkswagen", "Passat" }
    };

    private static final String[][] MOTORCYCLES = {
            { "Honda", "NC750X" },
            { "BMW", "F850GS" },
            { "Yamaha", "Tracer 900" }
    };

    static {
        int index = 0;

        for (Police police : PoliceBusiness.GetPoliceList()) {

            boolean motorcycle = "MOTOSIKLET".equals(police.taskType);
            String[] vehicleModel = motorcycle
                    ? MOTORCYCLES[index % MOTORCYCLES.length]
                    : CARS[index % CARS.length];

            Vehicle vehicle = new Vehicle();
            vehicle.plate = police.vehiclePlate;
            vehicle.policeId = police.id;
            vehicle.brand = vehicleModel[0];
            vehicle.model = vehicleModel[1];
            vehicle.modelYear = 2016 + (index % 9);
            vehicle.type = motorcycle ? "Motosiklet" : "Otomobil";
            vehicle.kilometers = 15000 + (index * 1350) % 240000;
            vehicle.lastMaintenanceDate = "2026-" + String.format("%02d", 1 + (index % 12)) + "-10";
            vehicle.photoUrl = "/images/vehicle/" + (motorcycle ? "motosiklet" : "otomobil") + "-"
                    + (index % 5 + 1) + ".jpg";
            vehicle.unitName = police.unitName;
            vehicle.cityName = police.cityName;

            VEHICLES.add(vehicle);
            index++;
        }
    }

    public static List<Vehicle> GetVehicles() {
        return VEHICLES;
    }

    /** Arac envanteri: sehir / birim / tip filtreleriyle */
    public VehicleListResponse VehicleList(VehicleListRequest vehicleListRequest) {

        String cityId = vehicleListRequest == null || vehicleListRequest.cityId == null
                ? ""
                : vehicleListRequest.cityId;
        String unitId = vehicleListRequest == null || vehicleListRequest.unitId == null
                ? ""
                : vehicleListRequest.unitId;
        String type = vehicleListRequest == null || vehicleListRequest.type == null ? "" : vehicleListRequest.type;
        String searchText = vehicleListRequest == null || vehicleListRequest.searchText == null
                ? ""
                : vehicleListRequest.searchText.trim().toLowerCase();

        VehicleListResponse vehicleListResponse = new VehicleListResponse();
        List<VehicleListItem> filtered = new ArrayList<>();

        for (Vehicle vehicle : VEHICLES) {

            Police police = PoliceBusiness.GetPolice(vehicle.policeId);
            if (police == null) {
                continue;
            }
            if (!cityId.isEmpty() && !police.cityId.equals(cityId)) {
                continue;
            }
            if (!unitId.isEmpty() && !police.unitId.equals(unitId)) {
                continue;
            }
            if (!type.isEmpty() && !vehicle.type.equals(type)) {
                continue;
            }
            if (!searchText.isEmpty()
                    && !vehicle.plate.toLowerCase().contains(searchText)
                    && !vehicle.brand.toLowerCase().contains(searchText)
                    && !vehicle.model.toLowerCase().contains(searchText)) {
                continue;
            }

            VehicleListItem item = new VehicleListItem();
            item.plate = vehicle.plate;
            item.policeId = vehicle.policeId;
            item.policeName = police.fullName;
            item.brand = vehicle.brand;
            item.model = vehicle.model;
            item.modelYear = vehicle.modelYear;
            item.type = vehicle.type;
            item.kilometers = vehicle.kilometers;
            item.cityName = vehicle.cityName;
            item.unitName = vehicle.unitName;
            item.photoUrl = vehicle.photoUrl;

            filtered.add(item);
        }

        vehicleListResponse.totalCount = filtered.size();

        int pageNumber = vehicleListRequest == null || vehicleListRequest.pageNumber == null
                ? 0
                : vehicleListRequest.pageNumber;
        int pageSize = vehicleListRequest == null || vehicleListRequest.pageSize == null
                ? 0
                : vehicleListRequest.pageSize;

        if (pageNumber < 1 || pageSize < 1) {
            vehicleListResponse.vehicles = filtered;
            return vehicleListResponse;
        }

        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, filtered.size());

        if (fromIndex < filtered.size()) {
            vehicleListResponse.vehicles = new ArrayList<>(filtered.subList(fromIndex, toIndex));
        }

        vehicleListResponse.pageNumber = pageNumber;
        vehicleListResponse.pageSize = pageSize;

        return vehicleListResponse;
    }

    /** Arac tipi filtresinin secenekleri, yanlarinda adet ile */
    public VehicleTypeListResponse VehicleTypeList(VehicleTypeListRequest vehicleTypeListRequest) {

        int carCount = 0;
        int motorcycleCount = 0;

        for (Vehicle vehicle : VEHICLES) {
            if ("Motosiklet".equals(vehicle.type)) {
                motorcycleCount++;
            } else {
                carCount++;
            }
        }

        VehicleTypeListResponse vehicleTypeListResponse = new VehicleTypeListResponse();
        vehicleTypeListResponse.types.add(new VehicleTypeItem("Otomobil", "Otomobil", carCount));
        vehicleTypeListResponse.types.add(new VehicleTypeItem("Motosiklet", "Motosiklet", motorcycleCount));

        return vehicleTypeListResponse;
    }

    /** Secilen polisin araci */
    public VehicleDetailResponse VehicleDetail(VehicleDetailRequest vehicleDetailRequest) {

        VehicleDetailResponse vehicleDetailResponse = new VehicleDetailResponse();

        if (vehicleDetailRequest == null) {
            return vehicleDetailResponse;
        }

        String policeId = vehicleDetailRequest.policeId == null ? "" : vehicleDetailRequest.policeId;
        String plate = vehicleDetailRequest.plate == null ? "" : vehicleDetailRequest.plate;

        for (Vehicle vehicle : VEHICLES) {

            boolean policeMatched = !policeId.isEmpty() && vehicle.policeId.equals(policeId);
            boolean plateMatched = !plate.isEmpty() && vehicle.plate.equals(plate);

            if (!policeMatched && !plateMatched) {
                continue;
            }

            vehicleDetailResponse.found = true;
            vehicleDetailResponse.plate = vehicle.plate;
            vehicleDetailResponse.policeId = vehicle.policeId;
            vehicleDetailResponse.brand = vehicle.brand;
            vehicleDetailResponse.model = vehicle.model;
            vehicleDetailResponse.modelYear = vehicle.modelYear;
            vehicleDetailResponse.type = vehicle.type;
            vehicleDetailResponse.kilometers = vehicle.kilometers;
            vehicleDetailResponse.lastMaintenanceDate = vehicle.lastMaintenanceDate;
            vehicleDetailResponse.photoUrl = vehicle.photoUrl;
            vehicleDetailResponse.unitName = vehicle.unitName;
            vehicleDetailResponse.cityName = vehicle.cityName;
            break;
        }

        return vehicleDetailResponse;
    }
}
