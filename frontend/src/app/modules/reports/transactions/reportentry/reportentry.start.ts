import { Component, OnInit, computed, inject } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
import { RegionControllerService } from '@lib/services/api/regionController.service';
import { ReportEntryControllerService } from '@lib/services/api/reportEntryController.service';
import { UnitControllerService } from '@lib/services/api/unitController.service';
import { Card } from '@lib/commons/card/card';
import { Input } from '@lib/commons/input/input';
import { Select } from '@lib/commons/select/select';

@Component({
  imports: [Card, Input, Select],
  templateUrl: './reportentry.start.html',
  styleUrl: './reportentry.start.scss',
})
export class ReportentryStart extends BaseComponent implements OnInit {
  private readonly reportService = inject(ReportEntryControllerService);
  private readonly regionService = inject(RegionControllerService);
  private readonly unitService = inject(UnitControllerService);

  readonly labels = computed(() => ({
    title: this.getResource('REPORTENTRY_TITLE', 'Rapor Girişi'),
    formTitle: this.getResource('REPORTENTRY_FORM', 'Rapor bilgileri'),
    hint: this.getResource('REPORTENTRY_HINT', 'Devam ile kapsam özetine geçilir.'),
    name: this.getResource('REPORTENTRY_NAME', 'Rapor adı'),
    type: this.getResource('REPORTENTRY_TYPE', 'Rapor tipi'),
    city: this.getResource('FILTER_CITY', 'Şehir'),
    unit: this.getResource('FILTER_UNIT', 'Birim'),
    all: this.getResource('FILTER_ALL', 'Tümü'),
    startDate: this.getResource('REPORTENTRY_STARTDATE', 'Başlangıç tarihi'),
    endDate: this.getResource('REPORTENTRY_ENDDATE', 'Bitiş tarihi'),
  }));

  constructor() {
    super();
  }

  ngOnInit() {
    if (!this.State.Request) {
      this.State.Request = {
        reportName: '',
        reportType: '',
        cityId: '',
        unitId: '',
        startDate: '',
        endDate: '',
      };
    }

    this.getTypeList();
    this.getCityList();
    this.getUnitList();
  }

  getTypeList() {
    this.reportService
      .reportTypeList({})
      .toPromise()
      .then((response) => {
        this.State.TypeList = (response?.types ?? []).map((type) => ({ value: type.key, text: type.name }));
      })
      .catch((error) => console.error('Report types:', error));
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

  setField(key: string, value: string) {
    this.State.Request = { ...this.State.Request, [key]: value };

    if (key === 'cityId') {
      this.State.Request = { ...this.State.Request, unitId: '' };
      this.getUnitList();
    }
  }
}
