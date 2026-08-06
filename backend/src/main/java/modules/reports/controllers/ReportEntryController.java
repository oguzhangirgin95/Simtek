package modules.reports.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.reports.request.ReportEntryRequest;
import models.reports.response.ReportEntryConfirmResponse;
import models.reports.response.ReportEntryExecuteResponse;
import modules.reports.business.ReportEntryBusiness;


@RestController
@RequestMapping("/reportentry")
public class ReportEntryController {

    @PostMapping(path = "/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReportEntryConfirmResponse Confirm(@RequestBody ReportEntryRequest reportEntryRequest) {
        ReportEntryBusiness reportEntryBusiness = new ReportEntryBusiness();
        return reportEntryBusiness.Confirm(reportEntryRequest);
    }

    @PostMapping(path = "/execute", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReportEntryExecuteResponse Execute(@RequestBody ReportEntryRequest reportEntryRequest) {
        ReportEntryBusiness reportEntryBusiness = new ReportEntryBusiness();
        return reportEntryBusiness.Execute(reportEntryRequest);
    }
}
