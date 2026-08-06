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

    /** Harita: sehir bazli toplam ve aktif memur sayilari, en yogun sehir */
    @PostMapping(path = "/mapstatistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public MapStatisticsResponse MapStatistics(@RequestBody DashboardRequest dashboardRequest) {
        DashboardBusiness dashboardBusiness = new DashboardBusiness();
        return dashboardBusiness.MapStatistics(dashboardRequest);
    }

    /** First-sight bilgiler: sahada, merkezde, izinde, raporlu, limit asan */
    @PostMapping(path = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public SummaryResponse DashboardSummary(@RequestBody DashboardRequest dashboardRequest) {
        DashboardBusiness dashboardBusiness = new DashboardBusiness();
        return dashboardBusiness.Summary(dashboardRequest);
    }

    /** Grafik: birim bazli gorev yogunlugu (varsayilan Ankara) */
    @PostMapping(path = "/unitworkload", produces = MediaType.APPLICATION_JSON_VALUE)
    public UnitWorkloadResponse UnitWorkload(@RequestBody DashboardRequest dashboardRequest) {
        DashboardBusiness dashboardBusiness = new DashboardBusiness();
        return dashboardBusiness.UnitWorkload(dashboardRequest);
    }
}
