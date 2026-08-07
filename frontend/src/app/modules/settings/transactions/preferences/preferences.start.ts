import { Component, OnInit, computed, inject } from '@angular/core';
import { BaseComponent } from '../../../../../lib/base/basecomponent/basecomponent';
import { CommonsModule } from '../../../../../lib/commons/commons-module';
import { RegionControllerService } from '../../../../../lib/services/api/regionController.service';
import { SettingControllerService } from '../../../../../lib/services/api/settingController.service';

@Component({
  imports: [CommonsModule],
  templateUrl: './preferences.start.html',
  styleUrl: './preferences.scss',
})
export class PreferencesStart extends BaseComponent implements OnInit {
  private readonly settingService = inject(SettingControllerService);
  private readonly regionService = inject(RegionControllerService);

  readonly labels = computed(() => ({
    title: this.getResource('PREFERENCES_TITLE', 'Ayarlar'),
    formTitle: this.getResource('PREFERENCES_FORM', 'Kullanıcı tercihleri'),
    language: this.getResource('PREFERENCES_LANGUAGE', 'Dil'),
    pageSize: this.getResource('PREFERENCES_PAGESIZE', 'Sayfa başına kayıt'),
    defaultCity: this.getResource('PREFERENCES_DEFAULTCITY', 'Varsayılan şehir'),
    refresh: this.getResource('PREFERENCES_REFRESH', 'Otomatik yenileme (sn)'),
    all: this.getResource('FILTER_ALL', 'Tümü'),
    save: this.getResource('BUTTON_SAVE', 'Kaydet'),
  }));

  readonly languageList = computed(() => [
    { value: 'tr', text: this.getResource('LANGUAGE_TR', 'Türkçe') },
    { value: 'en', text: this.getResource('LANGUAGE_EN', 'İngilizce') },
  ]);

  readonly pageSizeList = [
    { value: '10', text: '10' },
    { value: '20', text: '20' },
    { value: '50', text: '50' },
  ];

  constructor() {
    super();
  }

  ngOnInit() {
    this.State.Request = { token: this.flowService.token(), language: 'tr', pageSize: 20, defaultCityId: '', refreshSeconds: 0 };

    this.getCityList();
    this.getSetting();
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

  getSetting() {
    this.settingService
      .settingGet({ token: this.flowService.token() })
      .toPromise()
      .then((response) => {
        this.State.Request = {
          token: this.flowService.token(),
          language: response?.language ?? 'tr',
          pageSize: response?.pageSize ?? 20,
          defaultCityId: response?.defaultCityId ?? '',
          refreshSeconds: response?.refreshSeconds ?? 0,
        };
      })
      .catch((error) => console.error('Settings:', error));
  }

  saveSetting() {
    this.settingService
      .settingSave(this.State.Request)
      .toPromise()
      .then((response) => {
        this.State.Message = response?.message;
        this.flowService.set('language', response?.language);
      })
      .catch((error) => console.error('Setting save:', error));
  }

  setField(key: string, value: string) {
    const numeric = key === 'pageSize' || key === 'refreshSeconds';
    this.State.Request = { ...this.State.Request, [key]: numeric ? Number(value) : value };
  }
}
