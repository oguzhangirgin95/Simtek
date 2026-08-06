package modules.personnel.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.personnel.request.PoliceDetailRequest;
import models.personnel.request.PoliceListRequest;
import models.personnel.request.PoliceStatusListRequest;
import models.personnel.response.PoliceDetailResponse;
import models.personnel.response.PoliceListResponse;
import models.personnel.response.PoliceStatusListResponse;
import modules.personnel.business.PoliceBusiness;


@RestController
@RequestMapping("/police")
public class PoliceController {

    /** Il bazinda polis listesi, sehir / birim / durum filtreleriyle */
    @PostMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public PoliceListResponse PoliceList(@RequestBody PoliceListRequest policeListRequest) {
        PoliceBusiness policeBusiness = new PoliceBusiness();
        return policeBusiness.PoliceList(policeListRequest);
    }

    /** Secilen polisin detayi: yas, rutbe, puan, foto, gorev bilgileri */
    @PostMapping(path = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public PoliceDetailResponse PoliceDetail(@RequestBody PoliceDetailRequest policeDetailRequest) {
        PoliceBusiness policeBusiness = new PoliceBusiness();
        return policeBusiness.PoliceDetail(policeDetailRequest);
    }

    /** Durum filtresinin secenekleri */
    @PostMapping(path = "/statuslist", produces = MediaType.APPLICATION_JSON_VALUE)
    public PoliceStatusListResponse PoliceStatusList(@RequestBody PoliceStatusListRequest policeStatusListRequest) {
        PoliceBusiness policeBusiness = new PoliceBusiness();
        return policeBusiness.PoliceStatusList(policeStatusListRequest);
    }
}
