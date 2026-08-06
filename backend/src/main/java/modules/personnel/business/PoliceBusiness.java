package modules.personnel.business;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.personnel.entity.Police;
import models.personnel.request.PoliceDetailRequest;
import models.personnel.request.PoliceListRequest;
import models.personnel.request.PoliceStatusListRequest;
import models.personnel.response.PoliceDetailResponse;
import models.personnel.response.PoliceListItem;
import models.personnel.response.PoliceListResponse;
import models.personnel.response.PoliceStatusItem;
import models.personnel.response.PoliceStatusListResponse;
import models.regions.entity.City;
import models.units.entity.Unit;
import modules.regions.business.RegionBusiness;
import modules.units.business.UnitBusiness;

public class PoliceBusiness {

    private static final List<Police> POLICE_LIST = new ArrayList<>();

    private static final String[] FIRST_NAMES = {
            "Ahmet", "Mehmet", "Mustafa", "Ali", "Hasan", "Huseyin", "Emre", "Burak",
            "Elif", "Zeynep", "Ayse", "Fatma", "Merve", "Selin", "Kemal", "Onur"
    };

    private static final String[] LAST_NAMES = {
            "Yilmaz", "Kaya", "Demir", "Sahin", "Celik", "Yildiz", "Aydin", "Ozturk",
            "Arslan", "Dogan", "Kilic", "Aslan"
    };

    private static final String[] RANKS = {
            "Polis Memuru", "Kidemli Polis Memuru", "Basspolis Memuru",
            "Komiser Yardimcisi", "Komiser", "Baskomiser"
    };

    /**
     * Dagilim: yaklasik yarisi sahada, kalani merkez/izin/rapor.
     * Dizi uzunlugu 7, birim sayilariyla (3 ve 6) ortak boleni yok; boylece
     * her birime her durumdan memur dusuyor.
     */
    private static final String[] STATUS_ORDER = {
            "SAHADA", "SAHADA", "SAHADA", "SAHADA", "MERKEZDE", "IZINDE", "RAPORLU"
    };

    private static final String[] TASK_TYPES = {
            "DEVRIYE", "RADAR", "MOTOSIKLET", "OKUL_GECIDI", "KAZA_INCELEME"
    };

    private static final Map<String, Integer> POLICE_COUNTS = new HashMap<>();

    private static final Integer DAILY_TASK_LIMIT = 8;

    static {
        POLICE_COUNTS.put("34", 48);
        POLICE_COUNTS.put("06", 36);
        POLICE_COUNTS.put("35", 30);
        POLICE_COUNTS.put("16", 21);
        POLICE_COUNTS.put("07", 24);
        POLICE_COUNTS.put("01", 18);
        POLICE_COUNTS.put("42", 15);
        POLICE_COUNTS.put("27", 18);
        POLICE_COUNTS.put("38", 12);
        POLICE_COUNTS.put("55", 12);
        POLICE_COUNTS.put("61", 9);
        POLICE_COUNTS.put("21", 15);
        POLICE_COUNTS.put("25", 9);
        POLICE_COUNTS.put("65", 9);

        int index = 0;

        for (City city : RegionBusiness.GetCities()) {

            List<Unit> cityUnits = UnitBusiness.GetUnitsByCity(city.id);
            int count = POLICE_COUNTS.getOrDefault(city.id, 12);

            for (int i = 0; i < count; i++) {

                Unit unit = cityUnits.get(i % cityUnits.size());

                Police police = new Police();
                police.id = city.plateCode + "-" + (1001 + i);
                police.badgeNumber = city.plateCode + String.format("%04d", 1001 + i);
                police.fullName = FIRST_NAMES[index % FIRST_NAMES.length] + " "
                        + LAST_NAMES[(index / 3) % LAST_NAMES.length];
                police.age = 24 + (index % 30);
                police.rank = RANKS[index % RANKS.length];
                police.score = 60 + (index % 41);
                police.photoUrl = "/images/police/" + police.id + ".jpg";
                police.phone = "05" + String.format("%09d", 300000000 + (index * 137));
                police.startDate = (2005 + (index % 18)) + "-03-01";
                police.cityId = city.id;
                police.cityName = city.name;
                police.unitId = unit.id;
                police.unitName = unit.name;
                police.status = STATUS_ORDER[index % STATUS_ORDER.length];
                police.taskType = TASK_TYPES[index % TASK_TYPES.length];
                police.dailyTaskCount = 3 + (index % 8);
                police.dailyTaskLimit = DAILY_TASK_LIMIT;
                police.vehiclePlate = city.plateCode + " TP " + (100 + i);

                POLICE_LIST.add(police);
                index++;
            }
        }
    }

