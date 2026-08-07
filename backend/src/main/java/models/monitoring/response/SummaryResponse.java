package models.monitoring.response;

import java.util.ArrayList;
import java.util.List;

public class SummaryResponse {

    public Integer totalPolice = 0;

    public Integer onDuty = 0;

    public Integer atStation = 0;

    public Integer onLeave = 0;

    public Integer onReport = 0;

    public Integer overDailyLimit = 0;

    public Integer unitCount = 0;

    public List<StatusCount> statusDistribution = new ArrayList<>();

    public SummaryResponse() {
    }
}
