import { Routes } from '@angular/router';

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
        loadChildren: () => import('./modules/analytics/analytics-module').then((m) => m.AnalyticsModule),
    },
    {
        path: 'monitoring',
        loadChildren: () => import('./modules/monitoring/monitoring-module').then((m) => m.MonitoringModule),
    },
    {
        path: 'operations',
        loadChildren: () => import('./modules/operations/operations-module').then((m) => m.OperationsModule),
    },
    {
        path: 'personnel',
        loadChildren: () => import('./modules/personnel/personnel-module').then((m) => m.PersonnelModule),
    },
    {
        path: 'regions',
        loadChildren: () => import('./modules/regions/regions-module').then((m) => m.RegionsModule),
    },
    {
        path: 'reports',
        loadChildren: () => import('./modules/reports/reports-module').then((m) => m.ReportsModule),
    },
    {
        path: 'settings',
        loadChildren: () => import('./modules/settings/settings-module').then((m) => m.SettingsModule),
    },
    {
        path: 'units',
        loadChildren: () => import('./modules/units/units-module').then((m) => m.UnitsModule),
    },
    {
        path: 'vehicles',
        loadChildren: () => import('./modules/vehicles/vehicles-module').then((m) => m.VehiclesModule),
    },
];
