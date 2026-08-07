package modules.analytics.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.analytics.request.TaskTrendRequest;
import models.analytics.response.TaskTrendResponse;
import modules.analytics.business.AnalyticsBusiness;


@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsBusiness analyticsBusiness;

    public AnalyticsController(AnalyticsBusiness analyticsBusiness) {
        this.analyticsBusiness = analyticsBusiness;
    }

    @PostMapping(path = "/tasktrend", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskTrendResponse TaskTrend(@RequestBody TaskTrendRequest taskTrendRequest) {
        return analyticsBusiness.TaskTrend(taskTrendRequest);
    }
}
