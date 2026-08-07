import { Component, OnInit, computed, inject } from '@angular/core';
import { BaseComponent } from '../../../../../lib/base/basecomponent/basecomponent';
import { CommonsModule } from '../../../../../lib/commons/commons-module';
import { PoliceControllerService } from '../../../../../lib/services/api/policeController.service';
import { RegionControllerService } from '../../../../../lib/services/api/regionController.service';
import { TaskControllerService } from '../../../../../lib/services/api/taskController.service';
import { UnitControllerService } from '../../../../../lib/services/api/unitController.service';

@Component({
  imports: [CommonsModule],
  templateUrl: './taskassign.start.html',
  styleUrl: './taskassign.scss',
})
export class TaskassignStart extends BaseComponent implements OnInit {
  private readonly taskService = inject(TaskControllerService);
  private readonly policeService = inject(PoliceControllerService);
  private readonly regionService = inject(RegionControllerService);
  private readonly unitService = inject(UnitControllerService);

  readonly labels = computed(() => ({
    title: this.getResource('TASKASSIGN_TITLE', 'Görev Atama'),
    city: this.getResource('FILTER_CITY', 'Şehir'),
    unit: this.getResource('FILTER_UNIT', 'Birim'),
    all: this.getResource('FILTER_ALL', 'Tümü'),
    police: this.getResource('TASKASSIGN_POLICE', 'Personel'),
    type: this.getResource('FILTER_TASKTYPE', 'Görev tipi'),
    location: this.getResource('TASKASSIGN_LOCATION', 'Görev yeri'),
    startTime: this.getResource('GRID_STARTTIME', 'Başlangıç'),
    endTime: this.getResource('GRID_ENDTIME', 'Bitiş'),
    formTitle: this.getResource('TASKASSIGN_FORM', 'Görev bilgileri'),
    hint: this.getResource('TASKASSIGN_HINT', 'Devam ile onay ekranına geçilir.'),
  }));

  constructor() {
    super();
  }

  ngOnInit() {
    this.State.Request = {
      policeId: '',
      type: '',
      location: '',
      startTime: '16:00',
      endTime: '17:00',
    };

    this.State.Filter = { cityId: '', unitId: '' };

    this.getCityList();
    this.getUnitList();
    this.getTypeList();
    this.getPoliceList();
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

  getUnitList() {
    this.unitService
      .unitList({ cityId: this.State.Filter.cityId })
      .toPromise()
      .then((response) => {
        this.State.UnitList = (response?.units ?? []).map((unit) => ({ value: unit.id, text: unit.name }));
      })
      .catch((error) => console.error('Birim listesi:', error));
  }

  getTypeList() {
    this.taskService
      .taskTypeList({})
      .toPromise()
      .then((response) => {
        this.State.TypeList = (response?.types ?? []).map((type) => ({ value: type.key, text: type.name }));
      })
      .catch((error) => console.error('Gorev tipleri:', error));
  }

  getPoliceList() {
    this.policeService
      .policeList({ cityId: this.State.Filter.cityId, unitId: this.State.Filter.unitId })
      .toPromise()
      .then((response) => {
        this.State.PoliceList = (response?.policeList ?? []).map((police) => ({
          value: police.id,
          text: `${police.badgeNumber} - ${police.fullName}`,
        }));
      })
      .catch((error) => console.error('Personel listesi:', error));
  }

  setFilter(key: string, value: string) {
    this.State.Filter = { ...this.State.Filter, [key]: value };

    if (key === 'cityId') {
      this.State.Filter = { ...this.State.Filter, unitId: '' };
      this.getUnitList();
    }

    this.State.Request = { ...this.State.Request, policeId: '' };
    this.getPoliceList();
  }

  setField(key: string, value: string) {
    this.State.Request = { ...this.State.Request, [key]: value };
  }
}
