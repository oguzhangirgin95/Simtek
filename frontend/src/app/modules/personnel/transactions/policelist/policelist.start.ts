import { Component, OnInit, computed, inject } from '@angular/core';
import { BaseComponent } from '../../../../../lib/base/basecomponent/basecomponent';
import { CommonsModule } from '../../../../../lib/commons/commons-module';
import { PoliceControllerService } from '../../../../../lib/services/api/policeController.service';
import { RegionControllerService } from '../../../../../lib/services/api/regionController.service';
import { TaskControllerService } from '../../../../../lib/services/api/taskController.service';
import { UnitControllerService } from '../../../../../lib/services/api/unitController.service';
import { VehicleControllerService } from '../../../../../lib/services/api/vehicleController.service';

@Component({
  imports: [CommonsModule],
  templateUrl: './policelist.start.html',
  styleUrl: './policelist.scss',
})
export class PolicelistStart extends BaseComponent implements OnInit {
  private readonly policeService = inject(PoliceControllerService);
  private readonly vehicleService = inject(VehicleControllerService);
  private readonly taskService = inject(TaskControllerService);
  private readonly regionService = inject(RegionControllerService);
  private readonly unitService = inject(UnitControllerService);

  readonly labels = computed(() => ({
    title: this.getResource('POLICELIST_TITLE', 'Personel Listesi'),
    city: this.getResource('FILTER_CITY', 'Şehir'),
    unit: this.getResource('FILTER_UNIT', 'Birim'),
    status: this.getResource('FILTER_STATUS', 'Durum'),
    all: this.getResource('FILTER_ALL', 'Tümü'),
    search: this.getResource('POLICELIST_SEARCH', 'Ad veya sicil'),
    clear: this.getResource('BUTTON_CLEAR', 'Temizle'),
    listTitle: this.getResource('POLICELIST_LIST', 'Personel'),
    listHint: this.getResource('POLICELIST_HINT', 'Detay için satıra tıklayın'),
    detailTitle: this.getResource('POLICELIST_DETAIL', 'Detay'),
    selectFirst: this.getResource('POLICELIST_SELECT', 'Listeden bir personel seçin.'),
    overLimit: this.getResource('POLICELIST_OVERLIMIT', 'Bu personel günlük görev limitini aşmış.'),
    noVehicle: this.getResource('POLICELIST_NOVEHICLE', 'Bu personele tanımlı araç yok.'),
    emptyList: this.getResource('POLICELIST_EMPTY', 'Kayıt bulunamadı'),
    emptyTask: this.getResource('POLICELIST_EMPTYTASK', 'Görev kaydı yok'),
    tabPerson: this.getResource('TAB_PERSON', 'Kişi'),
    tabVehicle: this.getResource('TAB_VEHICLE', 'Araç'),
    tabTask: this.getResource('TAB_TASK', 'Görevler'),
  }));

  readonly tabs = computed(() => [
    { id: 'kisi', title: this.labels().tabPerson },
    { id: 'arac', title: this.labels().tabVehicle },
    { id: 'gorev', title: this.labels().tabTask },
  ]);

  readonly columns = computed(() => [
    { field: 'badgeNumber', title: this.getResource('GRID_BADGE', 'Sicil') },
    { field: 'fullName', title: this.getResource('GRID_FULLNAME', 'Ad Soyad') },
    { field: 'rank', title: this.getResource('GRID_RANK', 'Rütbe') },
    { field: 'cityName', title: this.getResource('GRID_CITY', 'Şehir') },
    { field: 'unitName', title: this.getResource('GRID_UNIT', 'Birim') },
    { field: 'statusName', title: this.getResource('GRID_STATUS', 'Durum') },
    { field: 'taskTypeName', title: this.getResource('GRID_TASKTYPE', 'Görev') },
    { field: 'score', title: this.getResource('GRID_SCORE', 'Puan') },
  ]);

  readonly taskColumns = computed(() => [
    { field: 'startTime', title: this.getResource('GRID_STARTTIME', 'Başlangıç') },
    { field: 'endTime', title: this.getResource('GRID_ENDTIME', 'Bitiş') },
    { field: 'typeName', title: this.getResource('GRID_TASKTYPE', 'Görev') },
    { field: 'location', title: this.getResource('GRID_LOCATION', 'Konum') },
    { field: 'statusName', title: this.getResource('GRID_STATUS', 'Durum') },
  ]);

  constructor() {
    super();
  }

