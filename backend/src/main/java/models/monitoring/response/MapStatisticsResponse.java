package models.monitoring.response;

import java.util.ArrayList;
import java.util.List;

public class MapStatisticsResponse {

    public List<CityStatistic> cities = new ArrayList<>();

    public Integer totalPolice = 0;

    public Integer activePolice = 0;

    /** en yogun sehir (en cok aktif memur) */
    public String busiestCityName;

    public Integer busiestCityActivePolice = 0;

    public MapStatisticsResponse() {
    }
}
