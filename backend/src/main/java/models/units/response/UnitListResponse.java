package models.units.response;

import java.util.ArrayList;
import java.util.List;

public class UnitListResponse {

    public List<UnitListItem> units = new ArrayList<>();

    public Integer totalCount = 0;

    public UnitListResponse() {
    }
}
