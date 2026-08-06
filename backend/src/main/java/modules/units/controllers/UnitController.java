package modules.units.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.units.request.UnitDeleteRequest;
import models.units.request.UnitListRequest;
import models.units.request.UnitSaveRequest;
import models.units.response.UnitDeleteResponse;
import models.units.response.UnitListResponse;
import models.units.response.UnitSaveResponse;
import modules.units.business.UnitBusiness;


@RestController
@RequestMapping("/unit")
public class UnitController {

    private final UnitBusiness unitBusiness;

    public UnitController(UnitBusiness unitBusiness) {
        this.unitBusiness = unitBusiness;
    }

    /** Birim filtresi icin birim listesi */
    @PostMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public UnitListResponse UnitList(@RequestBody UnitListRequest unitListRequest) {
        return unitBusiness.UnitList(unitListRequest);
    }

    /** Birim ekleme / guncelleme */
    @PostMapping(path = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public UnitSaveResponse UnitSave(@RequestBody UnitSaveRequest unitSaveRequest) {
        return unitBusiness.UnitSave(unitSaveRequest);
    }

    /** Birim silme */
    @PostMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public UnitDeleteResponse UnitDelete(@RequestBody UnitDeleteRequest unitDeleteRequest) {
        return unitBusiness.UnitDelete(unitDeleteRequest);
    }
}
