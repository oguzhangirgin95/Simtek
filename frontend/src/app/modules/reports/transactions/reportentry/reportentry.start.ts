import { Component, OnInit, computed, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { firstValueFrom } from 'rxjs';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
import { RegionControllerService } from '@lib/services/api/regionController.service';
import { ReportEntryControllerService } from '@lib/services/api/reportEntryController.service';
import { UnitControllerService } from '@lib/services/api/unitController.service';
import { Card } from '@lib/commons/card/card';
import { Input } from '@lib/commons/input/input';
import { Select } from '@lib/commons/select/select';

@Component({
  imports: [Card, FormsModule, Input, Select],
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
    firstValueFrom(this.reportService.reportTypeList({}))
      .then((response) => {
        this.State.TypeList = (response?.types ?? []).map((type) => ({ value: type.key, text: type.name }));
      })
      .catch((error) => console.error('Report types:', error));
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

  /** Şehir değişince birim seçimi geçersiz kalır; sıfırlanıp liste yenilenir. */
  setCity(cityId: string) {
    this.State.Request.cityId = cityId;
    this.State.Request.unitId = '';

    this.getUnitList();
  }
}
