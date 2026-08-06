package models.personnel.response;

import java.util.ArrayList;
import java.util.List;

public class PoliceListResponse {

    public List<PoliceListItem> policeList = new ArrayList<>();

    /** filtreye uyan toplam kayit sayisi (sayfalamadan once) */
    public Integer totalCount = 0;

    public Integer pageNumber = 0;

    public Integer pageSize = 0;

    public PoliceListResponse() {
    }
}
