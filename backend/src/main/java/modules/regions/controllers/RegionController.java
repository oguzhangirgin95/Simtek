package modules.regions.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.regions.request.RegionDeleteRequest;
import models.regions.request.RegionListRequest;
import models.regions.request.RegionSaveRequest;
import models.regions.response.RegionDeleteResponse;
import models.regions.response.RegionListResponse;
import models.regions.response.RegionSaveResponse;
import modules.regions.business.RegionBusiness;


@RestController
@RequestMapping("/region")
public class RegionController {

    private final RegionBusiness regionBusiness;

    public RegionController(RegionBusiness regionBusiness) {
        this.regionBusiness = regionBusiness;
    }

    @PostMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public RegionListResponse RegionList(@RequestBody RegionListRequest regionListRequest) {
        return regionBusiness.RegionList(regionListRequest);
    }

    @PostMapping(path = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public RegionSaveResponse RegionSave(@RequestBody RegionSaveRequest regionSaveRequest) {
        return regionBusiness.RegionSave(regionSaveRequest);
    }

    @PostMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public RegionDeleteResponse RegionDelete(@RequestBody RegionDeleteRequest regionDeleteRequest) {
        return regionBusiness.RegionDelete(regionDeleteRequest);
    }
}
