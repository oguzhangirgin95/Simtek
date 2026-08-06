package modules.reports.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.reports.request.ReportEntryRequest;
import models.reports.request.ReportListRequest;
import models.reports.request.ReportTypeListRequest;
import models.reports.response.ReportEntryConfirmResponse;
import models.reports.response.ReportEntryExecuteResponse;
import models.reports.response.ReportListResponse;
import models.reports.response.ReportTypeListResponse;
import modules.reports.business.ReportEntryBusiness;


@RestController
@RequestMapping("/reportentry")
public class ReportEntryController {

    /** Rapor onay adimi: kapsam ozeti */
    @PostMapping(path = "/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReportEntryConfirmResponse Confirm(@RequestBody ReportEntryRequest reportEntryRequest) {
        ReportEntryBusiness reportEntryBusiness = new ReportEntryBusiness();
        return reportEntryBusiness.Confirm(reportEntryRequest);
    }

    /** Rapor olusturma adimi */
    @PostMapping(path = "/execute", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReportEntryExecuteResponse Execute(@RequestBody ReportEntryRequest reportEntryRequest) {
        ReportEntryBusiness reportEntryBusiness = new ReportEntryBusiness();
        return reportEntryBusiness.Execute(reportEntryRequest);
    }

    /** Olusturulmus raporlar */
    @PostMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReportListResponse ReportList(@RequestBody ReportListRequest reportListRequest) {
        ReportEntryBusiness reportEntryBusiness = new ReportEntryBusiness();
        return reportEntryBusiness.ReportList(reportListRequest);
    }

    /** Rapor tipi secenekleri */
    @PostMapping(path = "/typelist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReportTypeListResponse ReportTypeList(@RequestBody ReportTypeListRequest reportTypeListRequest) {
        ReportEntryBusiness reportEntryBusiness = new ReportEntryBusiness();
        return reportEntryBusiness.ReportTypeList(reportTypeListRequest);
    }
}
