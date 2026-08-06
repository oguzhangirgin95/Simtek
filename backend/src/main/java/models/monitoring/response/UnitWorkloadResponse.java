package models.monitoring.response;

import java.util.ArrayList;
import java.util.List;

public class UnitWorkloadResponse {

    public String cityId;

    public String cityName;

    public List<UnitWorkloadItem> units = new ArrayList<>();

    public Integer totalTaskLoad = 0;

    public UnitWorkloadResponse() {
    }
}
