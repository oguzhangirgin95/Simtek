package modules.units.business;

import java.util.ArrayList;
import java.util.List;

import models.regions.entity.City;
import models.units.entity.Unit;
import models.units.request.UnitListRequest;
import models.units.response.UnitListItem;
import models.units.response.UnitListResponse;
import modules.regions.business.RegionBusiness;

public class UnitBusiness {

    private static final List<Unit> UNITS = new ArrayList<>();

    /** Ankara'nin birimleri isim isim tanimli, diger sehirler standart birimlerle acilir */
    private static final String[] ANKARA_UNITS = {
            "Cankaya Trafik Denetleme",
            "Kecioren Trafik Denetleme",
            "Yenimahalle Trafik Denetleme",
            "Etimesgut Trafik Denetleme",
            "Mamak Trafik Denetleme",
            "Bolge Trafik Denetleme"
    };

    private static final String[] STANDART_UNITS = {
            "Merkez Trafik Denetleme",
            "Bolge Trafik Denetleme",
            "Otoyol Denetleme"
    };

    static {
        for (City city : RegionBusiness.GetCities()) {

            String[] unitNames = city.id.equals("06") ? ANKARA_UNITS : STANDART_UNITS;

            for (int i = 0; i < unitNames.length; i++) {
                String unitId = city.id + "-B" + (i + 1);
                UNITS.add(new Unit(unitId, unitNames[i], city.id, city.name));
            }
        }
    }

    /** diger moduller birim listesine buradan ulasir */
    public static List<Unit> GetUnits() {
        return UNITS;
    }

    public static List<Unit> GetUnitsByCity(String cityId) {
        List<Unit> cityUnits = new ArrayList<>();
        for (Unit unit : UNITS) {
            if (unit.cityId.equals(cityId)) {
                cityUnits.add(unit);
            }
        }
        return cityUnits;
    }

    public UnitListResponse UnitList(UnitListRequest unitListRequest) {

        String cityId = unitListRequest == null || unitListRequest.cityId == null ? "" : unitListRequest.cityId;

        UnitListResponse unitListResponse = new UnitListResponse();

        for (Unit unit : UNITS) {
            if (!cityId.isEmpty() && !unit.cityId.equals(cityId)) {
                continue;
            }
            unitListResponse.units.add(new UnitListItem(unit.id, unit.name, unit.cityId, unit.cityName));
        }

        unitListResponse.totalCount = unitListResponse.units.size();

        return unitListResponse;
    }
}
