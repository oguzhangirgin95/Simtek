import { Routes } from '@angular/router';
import { AuthGuard } from '../lib/base/baseguard/authguard';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'firstlevel',
    },
    {
        path: 'firstlevel',
        loadChildren: () => import('./modules/firstlevel/firstlevel-module').then((m) => m.FirstlevelModule),
    },
    {
        path: 'analytics',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/analytics/analytics-module').then((m) => m.AnalyticsModule),
    },
    {
        path: 'monitoring',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/monitoring/monitoring-module').then((m) => m.MonitoringModule),
    },
    {
        path: 'operations',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/operations/operations-module').then((m) => m.OperationsModule),
    },
    {
        path: 'personnel',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/personnel/personnel-module').then((m) => m.PersonnelModule),
    },
    {
        path: 'regions',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/regions/regions-module').then((m) => m.RegionsModule),
    },
    {
        path: 'reports',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/reports/reports-module').then((m) => m.ReportsModule),
    },
    {
        path: 'settings',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/settings/settings-module').then((m) => m.SettingsModule),
    },
    {
        path: 'units',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/units/units-module').then((m) => m.UnitsModule),
    },
    {
        path: 'vehicles',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/vehicles/vehicles-module').then((m) => m.VehiclesModule),
    },
];
