package modules.regions.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.regions.request.RegionListRequest;
import models.regions.response.RegionListResponse;
import modules.regions.business.RegionBusiness;


@RestController
@RequestMapping("/region")
public class RegionController {

    /** Sehir filtresi ve harita icin sehir listesi */
    @PostMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public RegionListResponse RegionList(@RequestBody RegionListRequest regionListRequest) {
        RegionBusiness regionBusiness = new RegionBusiness();
        return regionBusiness.RegionList(regionListRequest);
    }
}
