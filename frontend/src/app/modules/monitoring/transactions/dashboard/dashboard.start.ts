import { Component, OnInit, computed, inject } from '@angular/core';
import { BaseComponent } from '../../../../../lib/base/basecomponent/basecomponent';
import { DashboardControllerService } from '../../../../../lib/services/api/dashboardController.service';
import { PoliceControllerService } from '../../../../../lib/services/api/policeController.service';
import { RegionControllerService } from '../../../../../lib/services/api/regionController.service';
import { UnitControllerService } from '../../../../../lib/services/api/unitController.service';
import { Barchart } from '../../../../../lib/commons/barchart/barchart';
import { Card } from '../../../../../lib/commons/card/card';
import { Donutchart } from '../../../../../lib/commons/donutchart/donutchart';
import { Grid } from '../../../../../lib/commons/grid/grid';
import { Map } from '../../../../../lib/commons/map/map';
import { Select } from '../../../../../lib/commons/select/select';
import { Statcard } from '../../../../../lib/commons/statcard/statcard';

@Component({
  imports: [Barchart, Card, Donutchart, Grid, Map, Select, Statcard],
  templateUrl: './dashboard.start.html',
  styleUrl: './dashboard.scss',
})
export class DashboardStart extends BaseComponent implements OnInit {
  private readonly dashboardService = inject(DashboardControllerService);
  private readonly regionService = inject(RegionControllerService);
  private readonly unitService = inject(UnitControllerService);
  private readonly policeService = inject(PoliceControllerService);

  readonly labels = computed(() => ({
    title: this.getResource('DASHBOARD_TITLE', 'Trafik Polisi Takip Panosu'),
    city: this.getResource('FILTER_CITY', 'Şehir'),
    unit: this.getResource('FILTER_UNIT', 'Birim'),
    status: this.getResource('FILTER_STATUS', 'Durum'),
    all: this.getResource('FILTER_ALL', 'Tümü'),
    totalPolice: this.getResource('STAT_TOTAL', 'Toplam personel'),
    onDuty: this.getResource('STAT_ONDUTY', 'Sahada'),
    atStation: this.getResource('STAT_ATSTATION', 'Merkezde'),
    onLeave: this.getResource('STAT_ONLEAVE', 'İzinde'),
    onReport: this.getResource('STAT_ONREPORT', 'Raporlu'),
    overLimit: this.getResource('STAT_OVERLIMIT', 'Limiti aşan'),
    mapTitle: this.getResource('MAP_TITLE', 'Şehir bazlı aktif memur'),
    mapHint: this.getResource('MAP_HINT', 'Şehre tıklayarak filtreleyin'),
    busiest: this.getResource('DASHBOARD_BUSIEST', 'En yoğun şehir'),
    statusChartTitle: this.getResource('STATUS_CHART_TITLE', 'Durum dağılımı'),
    unitWorkloadTitle: this.getResource('UNIT_WORKLOAD_TITLE', 'birim görev yoğunluğu'),
    emptyUnit: this.getResource('EMPTY_UNIT', 'Birim bulunamadı'),
  }));

  readonly unitColumns = computed(() => [
    { field: 'unitName', title: this.getResource('GRID_UNIT', 'Birim') },
    { field: 'totalPolice', title: this.getResource('GRID_TOTALPOLICE', 'Personel') },
    { field: 'activePolice', title: this.getResource('GRID_ACTIVEPOLICE', 'Sahada') },
    { field: 'patrol', title: this.getResource('GRID_PATROL', 'Devriye') },
    { field: 'radar', title: this.getResource('GRID_RADAR', 'Radar') },
    { field: 'motorcycle', title: this.getResource('GRID_MOTORCYCLE', 'Motosiklet') },
    { field: 'taskLoad', title: this.getResource('GRID_TASKLOAD', 'Görev') },
  ]);

  constructor() {
    super();
  }

  ngOnInit() {
    this.State.Request = { cityId: '', unitId: '', status: '' };

    this.getCityList();
    this.getUnitList();
    this.getStatusList();
    this.getDashboard();
  }


  getCityList() {
    this.once('CityList', () => this.regionService.regionList({}).toPromise())
      .then((response) => {
        this.State.CityList = (response?.regions ?? []).map((city) => ({ value: city.id, text: city.name }));
      })
      .catch((error) => console.error('City list:', error));
  }

  getUnitList() {
    this.once(`UnitList:${this.State.Request.cityId}`, () => this.unitService.unitList({ cityId: this.State.Request.cityId }).toPromise())
      .then((response) => {
        this.State.UnitList = (response?.units ?? []).map((unit) => ({ value: unit.id, text: unit.name }));
      })
      .catch((error) => console.error('Unit list:', error));
  }

  getStatusList() {
    this.once('StatusList', () => this.policeService.policeStatusList({}).toPromise())
      .then((response) => {
        this.State.StatusList = (response?.statuses ?? []).map((status) => ({
          value: status.key,
          text: status.name,
        }));
      })
      .catch((error) => console.error('Status list:', error));
  }


  getDashboard() {
    this.getSummary();
    this.getMapStatistics();
    this.getUnitWorkload();
  }

  getSummary() {
    this.dashboardService
      .dashboardSummary(this.State.Request)
      .toPromise()
      .then((response) => {
        this.State.Summary = response;
        this.State.StatusChart = (response?.statusDistribution ?? []).map((item) => ({
          label: item.name ?? '',
          value: item.count ?? 0,
        }));
      })
      .catch((error) => console.error('Summary:', error));
  }

  getMapStatistics() {
    this.dashboardService
      .mapStatistics(this.State.Request)
      .toPromise()
      .then((response) => {
        this.State.MapStatistics = response;
        this.State.MapPoints = (response?.cities ?? []).map((city) => ({
          id: city.cityId ?? '',
          name: city.cityName ?? '',
          x: city.x ?? 0,
          y: city.y ?? 0,
          value: city.activePolice ?? 0,
        }));
      })
      .catch((error) => console.error('Map:', error));
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
      .catch((error) => console.error('Unit workload:', error));
  }


  setFilter(key: string, value: string) {
    this.State.Request = { ...this.State.Request, [key]: value };

    if (key === 'cityId') {
      this.State.Request = { ...this.State.Request, unitId: '' };
      this.getUnitList();
    }

    this.getDashboard();
  }

  selectCity(point: any) {
    this.setFilter('cityId', point.id);
  }
}
