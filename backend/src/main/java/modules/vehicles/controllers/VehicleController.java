package modules.vehicles.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.vehicles.request.VehicleDeleteRequest;
import models.vehicles.request.VehicleDetailRequest;
import models.vehicles.request.VehicleListRequest;
import models.vehicles.request.VehicleSaveRequest;
import models.vehicles.request.VehicleTypeListRequest;
import models.vehicles.response.VehicleDeleteResponse;
import models.vehicles.response.VehicleDetailResponse;
import models.vehicles.response.VehicleListResponse;
import models.vehicles.response.VehicleSaveResponse;
import models.vehicles.response.VehicleTypeListResponse;
import modules.vehicles.business.VehicleBusiness;


@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleBusiness vehicleBusiness;

    public VehicleController(VehicleBusiness vehicleBusiness) {
        this.vehicleBusiness = vehicleBusiness;
    }

    /** Arac envanteri */
    @PostMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleListResponse VehicleList(@RequestBody VehicleListRequest vehicleListRequest) {
        return vehicleBusiness.VehicleList(vehicleListRequest);
    }

    /** Secilen polisin arac bilgileri */
    @PostMapping(path = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleDetailResponse VehicleDetail(@RequestBody VehicleDetailRequest vehicleDetailRequest) {
        return vehicleBusiness.VehicleDetail(vehicleDetailRequest);
    }

    /** Arac tipi filtresinin secenekleri */
    @PostMapping(path = "/typelist", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleTypeListResponse VehicleTypeList(@RequestBody VehicleTypeListRequest vehicleTypeListRequest) {
        return vehicleBusiness.VehicleTypeList(vehicleTypeListRequest);
    }

    /** Arac ekleme / guncelleme */
    @PostMapping(path = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleSaveResponse VehicleSave(@RequestBody VehicleSaveRequest vehicleSaveRequest) {
        return vehicleBusiness.VehicleSave(vehicleSaveRequest);
    }

    /** Arac silme */
    @PostMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleDeleteResponse VehicleDelete(@RequestBody VehicleDeleteRequest vehicleDeleteRequest) {
        return vehicleBusiness.VehicleDelete(vehicleDeleteRequest);
    }
}
