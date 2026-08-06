package models.regions.request;

public class RegionListRequest {
    /** sehir adina gore arama, bos ise tumu doner */
    public String searchText;

    public RegionListRequest() {
    }

    public RegionListRequest(String searchText) {
        this.searchText = searchText;
    }
}
