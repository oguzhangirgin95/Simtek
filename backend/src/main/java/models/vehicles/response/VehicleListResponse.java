package models.vehicles.response;

import java.util.ArrayList;
import java.util.List;

public class VehicleListResponse {

    public List<VehicleListItem> vehicles = new ArrayList<>();

    public Integer totalCount = 0;

    public Integer pageNumber = 0;

    public Integer pageSize = 0;

    public VehicleListResponse() {
    }
}
