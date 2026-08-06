package modules.units.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import models.regions.entity.City;
import models.units.entity.Unit;
import models.units.request.UnitDeleteRequest;
import models.units.request.UnitListRequest;
import models.units.request.UnitSaveRequest;
import models.units.response.UnitDeleteResponse;
import models.units.response.UnitListItem;
import models.units.response.UnitListResponse;
import models.units.response.UnitSaveResponse;
import modules.personnel.repositories.PoliceRepository;
import modules.regions.repositories.CityRepository;
import modules.units.repositories.UnitRepository;

@Service
public class UnitBusiness {

    private final UnitRepository unitRepository;
    private final CityRepository cityRepository;
    private final PoliceRepository policeRepository;

    public UnitBusiness(UnitRepository unitRepository, CityRepository cityRepository,
            PoliceRepository policeRepository) {
        this.unitRepository = unitRepository;
        this.cityRepository = cityRepository;
        this.policeRepository = policeRepository;
    }

    /** Birim filtresi icin birim listesi, cityId verilirse o sehrin birimleri */
    @Transactional(readOnly = true)
    public UnitListResponse UnitList(UnitListRequest unitListRequest) {

        String cityId = unitListRequest == null || unitListRequest.cityId == null ? "" : unitListRequest.cityId;

        List<Unit> units = cityId.isEmpty()
                ? unitRepository.findAllByOrderByCityIdAscSeqAsc()
                : unitRepository.findByCityIdOrderBySeqAsc(cityId);

        Map<String, String> cityNames = GetCityNames();

        UnitListResponse unitListResponse = new UnitListResponse();

        for (Unit unit : units) {
            unitListResponse.units.add(
                    new UnitListItem(unit.id, unit.name, unit.cityId, cityNames.getOrDefault(unit.cityId, "")));
        }

        unitListResponse.totalCount = unitListResponse.units.size();

        return unitListResponse;
    }

    /** Birim ekleme / guncelleme */
    @Transactional
    public UnitSaveResponse UnitSave(UnitSaveRequest unitSaveRequest) {

        UnitSaveResponse response = new UnitSaveResponse();

        if (unitSaveRequest == null || unitSaveRequest.name == null || unitSaveRequest.name.trim().isEmpty()) {
            response.message = "Birim adi girilmeli.";
            return response;
        }
        if (unitSaveRequest.cityId == null || unitSaveRequest.cityId.trim().isEmpty()) {
            response.message = "Sehir secilmeli.";
            return response;
        }
        if (cityRepository.findById(unitSaveRequest.cityId).isEmpty()) {
            response.message = "Sehir bulunamadi.";
            return response;
        }

        Unit unit;

        if (unitSaveRequest.id == null || unitSaveRequest.id.trim().isEmpty()) {
            // yeni birim: sehirdeki son siranin bir fazlasi
            int nextSeq = (int) unitRepository.countByCityId(unitSaveRequest.cityId) + 1;
            unit = new Unit();
            unit.id = unitSaveRequest.cityId + "-B" + nextSeq;
            unit.seq = unitSaveRequest.seq == null ? nextSeq : unitSaveRequest.seq;
        } else {
            unit = unitRepository.findById(unitSaveRequest.id).orElse(null);
            if (unit == null) {
                response.message = "Birim bulunamadi.";
                return response;
            }
            unit.seq = unitSaveRequest.seq == null ? unit.seq : unitSaveRequest.seq;
        }

        unit.name = unitSaveRequest.name.trim();
        unit.cityId = unitSaveRequest.cityId;

        unitRepository.save(unit);

        response.success = true;
        response.id = unit.id;
        response.message = "Birim kaydedildi.";

        return response;
    }

    /** Birim silme; bagli personel varsa silinmez */
    @Transactional
    public UnitDeleteResponse UnitDelete(UnitDeleteRequest unitDeleteRequest) {

        UnitDeleteResponse response = new UnitDeleteResponse();

        if (unitDeleteRequest == null || unitDeleteRequest.id == null) {
            response.message = "Birim secilmedi.";
            return response;
        }

        Optional<Unit> unit = unitRepository.findById(unitDeleteRequest.id);
        if (unit.isEmpty()) {
            response.message = "Birim bulunamadi.";
            return response;
        }

        if (policeRepository.countByUnitId(unitDeleteRequest.id) > 0) {
            response.message = "Birime bagli personel oldugu icin silinemez.";
            return response;
        }

        unitRepository.delete(unit.get());

        response.success = true;
        response.message = "Birim silindi.";

        return response;
    }

    private Map<String, String> GetCityNames() {
        Map<String, String> cityNames = new HashMap<>();
        for (City city : cityRepository.findAll()) {
            cityNames.put(city.id, city.name);
        }
        return cityNames;
    }
}
