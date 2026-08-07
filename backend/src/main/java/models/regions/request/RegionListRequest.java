package models.regions.request;

public class RegionListRequest {
    public String searchText;

    public RegionListRequest() {
    }

    public RegionListRequest(String searchText) {
        this.searchText = searchText;
    }
}
