import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FirstlevelModule } from './modules/firstlevel/firstlevel-module';
import { AnalyticsModule } from './modules/analytics/analytics-module';
import { MonitoringModule } from './modules/monitoring/monitoring-module';
import { OperationsModule } from './modules/operations/operations-module';
import { PersonnelModule } from './modules/personnel/personnel-module';
import { RegionsModule } from './modules/regions/regions-module';
import { ReportsModule } from './modules/reports/reports-module';
import { SettingsModule } from './modules/settings/settings-module';
import { UnitsModule } from './modules/units/units-module';
import { VehiclesModule } from './modules/vehicles/vehicles-module';
import { CommonsModule } from '../lib/commons/commons-module';
import { BaseModule } from '../lib/base/base-module';

@NgModule({
  declarations: [],
  imports: [CommonModule, FirstlevelModule, AnalyticsModule, MonitoringModule, OperationsModule, PersonnelModule, RegionsModule, ReportsModule, SettingsModule, UnitsModule, VehiclesModule, CommonsModule, BaseModule],
})
export class AppModule {}
