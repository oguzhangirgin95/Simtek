import { Component, OnInit, computed, inject } from '@angular/core';
import { BaseComponent } from '../../../../../lib/base/basecomponent/basecomponent';
import { CommonsModule } from '../../../../../lib/commons/commons-module';
import { DashboardControllerService } from '../../../../../lib/services/api/dashboardController.service';
import { RegionControllerService } from '../../../../../lib/services/api/regionController.service';

@Component({
  imports: [CommonsModule],
  templateUrl: './unitlist.start.html',
  styleUrl: './unitlist.scss',
})
export class UnitlistStart extends BaseComponent implements OnInit {
  private readonly dashboardService = inject(DashboardControllerService);
  private readonly regionService = inject(RegionControllerService);

  readonly labels = computed(() => ({
    title: this.getResource('UNITLIST_TITLE', 'Birim Yoğunluğu'),
    city: this.getResource('FILTER_CITY', 'Şehir'),
    all: this.getResource('FILTER_ALL', 'Tümü'),
    chartTitle: this.getResource('UNIT_WORKLOAD_TITLE', 'birim görev yoğunluğu'),
    listTitle: this.getResource('UNITLIST_LIST', 'Birimler'),
    totalTask: this.getResource('UNITLIST_TOTALTASK', 'Toplam görev'),
    unitCount: this.getResource('UNITLIST_UNITCOUNT', 'Birim sayısı'),
    empty: this.getResource('EMPTY_UNIT', 'Birim bulunamadı'),
  }));

  readonly columns = computed(() => [
    { field: 'unitName', title: this.getResource('GRID_UNIT', 'Birim') },
    { field: 'totalPolice', title: this.getResource('GRID_TOTALPOLICE', 'Personel') },
    { field: 'activePolice', title: this.getResource('GRID_ACTIVEPOLICE', 'Sahada') },
    { field: 'patrol', title: this.getResource('GRID_PATROL', 'Devriye') },
    { field: 'radar', title: this.getResource('GRID_RADAR', 'Radar') },
    { field: 'motorcycle', title: this.getResource('GRID_MOTORCYCLE', 'Motosiklet') },
    { field: 'schoolCrossing', title: this.getResource('GRID_SCHOOL', 'Okul Geçidi') },
    { field: 'accidentInvestigation', title: this.getResource('GRID_ACCIDENT', 'Kaza') },
    { field: 'taskLoad', title: this.getResource('GRID_TASKLOAD', 'Görev') },
    { field: 'loadPercent', title: this.getResource('GRID_LOADPERCENT', 'Yoğunluk %') },
  ]);

  constructor() {
    super();
  }

  ngOnInit() {
    this.State.Request = { cityId: '', unitId: '', status: '' };

    this.getCityList();
    this.getUnitWorkload();
  }

  getCityList() {
    this.regionService
      .regionList({})
      .toPromise()
      .then((response) => {
        this.State.CityList = (response?.regions ?? []).map((city) => ({ value: city.id, text: city.name }));
      })
      .catch((error) => console.error('Sehir listesi:', error));
  }

  getUnitWorkload() {
    this.dashboardService
      .unitWorkload(this.State.Request)
      .toPromise()
      .then((response) => {
        this.State.UnitWorkload = response;
        this.State.UnitChart = (response?.units ?? []).map((unit) => ({
          label: unit.unitName ?? '',
          value: unit.taskLoad ?? 0,
        }));
      })
      .catch((error) => console.error('Birim yogunlugu:', error));
  }

  setFilter(key: string, value: string) {
    this.State.Request = { ...this.State.Request, [key]: value };
    this.getUnitWorkload();
  }
}
