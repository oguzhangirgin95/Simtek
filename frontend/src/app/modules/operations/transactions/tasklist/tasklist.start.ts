import { Component, OnInit, computed, inject } from '@angular/core';
import { BaseComponent } from '../../../../../lib/base/basecomponent/basecomponent';
import { CommonsModule } from '../../../../../lib/commons/commons-module';
import { RegionControllerService } from '../../../../../lib/services/api/regionController.service';
import { TaskControllerService } from '../../../../../lib/services/api/taskController.service';
import { UnitControllerService } from '../../../../../lib/services/api/unitController.service';

@Component({
  imports: [CommonsModule],
  templateUrl: './tasklist.start.html',
  styleUrl: './tasklist.scss',
})
export class TasklistStart extends BaseComponent implements OnInit {
  private readonly taskService = inject(TaskControllerService);
  private readonly regionService = inject(RegionControllerService);
  private readonly unitService = inject(UnitControllerService);

  readonly labels = computed(() => ({
    title: this.getResource('TASKLIST_TITLE', 'Görev Takibi'),
    city: this.getResource('FILTER_CITY', 'Şehir'),
    unit: this.getResource('FILTER_UNIT', 'Birim'),
    type: this.getResource('FILTER_TASKTYPE', 'Görev tipi'),
    status: this.getResource('FILTER_STATUS', 'Durum'),
    limit: this.getResource('FILTER_LIMIT', 'Limit durumu'),
    all: this.getResource('FILTER_ALL', 'Tümü'),
    clear: this.getResource('BUTTON_CLEAR', 'Temizle'),
    total: this.getResource('TASKLIST_TOTAL', 'Listelenen görev'),
    listTitle: this.getResource('TASKLIST_LIST', 'Görevler'),
    typeChartTitle: this.getResource('TASKLIST_TYPECHART', 'Görev tipi dağılımı'),
    empty: this.getResource('TASKLIST_EMPTY', 'Görev bulunamadı'),
    onlyOverLimit: this.getResource('TASKLIST_ONLYOVERLIMIT', 'Limiti aşanlar'),
  }));

  readonly statusList = computed(() => [
    { value: 'TAMAMLANDI', text: this.getResource('TASKSTATUS_DONE', 'Tamamlandı') },
    { value: 'DEVAM', text: this.getResource('TASKSTATUS_ACTIVE', 'Devam ediyor') },
    { value: 'PLANLANDI', text: this.getResource('TASKSTATUS_PLANNED', 'Planlandı') },
  ]);

  readonly limitList = computed(() => [{ value: '1', text: this.labels().onlyOverLimit }]);

  readonly columns = computed(() => [
    { field: 'badgeNumber', title: this.getResource('GRID_BADGE', 'Sicil') },
    { field: 'policeName', title: this.getResource('GRID_FULLNAME', 'Ad Soyad') },
    { field: 'cityName', title: this.getResource('GRID_CITY', 'Şehir') },
    { field: 'unitName', title: this.getResource('GRID_UNIT', 'Birim') },
    { field: 'typeName', title: this.getResource('GRID_TASKTYPE', 'Görev') },
    { field: 'location', title: this.getResource('GRID_LOCATION', 'Konum') },
    { field: 'startTime', title: this.getResource('GRID_STARTTIME', 'Başlangıç') },
    { field: 'endTime', title: this.getResource('GRID_ENDTIME', 'Bitiş') },
    { field: 'statusName', title: this.getResource('GRID_STATUS', 'Durum') },
  ]);

  constructor() {
    super();
  }

  ngOnInit() {
    this.State.Request = { cityId: '', unitId: '', type: '', status: '', onlyOverLimit: false };

    this.getCityList();
    this.getUnitList();
    this.getTypeList();
    this.getTaskList();
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
    this.taskService
      .taskTypeList({ cityId: this.State.Request.cityId })
      .toPromise()
      .then((response) => {
        this.State.TypeList = (response?.types ?? []).map((type) => ({ value: type.key, text: type.name }));
        this.State.TypeChart = (response?.types ?? []).map((type) => ({
          label: type.name ?? '',
          value: type.count ?? 0,
        }));
      })
      .catch((error) => console.error('Task types:', error));
  }


  getTaskList() {
    this.taskService
      .taskList(this.State.Request)
      .toPromise()
      .then((response) => {
        this.State.TaskList = response?.tasks ?? [];
        this.State.TotalCount = response?.totalCount ?? 0;
      })
      .catch((error) => console.error('Task list:', error));
  }

  setFilter(key: string, value: string) {
    this.State.Request = { ...this.State.Request, [key]: value };

    if (key === 'cityId') {
      this.State.Request = { ...this.State.Request, unitId: '' };
      this.getUnitList();
      this.getTypeList();
    }

    this.getTaskList();
  }

  setOverLimit(value: string) {
    this.State.Request = { ...this.State.Request, onlyOverLimit: value === '1' };
    this.getTaskList();
  }

  clearFilter() {
    this.State.Request = { cityId: '', unitId: '', type: '', status: '', onlyOverLimit: false };
    this.getUnitList();
    this.getTypeList();
    this.getTaskList();
  }
}
