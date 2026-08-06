package modules.monitoring.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.monitoring.request.DashboardRequest;
import models.monitoring.response.MapStatisticsResponse;
import models.monitoring.response.SummaryResponse;
import models.monitoring.response.UnitWorkloadResponse;
import modules.monitoring.business.DashboardBusiness;


@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardBusiness dashboardBusiness;

    public DashboardController(DashboardBusiness dashboardBusiness) {
        this.dashboardBusiness = dashboardBusiness;
    }

    /** Harita: sehir bazli toplam ve aktif memur sayilari */
    @PostMapping(path = "/mapstatistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public MapStatisticsResponse MapStatistics(@RequestBody DashboardRequest dashboardRequest) {
        return dashboardBusiness.MapStatistics(dashboardRequest);
    }

    /** First-sight bilgiler */
    @PostMapping(path = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public SummaryResponse DashboardSummary(@RequestBody DashboardRequest dashboardRequest) {
        return dashboardBusiness.Summary(dashboardRequest);
    }

    /** Grafik: birim bazli gorev yogunlugu */
    @PostMapping(path = "/unitworkload", produces = MediaType.APPLICATION_JSON_VALUE)
    public UnitWorkloadResponse UnitWorkload(@RequestBody DashboardRequest dashboardRequest) {
        return dashboardBusiness.UnitWorkload(dashboardRequest);
    }
}
