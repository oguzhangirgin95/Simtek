import { Component, OnInit, computed, inject } from '@angular/core';
import { BaseComponent } from '../../../../../lib/base/basecomponent/basecomponent';
import { CommonsModule } from '../../../../../lib/commons/commons-module';
import { DashboardControllerService } from '../../../../../lib/services/api/dashboardController.service';

@Component({
  imports: [CommonsModule],
  templateUrl: './regionlist.start.html',
  styleUrl: './regionlist.scss',
})
export class RegionlistStart extends BaseComponent implements OnInit {
  private readonly dashboardService = inject(DashboardControllerService);

  readonly labels = computed(() => ({
    title: this.getResource('REGIONLIST_TITLE', 'Şehir Bazlı Dağılım'),
    mapTitle: this.getResource('MAP_TITLE', 'Şehir bazlı aktif memur'),
    listTitle: this.getResource('REGIONLIST_LIST', 'Şehirler'),
    listHint: this.getResource('REGIONLIST_HINT', 'Haritada seçmek için satıra tıklayın'),
    totalPolice: this.getResource('STAT_TOTAL', 'Toplam personel'),
    activePolice: this.getResource('STAT_ONDUTY', 'Sahada'),
    busiest: this.getResource('REGIONLIST_BUSIEST', 'En yoğun şehir'),
    empty: this.getResource('REGIONLIST_EMPTY', 'Şehir bulunamadı'),
  }));

  readonly columns = computed(() => [
    { field: 'plateCode', title: this.getResource('GRID_PLATECODE', 'Plaka') },
    { field: 'cityName', title: this.getResource('GRID_CITY', 'Şehir') },
    { field: 'totalPolice', title: this.getResource('GRID_TOTALPOLICE', 'Personel') },
    { field: 'activePolice', title: this.getResource('GRID_ACTIVEPOLICE', 'Sahada') },
    { field: 'activePercent', title: this.getResource('GRID_ACTIVEPERCENT', 'Aktiflik %') },
    { field: 'unitCount', title: this.getResource('GRID_UNITCOUNT', 'Birim') },
  ]);

  constructor() {
    super();
  }

  ngOnInit() {
    this.State.Request = { cityId: '', unitId: '', status: '' };

    this.getMapStatistics();
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

  selectCity(point: any) {
    this.State.SelectedCityId = point.id;
  }

  selectRow(row: any) {
    this.State.SelectedCityId = row.cityId;
  }
}