    /** diger moduller polis listesine buradan ulasir */
    public static List<Police> GetPoliceList() {
        return POLICE_LIST;
    }

    public static Police GetPolice(String policeId) {
        for (Police police : POLICE_LIST) {
            if (police.id.equals(policeId)) {
                return police;
            }
        }
        return null;
    }

    public static Boolean IsOverDailyLimit(Police police) {
        return police.dailyTaskCount > police.dailyTaskLimit;
    }

    public static String GetStatusName(String status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case "SAHADA":
                return "Sahada";
            case "MERKEZDE":
                return "Merkezde";
            case "IZINDE":
                return "Izinde";
            case "RAPORLU":
                return "Raporlu";
            default:
                return status;
        }
    }

    public static String GetTaskTypeName(String taskType) {
        if (taskType == null) {
            return "";
        }
        switch (taskType) {
            case "DEVRIYE":
                return "Devriye";
            case "RADAR":
                return "Radar";
            case "MOTOSIKLET":
                return "Motosiklet";
            case "OKUL_GECIDI":
                return "Okul Gecidi";
            case "KAZA_INCELEME":
                return "Kaza Inceleme";
            default:
                return taskType;
        }
    }

    /** Sehir / birim / durum filtreleriyle polis listesi */
    public PoliceListResponse PoliceList(PoliceListRequest policeListRequest) {

        String cityId = policeListRequest == null || policeListRequest.cityId == null ? "" : policeListRequest.cityId;
        String unitId = policeListRequest == null || policeListRequest.unitId == null ? "" : policeListRequest.unitId;
        String status = policeListRequest == null || policeListRequest.status == null ? "" : policeListRequest.status;
        String searchText = policeListRequest == null || policeListRequest.searchText == null
                ? ""
                : policeListRequest.searchText.trim().toLowerCase();

        PoliceListResponse policeListResponse = new PoliceListResponse();

        List<PoliceListItem> filtered = new ArrayList<>();

        for (Police police : POLICE_LIST) {

            if (!cityId.isEmpty() && !police.cityId.equals(cityId)) {
                continue;
            }
            if (!unitId.isEmpty() && !police.unitId.equals(unitId)) {
                continue;
            }
            if (!status.isEmpty() && !police.status.equals(status)) {
                continue;
            }
            if (!searchText.isEmpty()
                    && !police.fullName.toLowerCase().contains(searchText)
                    && !police.badgeNumber.toLowerCase().contains(searchText)) {
                continue;
            }

            PoliceListItem item = new PoliceListItem();
            item.id = police.id;
            item.badgeNumber = police.badgeNumber;
            item.fullName = police.fullName;
            item.rank = police.rank;
            item.cityName = police.cityName;
            item.unitName = police.unitName;
            item.status = police.status;
            item.statusName = GetStatusName(police.status);
            item.taskTypeName = GetTaskTypeName(police.taskType);
            item.score = police.score;
            item.photoUrl = police.photoUrl;
            item.overDailyLimit = IsOverDailyLimit(police);

            filtered.add(item);
        }

        Sort(filtered, policeListRequest);

        policeListResponse.totalCount = filtered.size();

        int pageNumber = policeListRequest == null || policeListRequest.pageNumber == null
                ? 0
                : policeListRequest.pageNumber;
        int pageSize = policeListRequest == null || policeListRequest.pageSize == null
                ? 0
                : policeListRequest.pageSize;

        if (pageNumber < 1 || pageSize < 1) {
            policeListResponse.policeList = filtered;
            return policeListResponse;
        }

        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, filtered.size());

        if (fromIndex < filtered.size()) {
            policeListResponse.policeList = new ArrayList<>(filtered.subList(fromIndex, toIndex));
        }

        policeListResponse.pageNumber = pageNumber;
        policeListResponse.pageSize = pageSize;

        return policeListResponse;
    }

    /** Grid basliklarindan siralama */
    private void Sort(List<PoliceListItem> list, PoliceListRequest policeListRequest) {

        String sortField = policeListRequest == null || policeListRequest.sortField == null
                ? ""
                : policeListRequest.sortField;

        if (sortField.isEmpty()) {
            return;
        }

        Comparator<PoliceListItem> comparator;

        switch (sortField) {
            case "score":
                comparator = Comparator.comparing(item -> item.score);
                break;
            case "badgeNumber":
                comparator = Comparator.comparing(item -> item.badgeNumber);
                break;
            case "cityName":
                comparator = Comparator.comparing(item -> item.cityName);
                break;
            case "unitName":
                comparator = Comparator.comparing(item -> item.unitName);
                break;
            case "fullName":
                comparator = Comparator.comparing(item -> item.fullName);
                break;
            default:
                return;
        }

        boolean descending = policeListRequest.sortDirection != null
                && policeListRequest.sortDirection.equalsIgnoreCase("DESC");

        list.sort(descending ? comparator.reversed() : comparator);
    }

    /** Secilen polisin detay bilgileri */
    public PoliceDetailResponse PoliceDetail(PoliceDetailRequest policeDetailRequest) {

        PoliceDetailResponse policeDetailResponse = new PoliceDetailResponse();

        if (policeDetailRequest == null || policeDetailRequest.policeId == null) {
            return policeDetailResponse;
        }

        Police police = GetPolice(policeDetailRequest.policeId);
        if (police == null) {
            return policeDetailResponse;
        }

        policeDetailResponse.found = true;
        policeDetailResponse.id = police.id;
        policeDetailResponse.badgeNumber = police.badgeNumber;
        policeDetailResponse.fullName = police.fullName;
        policeDetailResponse.age = police.age;
        policeDetailResponse.rank = police.rank;
        policeDetailResponse.score = police.score;
        policeDetailResponse.photoUrl = police.photoUrl;
        policeDetailResponse.phone = police.phone;
        policeDetailResponse.startDate = police.startDate;
        policeDetailResponse.cityId = police.cityId;
        policeDetailResponse.cityName = police.cityName;
        policeDetailResponse.unitId = police.unitId;
        policeDetailResponse.unitName = police.unitName;
        policeDetailResponse.status = police.status;
        policeDetailResponse.statusName = GetStatusName(police.status);
        policeDetailResponse.taskType = police.taskType;
        policeDetailResponse.taskTypeName = GetTaskTypeName(police.taskType);
        policeDetailResponse.dailyTaskCount = police.dailyTaskCount;
        policeDetailResponse.dailyTaskLimit = police.dailyTaskLimit;
        policeDetailResponse.overDailyLimit = IsOverDailyLimit(police);
        policeDetailResponse.vehiclePlate = police.vehiclePlate;

        return policeDetailResponse;
    }

    /** Durum filtresinin secenekleri */
    public PoliceStatusListResponse PoliceStatusList(PoliceStatusListRequest policeStatusListRequest) {

        PoliceStatusListResponse policeStatusListResponse = new PoliceStatusListResponse();

        policeStatusListResponse.statuses.add(new PoliceStatusItem("SAHADA", "Sahada"));
        policeStatusListResponse.statuses.add(new PoliceStatusItem("MERKEZDE", "Merkezde"));
        policeStatusListResponse.statuses.add(new PoliceStatusItem("IZINDE", "Izinde"));
        policeStatusListResponse.statuses.add(new PoliceStatusItem("RAPORLU", "Raporlu"));

        return policeStatusListResponse;
    }
}
