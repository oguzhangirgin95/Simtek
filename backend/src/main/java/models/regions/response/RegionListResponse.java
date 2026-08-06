package models.regions.response;

import java.util.ArrayList;
import java.util.List;

public class RegionListResponse {

    public List<RegionListItem> regions = new ArrayList<>();

    public Integer totalCount = 0;

    public RegionListResponse() {
    }
}
