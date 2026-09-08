import { isPlatformBrowser } from '@angular/common';
import { Component, DestroyRef, OnInit, PLATFORM_ID, computed, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, EMPTY, firstValueFrom, interval, switchMap } from 'rxjs';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
import { DashboardControllerService } from '@lib/services/api/dashboardController.service';
import { PoliceControllerService } from '@lib/services/api/policeController.service';
import { RegionControllerService } from '@lib/services/api/regionController.service';
import { SettingControllerService } from '@lib/services/api/settingController.service';
import { UnitControllerService } from '@lib/services/api/unitController.service';
import { Barchart } from '@lib/commons/barchart/barchart';
import { Card } from '@lib/commons/card/card';
import { Donutchart } from '@lib/commons/donutchart/donutchart';
import { GenericListConfig, Genericlist } from '@lib/commons/genericlist/genericlist';
import { InfoVariant } from '@lib/commons/info/info';
import { Map } from '@lib/commons/map/map';
import { Select } from '@lib/commons/select/select';
import { Statcard } from '@lib/commons/statcard/statcard';

const STATUS_VARIANT: Record<string, string> = {
  SAHADA: 'success',
  MERKEZDE: 'teal',
  IZINDE: 'warning',
  RAPORLU: 'violet',
};

@Component({
  imports: [Barchart, Card, Donutchart, FormsModule, Genericlist, Map, Select, Statcard],
  templateUrl: './dashboard.start.html',
  styleUrl: './dashboard.scss',
})
export class DashboardStart extends BaseComponent implements OnInit {
  private readonly dashboardService = inject(DashboardControllerService);
  private readonly regionService = inject(RegionControllerService);
  private readonly unitService = inject(UnitControllerService);
  private readonly policeService = inject(PoliceControllerService);
  private readonly settingService = inject(SettingControllerService);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);
  private readonly platformId = inject(PLATFORM_ID);

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
    withinLimit: this.getResource('STAT_WITHINLIMIT', 'Limit içinde'),
    limitChartTitle: this.getResource('LIMIT_CHART_TITLE', 'Görev limiti'),
    mapHint: this.getResource('MAP_HINT', 'Şehre tıklayarak filtreleyin'),
    busiest: this.getResource('DASHBOARD_BUSIEST', 'En yoğun şehir'),
    statusChartTitle: this.getResource('STATUS_CHART_TITLE', 'Durum dağılımı'),
    unitWorkloadTitle: this.getResource('UNIT_WORKLOAD_TITLE', 'birim görev yoğunluğu'),
    emptyUnit: this.getResource('EMPTY_UNIT', 'Birim bulunamadı'),
    mapTotalTitle: this.getResource('MAP_TOTAL_TITLE', 'Şehir bazlı toplam memur'),
    mapTotalActiveTitle: this.getResource('MAP_TOTAL_ACTIVE_TITLE', 'Şehir bazlı sahada olan memur'),
    mapTotalCentralTitle: this.getResource('MAP_TOTAL_CENTRAL_TITLE', 'Şehir bazlı merkezde olan memur'),
    mapTotalLeaveTitle: this.getResource('MAP_TOTAL_LEAVE_TITLE', 'Şehir bazlı izinde olan memur'),
    mapTotalReportTitle: this.getResource('MAP_TOTAL_REPORT_TITLE', 'Şehir bazlı raporlu olan memur'),
    mapTotalOverlimitTitle: this.getResource('MAP_TOTAL_OVERLIMIT_TITLE', 'Şehir bazlı limiti aşan memur'),
  }));

  readonly mapTitle = computed(() => {
    const labels = this.labels() as Record<string, string>;
    const key = this.State.MapMetric?.titleKey ?? '';

    return labels[key] ?? labels['mapTotalTitle'];
  });

  /**
   * Birim tablosunun genericlist yapılandırması.
   *
   * Sütunlar ve satır sonundaki butonlar burada tanımlanır; bileşen yalnızca
   * bunu çizer. Başka bir ekran aynı bileşeni kendi config'i ile kullanır.
   */
  readonly unitListConfig = computed<GenericListConfig>(() => {
    const view = this.getResource('ACTION_VIEW', 'Görüntüle');
    const edit = this.getResource('ACTION_EDIT', 'Düzenle');
    const download = this.getResource('ACTION_EXPORT', 'Dışa aktar');
    const archive = this.getResource('ACTION_ARCHIVE', 'Arşivle');

    return {
      columns: [
        {
          field: 'loadPercent',
          title: this.getResource('GRID_STATUS', 'Durum'),
          type: 'badge',
          format: (unit) => this.getLoadLabel(unit.loadPercent ?? 0),
          variant: (unit) => this.getLoadVariant(unit.loadPercent ?? 0),
        },
        { field: 'unitName', title: this.getResource('GRID_UNIT', 'Birim') },
        { field: 'totalPolice', title: this.getResource('GRID_TOTALPOLICE', 'Personel') },
        { field: 'activePolice', title: this.getResource('GRID_ACTIVEPOLICE', 'Sahada') },
        { field: 'patrol', title: this.getResource('GRID_PATROL', 'Devriye') },
        { field: 'radar', title: this.getResource('GRID_RADAR', 'Radar') },
        { field: 'motorcycle', title: this.getResource('GRID_MOTORCYCLE', 'Motosiklet') },
        { field: 'taskLoad', title: this.getResource('GRID_TASKLOAD', 'Görev') },
      ],
      actions: [
        { key: 'view', label: view, click: (unit) => this.viewUnit(unit) },
        { key: 'edit', label: edit, click: (unit) => this.editUnit(unit) },
        { key: 'export', label: download, variant: 'secondary', click: (unit) => this.exportUnit(unit) },
        {
          key: 'archive',
          label: archive,
          variant: 'secondary',
          visible: (unit) => (unit.loadPercent ?? 0) < 80,
          click: (unit) => this.archiveUnit(unit),
        },
      ],
      emptyText: this.labels().emptyUnit,
    };
  });

  /** Son tıklanan işlem; örneğin çalıştığını göstermek için kartın altında yazar. */
  readonly unitActionText = computed(() => {
    const action = this.State.UnitAction;

    return action ? `${action.label}: ${action.unitName}` : '';
  });

  constructor() {
    super();
  }

  ngOnInit() {
    this.State.Request = { cityId: '', unitId: '', status: '' };

    this.getCityList();
    this.getUnitList();
    this.getStatusList();
    this.getDashboard();
    this.getAutoRefresh();
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

  getStatusList() {
    this.once('StatusList', () => firstValueFrom(this.policeService.policeStatusList({})))
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
    firstValueFrom(this.dashboardService.dashboardSummary(this.State.Request))
      .then((response) => {
        this.State.Summary = response;
        this.State.StatusChart = (response?.statusDistribution ?? []).map((item) => ({
          label: item.name ?? '',
          value: item.count ?? 0,
          color: `var(--color-${STATUS_VARIANT[item.key ?? ''] ?? 'info'})`,
        }));

        const total = response?.totalPolice ?? 0;
        const overLimit = response?.overDailyLimit ?? 0;

        this.State.LimitChart = [
          { label: this.labels().overLimit, value: overLimit, color: 'var(--color-error)' },
          { label: this.labels().withinLimit, value: Math.max(0, total - overLimit), color: 'var(--color-success)' },
        ];
      })
      .catch((error) => console.error('Summary:', error));
  }

  getMapStatistics() {
    const metric = this.State.MapMetric;
    const request = metric ? { ...this.State.Request, status: metric.status } : this.State.Request;

    firstValueFrom(this.dashboardService.mapStatistics(request))
      .then((response) => {
        this.State.MapStatistics = response;
        this.State.MapPoints = (response?.cities ?? []).map((city) => ({
          id: city.cityId ?? '',
          name: city.cityName ?? '',
          x: city.x ?? 0,
          y: city.y ?? 0,
          value: (metric ? city[metric.field as keyof typeof city] : city.totalPolice) ?? 0,
        }));
      })
      .catch((error) => console.error('Map:', error));
  }

  getUnitWorkload() {
    firstValueFrom(this.dashboardService.unitWorkload(this.State.Request))
      .then((response) => {
        this.State.UnitWorkload = response;
        this.State.UnitChart = (response?.units ?? []).map((unit) => ({
          label: unit.unitName ?? '',
          value: unit.taskLoad ?? 0,
        }));
      })
      .catch((error) => console.error('Unit workload:', error));
  }

  getAutoRefresh() {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    this.settingService
      .settingGet({ token: this.flowService.token() })
      .pipe(
        catchError(() => EMPTY),
        switchMap((response) => {
          const seconds = response?.refreshSeconds ?? 0;
          return seconds > 0 ? interval(seconds * 1000) : EMPTY;
        }),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe(() => this.getDashboard());
  }


  setFilter(key: string, value: string) {
    this.State.Request[key] = value;

    if (key === 'cityId') {
      this.State.Request.unitId = '';
      this.getUnitList();
    }

    if (key === 'status') {
      this.State.MapMetric = undefined;
    }

    this.getDashboard();
  }

  selectCity(point: any) {
    this.setFilter('cityId', point.id);
  }

  /** Görev yüküne göre etiket rengi. */
  getLoadVariant(percent: number): InfoVariant {
    if (percent >= 80) {
      return 'error';
    }

    return percent >= 50 ? 'warning' : 'success';
  }

  /** Görev yüküne göre etiket metni. */
  getLoadLabel(percent: number): string {
    if (percent >= 80) {
      return this.getResource('LOAD_HIGH', 'Yoğun');
    }

    return percent >= 50 ? this.getResource('LOAD_NORMAL', 'Normal') : this.getResource('LOAD_LOW', 'Düşük');
  }

  /** Seçilen birimi filtreye taşır. */
  viewUnit(unit: any) {
    this.setUnitAction(this.getResource('ACTION_VIEW', 'Görüntüle'), unit);
    this.setFilter('unitId', unit.unitId ?? '');
  }

  /** Birim düzenleme ekranına gider. */
  editUnit(unit: any) {
    this.setUnitAction(this.getResource('ACTION_EDIT', 'Düzenle'), unit);
    this.router.navigateByUrl('/units/unitlist/start');
  }

  /** Satırı CSV olarak indirir. */
  exportUnit(unit: any) {
    this.setUnitAction(this.getResource('ACTION_EXPORT', 'Dışa aktar'), unit);

    const csv = `${Object.keys(unit).join(';')}
${Object.values(unit).join(';')}`;
    const link = document.createElement('a');

    link.href = URL.createObjectURL(new Blob([csv], { type: 'text/csv' }));
    link.download = `${unit.unitName ?? 'birim'}.csv`;
    link.click();
    URL.revokeObjectURL(link.href);
  }

  /** Birimi arşivler. Gerçek ekranda burada arşivleme servisi çağrılır. */
  archiveUnit(unit: any) {
    this.setUnitAction(this.getResource('ACTION_ARCHIVE', 'Arşivle'), unit);
  }

  /** Yapılan son işlemi kartın üstünde göstermek için saklar. */
  setUnitAction(label: string, unit: any) {
    this.State.UnitAction = { label, unitName: unit.unitName ?? '' };
  }

  selectMetric(metric: { titleKey: string; status?: string; field?: string; variant?: string }) {
    this.State.MapMetric =
      this.State.MapMetric?.titleKey === metric.titleKey
        ? undefined
        : { status: '', field: 'totalPolice', variant: '', ...metric };

    this.getMapStatistics();
  }
}
