package modules.personnel.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.personnel.request.PoliceDeleteRequest;
import models.personnel.request.PoliceDetailRequest;
import models.personnel.request.PoliceListRequest;
import models.personnel.request.PoliceSaveRequest;
import models.personnel.request.PoliceStatusListRequest;
import models.personnel.response.PoliceDeleteResponse;
import models.personnel.response.PoliceDetailResponse;
import models.personnel.response.PoliceListResponse;
import models.personnel.response.PoliceSaveResponse;
import models.personnel.response.PoliceStatusListResponse;
import modules.personnel.business.PoliceBusiness;


@RestController
@RequestMapping("/police")
public class PoliceController {

    private final PoliceBusiness policeBusiness;

    public PoliceController(PoliceBusiness policeBusiness) {
        this.policeBusiness = policeBusiness;
    }

    /** Il bazinda polis listesi, sehir / birim / durum filtreleriyle */
    @PostMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public PoliceListResponse PoliceList(@RequestBody PoliceListRequest policeListRequest) {
        return policeBusiness.PoliceList(policeListRequest);
    }

    /** Secilen polisin detayi */
    @PostMapping(path = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public PoliceDetailResponse PoliceDetail(@RequestBody PoliceDetailRequest policeDetailRequest) {
        return policeBusiness.PoliceDetail(policeDetailRequest);
    }

    /** Durum filtresinin secenekleri */
    @PostMapping(path = "/statuslist", produces = MediaType.APPLICATION_JSON_VALUE)
    public PoliceStatusListResponse PoliceStatusList(@RequestBody PoliceStatusListRequest policeStatusListRequest) {
        return policeBusiness.PoliceStatusList(policeStatusListRequest);
    }

    /** Personel ekleme / guncelleme */
    @PostMapping(path = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public PoliceSaveResponse PoliceSave(@RequestBody PoliceSaveRequest policeSaveRequest) {
        return policeBusiness.PoliceSave(policeSaveRequest);
    }

    /** Personel silme */
    @PostMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public PoliceDeleteResponse PoliceDelete(@RequestBody PoliceDeleteRequest policeDeleteRequest) {
        return policeBusiness.PoliceDelete(policeDeleteRequest);
    }
}
