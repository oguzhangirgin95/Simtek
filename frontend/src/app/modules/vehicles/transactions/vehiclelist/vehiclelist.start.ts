import { Component, DestroyRef, OnInit, computed, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { catchError, debounceTime, EMPTY, firstValueFrom, Subject, switchMap } from 'rxjs';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
import { RegionControllerService } from '@lib/services/api/regionController.service';
import { UnitControllerService } from '@lib/services/api/unitController.service';
import { VehicleControllerService } from '@lib/services/api/vehicleController.service';
import { Button } from '@lib/commons/button/button';
import { Card } from '@lib/commons/card/card';
import { Detailcard } from '@lib/commons/detailcard/detailcard';
import { Donutchart } from '@lib/commons/donutchart/donutchart';
import { Grid } from '@lib/commons/grid/grid';
import { Info } from '@lib/commons/info/info';
import { Input } from '@lib/commons/input/input';
import { Modal } from '@lib/commons/modal/modal';
import { Pagination } from '@lib/commons/pagination/pagination';
import { Select } from '@lib/commons/select/select';
import { Statcard } from '@lib/commons/statcard/statcard';

const PAGE_SIZE = 20;
const SEARCH_DELAY = 400;

@Component({
  imports: [Button, Card, Detailcard, Donutchart, FormsModule, Grid, Info, Input, Modal, Pagination, Select, Statcard],
  templateUrl: './vehiclelist.start.html',
  styleUrl: './vehiclelist.scss',
})
export class VehiclelistStart extends BaseComponent implements OnInit {
  private readonly vehicleService = inject(VehicleControllerService);
  private readonly regionService = inject(RegionControllerService);
  private readonly unitService = inject(UnitControllerService);
  private readonly destroyRef = inject(DestroyRef);

  private readonly search = new Subject<void>();

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
    this.State.Request = { cityId: '', unitId: '', type: '', searchText: '', pageNumber: 1, pageSize: PAGE_SIZE };

    this.getCityList();
    this.getUnitList();
    this.getTypeList();
    this.getVehicleList();

    this.search
      .pipe(
        debounceTime(SEARCH_DELAY),
        switchMap(() => this.vehicleService.vehicleList(this.State.Request).pipe(catchError(() => EMPTY))),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe((response) => {
        this.State.VehicleList = response?.vehicles ?? [];
        this.State.TotalCount = response?.totalCount ?? 0;
      });
  }


  getCityList() {
    this.once('CityList', () => firstValueFrom(this.regionService.regionList({})))
      .then((response) => {
        this.State.CityList = (response?.regions ?? []).map((city) => ({ value: city.id, text: city.name }));
      })
      .catch((error) => console.error('City list:', error));
  }

  getUnitList() {
    this.once(`UnitList:${this.State.Request.cityId}`, () => firstValueFrom(this.unitService.unitList({ cityId: this.State.Request.cityId })))
      .then((response) => {
        this.State.UnitList = (response?.units ?? []).map((unit) => ({ value: unit.id, text: unit.name }));
      })
      .catch((error) => console.error('Unit list:', error));
  }

  getTypeList() {
    this.once('VehicleTypeList', () => firstValueFrom(this.vehicleService.vehicleTypeList({})))
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
    firstValueFrom(this.vehicleService.vehicleList(this.State.Request))
      .then((response) => {
        this.State.VehicleList = response?.vehicles ?? [];
        this.State.TotalCount = response?.totalCount ?? 0;
      })
      .catch((error) => console.error('Vehicle list:', error));
  }

  setPage(page: number) {
    this.State.Request.pageNumber = page;
    this.getVehicleList();
  }

  setFilter(key: string, value: string) {
    this.State.Request[key] = value;
    this.State.Request.pageNumber = 1;

    if (key === 'cityId') {
      this.State.Request.unitId = '';
      this.getUnitList();
    }

    key === 'searchText' ? this.search.next() : this.getVehicleList();
  }

  clearFilter() {
    this.State.Request = { cityId: '', unitId: '', type: '', searchText: '', pageNumber: 1, pageSize: PAGE_SIZE };
    this.getUnitList();
    this.getVehicleList();
  }


  closeDetail() {
    this.State.SelectedPlate = undefined;
  }

  selectVehicle(row: any) {
    this.State.SelectedPlate = row.plate;

    firstValueFrom(this.vehicleService.vehicleDetail({ plate: row.plate }))
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
