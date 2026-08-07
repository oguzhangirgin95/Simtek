import { Component, OnInit, computed, inject } from '@angular/core';
import { BaseComponent } from '../../../../../lib/base/basecomponent/basecomponent';
import { AnalyticsControllerService } from '../../../../../lib/services/api/analyticsController.service';
import { RegionControllerService } from '../../../../../lib/services/api/regionController.service';
import { TaskControllerService } from '../../../../../lib/services/api/taskController.service';
import { UnitControllerService } from '../../../../../lib/services/api/unitController.service';
import { Barchart } from '../../../../../lib/commons/barchart/barchart';
import { Card } from '../../../../../lib/commons/card/card';
import { Donutchart } from '../../../../../lib/commons/donutchart/donutchart';
import { Grid } from '../../../../../lib/commons/grid/grid';
import { Select } from '../../../../../lib/commons/select/select';
import { Statcard } from '../../../../../lib/commons/statcard/statcard';

@Component({
  imports: [Barchart, Card, Donutchart, Grid, Select, Statcard],
  templateUrl: './tasktrend.start.html',
  styleUrl: './tasktrend.scss',
})
export class TasktrendStart extends BaseComponent implements OnInit {
  private readonly analyticsService = inject(AnalyticsControllerService);
  private readonly taskService = inject(TaskControllerService);
  private readonly regionService = inject(RegionControllerService);
  private readonly unitService = inject(UnitControllerService);

  readonly labels = computed(() => ({
    title: this.getResource('TASKTREND_TITLE', 'Görev Analizi'),
    city: this.getResource('FILTER_CITY', 'Şehir'),
    unit: this.getResource('FILTER_UNIT', 'Birim'),
    all: this.getResource('FILTER_ALL', 'Tümü'),
    trendTitle: this.getResource('TASKTREND_CHART', 'Günlük görev trendi'),
    typeChartTitle: this.getResource('TASKLIST_TYPECHART', 'Görev tipi dağılımı'),
    scope: this.getResource('TASKTREND_SCOPE', 'Kapsam'),
    totalTask: this.getResource('TASKTREND_TOTAL', 'Toplam görev'),
    averageTask: this.getResource('TASKTREND_AVERAGE', 'Günlük ortalama'),
  }));

  readonly columns = computed(() => [
    { field: 'label', title: this.getResource('GRID_DAY', 'Gün') },
    { field: 'taskCount', title: this.getResource('GRID_TASKCOUNT', 'Görev') },
    { field: 'activePolice', title: this.getResource('GRID_ACTIVEPOLICE', 'Sahada') },
  ]);

  constructor() {
    super();
  }

  ngOnInit() {
    this.State.Request = { cityId: '', unitId: '' };

    this.getCityList();
    this.getUnitList();
    this.getTaskTrend();
    this.getTypeList();
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

  getTaskTrend() {
    this.analyticsService
      .taskTrend(this.State.Request)
      .toPromise()
      .then((response) => {
        this.State.Trend = response;
        this.State.TrendChart = (response?.points ?? []).map((point) => ({
          label: point.label ?? '',
          value: point.taskCount ?? 0,
        }));
      })
      .catch((error) => console.error('Task trend:', error));
  }

  getTypeList() {
    this.taskService
      .taskTypeList({ cityId: this.State.Request.cityId })
      .toPromise()
      .then((response) => {
        this.State.TypeChart = (response?.types ?? []).map((type) => ({
          label: type.name ?? '',
          value: type.count ?? 0,
        }));
      })
      .catch((error) => console.error('Task types:', error));
  }

  setFilter(key: string, value: string) {
    this.State.Request = { ...this.State.Request, [key]: value };

    if (key === 'cityId') {
      this.State.Request = { ...this.State.Request, unitId: '' };
      this.getUnitList();
      this.getTypeList();
    }

    this.getTaskTrend();
  }
}
