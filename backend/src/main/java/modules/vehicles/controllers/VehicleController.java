package modules.vehicles.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.vehicles.request.VehicleDetailRequest;
import models.vehicles.request.VehicleListRequest;
import models.vehicles.request.VehicleTypeListRequest;
import models.vehicles.response.VehicleDetailResponse;
import models.vehicles.response.VehicleListResponse;
import models.vehicles.response.VehicleTypeListResponse;
import modules.vehicles.business.VehicleBusiness;


@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    /** Arac envanteri, sehir / birim / tip filtreleriyle */
    @PostMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleListResponse VehicleList(@RequestBody VehicleListRequest vehicleListRequest) {
        VehicleBusiness vehicleBusiness = new VehicleBusiness();
        return vehicleBusiness.VehicleList(vehicleListRequest);
    }

    /** Arac tipi filtresinin secenekleri */
    @PostMapping(path = "/typelist", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleTypeListResponse VehicleTypeList(@RequestBody VehicleTypeListRequest vehicleTypeListRequest) {
        VehicleBusiness vehicleBusiness = new VehicleBusiness();
        return vehicleBusiness.VehicleTypeList(vehicleTypeListRequest);
    }

    /** Secilen polisin arac bilgileri ve arac fotosu */
    @PostMapping(path = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleDetailResponse VehicleDetail(@RequestBody VehicleDetailRequest vehicleDetailRequest) {
        VehicleBusiness vehicleBusiness = new VehicleBusiness();
        return vehicleBusiness.VehicleDetail(vehicleDetailRequest);
    }
}
