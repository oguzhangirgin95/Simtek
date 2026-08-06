package models.analytics.response;

import java.util.ArrayList;
import java.util.List;

public class TaskTrendResponse {

    public String cityName;

    public List<TrendPoint> points = new ArrayList<>();

    public Integer totalTaskCount = 0;

    /** gunluk ortalama gorev sayisi */
    public Integer averageTaskCount = 0;

    public TaskTrendResponse() {
    }
}
