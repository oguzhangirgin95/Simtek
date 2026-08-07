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

    private final ReportEntryBusiness reportEntryBusiness;

    public ReportEntryController(ReportEntryBusiness reportEntryBusiness) {
        this.reportEntryBusiness = reportEntryBusiness;
    }

    @PostMapping(path = "/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReportEntryConfirmResponse Confirm(@RequestBody ReportEntryRequest reportEntryRequest) {
        return reportEntryBusiness.Confirm(reportEntryRequest);
    }

    @PostMapping(path = "/execute", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReportEntryExecuteResponse Execute(@RequestBody ReportEntryRequest reportEntryRequest) {
        return reportEntryBusiness.Execute(reportEntryRequest);
    }

    @PostMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReportListResponse ReportList(@RequestBody ReportListRequest reportListRequest) {
        return reportEntryBusiness.ReportList(reportListRequest);
    }

    @PostMapping(path = "/typelist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReportTypeListResponse ReportTypeList(@RequestBody ReportTypeListRequest reportTypeListRequest) {
        return reportEntryBusiness.ReportTypeList(reportTypeListRequest);
    }
}
