package modules.units.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.units.request.UnitListRequest;
import models.units.response.UnitListResponse;
import modules.units.business.UnitBusiness;


@RestController
@RequestMapping("/unit")
public class UnitController {

    /** Birim filtresi icin birim listesi, cityId verilirse o sehrin birimleri */
    @PostMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public UnitListResponse UnitList(@RequestBody UnitListRequest unitListRequest) {
        UnitBusiness unitBusiness = new UnitBusiness();
        return unitBusiness.UnitList(unitListRequest);
    }
}
