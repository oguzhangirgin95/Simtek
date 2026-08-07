import { Component, OnInit, computed, inject } from '@angular/core';
import { BaseComponent } from '../../../../../lib/base/basecomponent/basecomponent';
import { CommonsModule } from '../../../../../lib/commons/commons-module';
import { RegionControllerService } from '../../../../../lib/services/api/regionController.service';
import { UnitControllerService } from '../../../../../lib/services/api/unitController.service';
import { VehicleControllerService } from '../../../../../lib/services/api/vehicleController.service';

@Component({
  imports: [CommonsModule],
  templateUrl: './vehiclelist.start.html',
  styleUrl: './vehiclelist.scss',
})
export class VehiclelistStart extends BaseComponent implements OnInit {
  private readonly vehicleService = inject(VehicleControllerService);
  private readonly regionService = inject(RegionControllerService);
  private readonly unitService = inject(UnitControllerService);

  readonly labels = computed(() => ({
    title: this.getResource('VEHICLELIST_TITLE', 'Araç Envanteri'),
    city: this.getResource('FILTER_CITY', 'Şehir'),
    unit: this.getResource('FILTER_UNIT', 'Birim'),
    type: this.getResource('FILTER_TYPE', 'Araç tipi'),
    all: this.getResource('FILTER_ALL', 'Tümü'),
    search: this.getResource('VEHICLELIST_SEARCH', 'Plaka veya marka'),
    clear: this.getResource('BUTTON_CLEAR', 'Temizle'),
    listTitle: this.getResource('VEHICLELIST_LIST', 'Araçlar'),
    listHint: this.getResource('VEHICLELIST_HINT', 'Detay için satıra tıklayın'),
    detailTitle: this.getResource('VEHICLELIST_DETAIL', 'Araç detayı'),
    selectFirst: this.getResource('VEHICLELIST_SELECT', 'Listeden bir araç seçin.'),
    typeChartTitle: this.getResource('VEHICLELIST_TYPECHART', 'Tip dağılımı'),
    total: this.getResource('VEHICLELIST_TOTAL', 'Listelenen araç'),
    empty: this.getResource('VEHICLELIST_EMPTY', 'Araç bulunamadı'),
  }));

  readonly columns = computed(() => [
    { field: 'plate', title: this.getResource('GRID_PLATE', 'Plaka') },
    { field: 'type', title: this.getResource('GRID_TYPE', 'Tip') },
    { field: 'brand', title: this.getResource('GRID_BRAND', 'Marka') },
    { field: 'model', title: this.getResource('GRID_MODEL', 'Model') },
    { field: 'modelYear', title: this.getResource('GRID_MODELYEAR', 'Yıl') },
    { field: 'kilometers', title: this.getResource('GRID_KM', 'Km') },
    { field: 'cityName', title: this.getResource('GRID_CITY', 'Şehir') },
    { field: 'unitName', title: this.getResource('GRID_UNIT', 'Birim') },
    { field: 'policeName', title: this.getResource('GRID_ASSIGNED', 'Zimmetli') },
  ]);

  constructor() {
    super();
  }

  ngOnInit() {
    this.State.Request = { cityId: '', unitId: '', type: '', searchText: '' };

    this.getCityList();
    this.getUnitList();
    this.getTypeList();
    this.getVehicleList();
  }


  getCityList() {
    this.regionService
      .regionList({})
      .toPromise()
      .then((response) => {
        this.State.CityList = (response?.regions ?? []).map((city) => ({ value: city.id, text: city.name }));
      })
      .catch((error) => console.error('City list:', error));
  }

  getUnitList() {
    this.unitService
      .unitList({ cityId: this.State.Request.cityId })
      .toPromise()
      .then((response) => {
        this.State.UnitList = (response?.units ?? []).map((unit) => ({ value: unit.id, text: unit.name }));
      })
      .catch((error) => console.error('Unit list:', error));
  }

  getTypeList() {
    this.vehicleService
      .vehicleTypeList({})
      .toPromise()
      .then((response) => {
        this.State.TypeCounts = response?.types ?? [];
        this.State.TypeList = (response?.types ?? []).map((type) => ({ value: type.key, text: type.name }));
        this.State.TypeChart = (response?.types ?? []).map((type) => ({
          label: type.name ?? '',
          value: type.count ?? 0,
        }));
      })
      .catch((error) => console.error('Vehicle types:', error));
  }


  getVehicleList() {
    this.vehicleService
      .vehicleList(this.State.Request)
      .toPromise()
      .then((response) => {
        this.State.VehicleList = response?.vehicles ?? [];
        this.State.TotalCount = response?.totalCount ?? 0;
      })
      .catch((error) => console.error('Vehicle list:', error));
  }

  setFilter(key: string, value: string) {
    this.State.Request = { ...this.State.Request, [key]: value };

    if (key === 'cityId') {
      this.State.Request = { ...this.State.Request, unitId: '' };
      this.getUnitList();
    }

    this.getVehicleList();
  }

  clearFilter() {
    this.State.Request = { cityId: '', unitId: '', type: '', searchText: '' };
    this.getUnitList();
    this.getVehicleList();
  }


  selectVehicle(row: any) {
    this.State.SelectedPlate = row.plate;

    this.vehicleService
      .vehicleDetail({ plate: row.plate })
      .toPromise()
      .then((response) => {
        this.State.VehicleDetail = response;
        this.State.VehicleItems = [
          { key: this.getResource('DETAIL_PLATE', 'Plaka'), value: response?.plate },
          { key: this.getResource('DETAIL_TYPE', 'Tip'), value: response?.type },
          { key: this.getResource('DETAIL_BRAND', 'Marka'), value: response?.brand },
          { key: this.getResource('DETAIL_MODEL', 'Model'), value: `${response?.model} (${response?.modelYear})` },
          { key: this.getResource('DETAIL_KM', 'Kilometre'), value: response?.kilometers },
          { key: this.getResource('DETAIL_MAINTENANCE', 'Son bakım'), value: response?.lastMaintenanceDate },
          { key: this.getResource('GRID_CITY', 'Şehir'), value: response?.cityName },
          { key: this.getResource('GRID_UNIT', 'Birim'), value: response?.unitName },
        ];
      })
      .catch((error) => console.error('Vehicle detail:', error));
  }
}
