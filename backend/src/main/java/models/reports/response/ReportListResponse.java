package models.reports.response;

import java.util.ArrayList;
import java.util.List;

public class ReportListResponse {

    public List<ReportListItem> reports = new ArrayList<>();

    public Integer totalCount = 0;

    public ReportListResponse() {
    }
}
