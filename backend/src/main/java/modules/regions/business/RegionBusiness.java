package modules.regions.business;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import models.regions.entity.City;
import models.regions.request.RegionDeleteRequest;
import models.regions.request.RegionListRequest;
import models.regions.request.RegionSaveRequest;
import models.regions.response.RegionDeleteResponse;
import models.regions.response.RegionListItem;
import models.regions.response.RegionListResponse;
import models.regions.response.RegionSaveResponse;
import modules.personnel.repositories.PoliceRepository;
import modules.regions.repositories.CityRepository;
import modules.units.repositories.UnitRepository;

@Service
public class RegionBusiness {

    private final CityRepository cityRepository;
    private final UnitRepository unitRepository;
    private final PoliceRepository policeRepository;

    public RegionBusiness(CityRepository cityRepository, UnitRepository unitRepository,
            PoliceRepository policeRepository) {
        this.cityRepository = cityRepository;
        this.unitRepository = unitRepository;
        this.policeRepository = policeRepository;
    }

    @Transactional(readOnly = true)
    public RegionListResponse RegionList(RegionListRequest regionListRequest) {

        String searchText = regionListRequest == null || regionListRequest.searchText == null
                ? ""
                : regionListRequest.searchText.trim();

        List<City> cities = searchText.isEmpty()
                ? cityRepository.findAllByOrderBySortOrderAsc()
                : cityRepository.findByNameContainingIgnoreCaseOrderBySortOrderAsc(searchText);

        RegionListResponse regionListResponse = new RegionListResponse();

        for (City city : cities) {
            regionListResponse.regions.add(new RegionListItem(city.id, city.name, city.plateCode, city.x, city.y));
        }

        regionListResponse.totalCount = regionListResponse.regions.size();

        return regionListResponse;
    }

    @Transactional
    public RegionSaveResponse RegionSave(RegionSaveRequest regionSaveRequest) {

        RegionSaveResponse response = new RegionSaveResponse();

        if (regionSaveRequest == null || regionSaveRequest.name == null || regionSaveRequest.name.trim().isEmpty()) {
            response.message = "Sehir adi girilmeli.";
            return response;
        }
        if (regionSaveRequest.plateCode == null || regionSaveRequest.plateCode.trim().isEmpty()) {
            response.message = "Plaka kodu girilmeli.";
            return response;
        }

        String id = regionSaveRequest.id == null || regionSaveRequest.id.trim().isEmpty()
                ? regionSaveRequest.plateCode.trim()
                : regionSaveRequest.id.trim();

        City city = cityRepository.findById(id).orElseGet(City::new);
        city.id = id;
        city.name = regionSaveRequest.name.trim();
        city.plateCode = regionSaveRequest.plateCode.trim();
        city.x = regionSaveRequest.x == null ? 0.0 : regionSaveRequest.x;
        city.y = regionSaveRequest.y == null ? 0.0 : regionSaveRequest.y;
        city.sortOrder = regionSaveRequest.sortOrder == null ? 99 : regionSaveRequest.sortOrder;

        cityRepository.save(city);

        response.success = true;
        response.id = city.id;
        response.message = "Sehir kaydedildi.";

        return response;
    }

    @Transactional
    public RegionDeleteResponse RegionDelete(RegionDeleteRequest regionDeleteRequest) {

        RegionDeleteResponse response = new RegionDeleteResponse();

        if (regionDeleteRequest == null || regionDeleteRequest.id == null) {
            response.message = "Sehir secilmedi.";
            return response;
        }

        Optional<City> city = cityRepository.findById(regionDeleteRequest.id);
        if (city.isEmpty()) {
            response.message = "Sehir bulunamadi.";
            return response;
        }

        if (policeRepository.countByCityId(regionDeleteRequest.id) > 0) {
            response.message = "Sehre bagli personel oldugu icin silinemez.";
            return response;
        }
        if (unitRepository.countByCityId(regionDeleteRequest.id) > 0) {
            response.message = "Sehre bagli birim oldugu icin silinemez.";
            return response;
        }

        cityRepository.delete(city.get());

        response.success = true;
        response.message = "Sehir silindi.";

        return response;
    }
}