  ngOnInit() {
    this.State.Request = { cityId: '', unitId: '', status: '', searchText: '' };
    this.State.ActiveTab = 'kisi';

    this.getCityList();
    this.getUnitList();
    this.getStatusList();
    this.getPoliceList();
  }

  /* ---------------- filtre secenekleri ---------------- */

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
      .unitList({ cityId: this.State.Request.cityId })
      .toPromise()
      .then((response) => {
        this.State.UnitList = (response?.units ?? []).map((unit) => ({ value: unit.id, text: unit.name }));
      })
      .catch((error) => console.error('Birim listesi:', error));
  }

  getStatusList() {
    this.policeService
      .policeStatusList({})
      .toPromise()
      .then((response) => {
        this.State.StatusList = (response?.statuses ?? []).map((status) => ({
          value: status.key,
          text: status.name,
        }));
      })
      .catch((error) => console.error('Durum listesi:', error));
  }

  /* ---------------- liste ---------------- */

  getPoliceList() {
    this.policeService
      .policeList(this.State.Request)
      .toPromise()
      .then((response) => {
        this.State.PoliceList = response?.policeList ?? [];
        this.State.TotalCount = response?.totalCount ?? 0;
      })
      .catch((error) => console.error('Personel listesi:', error));
  }

  setFilter(key: string, value: string) {
    this.State.Request = { ...this.State.Request, [key]: value };

    // sehir degisince birim listesi yenilenir
    if (key === 'cityId') {
      this.State.Request = { ...this.State.Request, unitId: '' };
      this.getUnitList();
    }

    this.getPoliceList();
  }

  clearFilter() {
    this.State.Request = { cityId: '', unitId: '', status: '', searchText: '' };
    this.getUnitList();
    this.getPoliceList();
  }

  /** ayni alana tekrar basilirsa yon degisir */
  sortBy(field: string) {
    const sameField = this.State.Request.sortField === field;
    const direction = sameField && this.State.Request.sortDirection === 'ASC' ? 'DESC' : 'ASC';

    this.State.Request = { ...this.State.Request, sortField: field, sortDirection: direction };
    this.getPoliceList();
  }

  /* ---------------- detay ---------------- */

  selectPolice(row: any) {
    this.State.SelectedPoliceId = row.id;
    this.State.ActiveTab = 'kisi';

    this.getPoliceDetail(row.id);
    this.getVehicleDetail(row.id);
    this.getTaskList(row.id);
  }

  getPoliceDetail(policeId: string) {
    this.policeService
      .policeDetail({ policeId: policeId })
      .toPromise()
      .then((response) => {
        this.State.PoliceDetail = response;
        this.State.PoliceItems = [
          { key: this.getResource('GRID_BADGE', 'Sicil'), value: response?.badgeNumber },
          { key: this.getResource('DETAIL_AGE', 'Yaş'), value: response?.age },
          { key: this.getResource('GRID_RANK', 'Rütbe'), value: response?.rank },
          { key: this.getResource('GRID_SCORE', 'Puan'), value: response?.score },
          { key: this.getResource('GRID_CITY', 'Şehir'), value: response?.cityName },
          { key: this.getResource('GRID_UNIT', 'Birim'), value: response?.unitName },
          { key: this.getResource('GRID_STATUS', 'Durum'), value: response?.statusName },
          { key: this.getResource('GRID_TASKTYPE', 'Görev'), value: response?.taskTypeName },
          {
            key: this.getResource('DETAIL_DAILYTASK', 'Günlük görev'),
            value: `${response?.dailyTaskCount} / ${response?.dailyTaskLimit}`,
          },
          { key: this.getResource('DETAIL_PHONE', 'Telefon'), value: response?.phone },
          { key: this.getResource('DETAIL_STARTDATE', 'Göreve başlama'), value: response?.startDate },
        ];
      })
      .catch((error) => console.error('Personel detayi:', error));
  }

  getVehicleDetail(policeId: string) {
    this.vehicleService
      .vehicleDetail({ policeId: policeId })
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
        ];
      })
      .catch((error) => console.error('Arac detayi:', error));
  }

  getTaskList(policeId: string) {
    this.taskService
      .taskList({ policeId: policeId })
      .toPromise()
      .then((response) => {
        this.State.TaskList = response?.tasks ?? [];
      })
      .catch((error) => console.error('Gorev listesi:', error));
  }

  setTab(tabId: string) {
    this.State.ActiveTab = tabId;
  }
}
