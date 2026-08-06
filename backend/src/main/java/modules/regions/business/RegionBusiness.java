package modules.regions.business;

import java.util.ArrayList;
import java.util.List;

import models.regions.entity.City;
import models.regions.request.RegionListRequest;
import models.regions.response.RegionListItem;
import models.regions.response.RegionListResponse;

public class RegionBusiness {

    /** Sehirler ve harita uzerindeki konumlari. x/y, gercek koordinatlardan 0-100 / 0-60 araligina tasinmistir. */
    private static final List<City> CITIES = new ArrayList<>();

    static {
        CITIES.add(new City("34", "Istanbul", "34", 15.7, 9.9));
        CITIES.add(new City("06", "Ankara", "06", 36.1, 20.7));
        CITIES.add(new City("35", "Izmir", "35", 6.0, 35.8));
        CITIES.add(new City("16", "Bursa", "16", 16.1, 18.1));
        CITIES.add(new City("07", "Antalya", "07", 24.8, 51.0));
        CITIES.add(new City("01", "Adana", "01", 49.1, 50.0));
        CITIES.add(new City("42", "Konya", "42", 34.1, 41.3));
        CITIES.add(new City("27", "Gaziantep", "27", 59.9, 49.3));
        CITIES.add(new City("38", "Kayseri", "38", 49.9, 32.7));
        CITIES.add(new City("55", "Samsun", "55", 54.4, 7.1));
        CITIES.add(new City("61", "Trabzon", "61", 72.2, 10.0));
        CITIES.add(new City("21", "Diyarbakir", "21", 74.9, 40.9));
        CITIES.add(new City("25", "Erzurum", "25", 80.4, 21.0));
        CITIES.add(new City("65", "Van", "65", 91.5, 35.1));
    }

    /** diger moduller sehir listesine buradan ulasir */
    public static List<City> GetCities() {
        return CITIES;
    }

    public static City GetCity(String cityId) {
        for (City city : CITIES) {
            if (city.id.equals(cityId)) {
                return city;
            }
        }
        return null;
    }

    public RegionListResponse RegionList(RegionListRequest regionListRequest) {

        String searchText = regionListRequest == null || regionListRequest.searchText == null
                ? ""
                : regionListRequest.searchText.trim().toLowerCase();

        RegionListResponse regionListResponse = new RegionListResponse();

        for (City city : CITIES) {
            if (!searchText.isEmpty() && !city.name.toLowerCase().contains(searchText)) {
                continue;
            }
            regionListResponse.regions.add(new RegionListItem(city.id, city.name, city.plateCode, city.x, city.y));
        }

        regionListResponse.totalCount = regionListResponse.regions.size();

        return regionListResponse;
    }
}
