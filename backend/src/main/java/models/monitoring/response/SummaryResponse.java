package models.monitoring.response;

import java.util.ArrayList;
import java.util.List;

public class SummaryResponse {

    public Integer totalPolice = 0;

    /** sahada */
    public Integer onDuty = 0;

    /** merkezde */
    public Integer atStation = 0;

    /** izinde */
    public Integer onLeave = 0;

    /** raporlu */
    public Integer onReport = 0;

    /** gunluk gorev limitini asan memur sayisi */
    public Integer overDailyLimit = 0;

    public Integer unitCount = 0;

    /** donut grafik icin durum dagilimi */
    public List<StatusCount> statusDistribution = new ArrayList<>();

    public SummaryResponse() {
    }
}
