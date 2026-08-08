import { Routes } from '@angular/router';
import { AuthGuard } from '@lib/base/baseguard/authguard';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'firstlevel',
    },
    {
        path: 'firstlevel',
        loadChildren: () => import('./modules/firstlevel/firstlevel.routes').then((m) => m.routes),
    },
    {
        path: 'analytics',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/analytics/analytics.routes').then((m) => m.routes),
    },
    {
        path: 'monitoring',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/monitoring/monitoring.routes').then((m) => m.routes),
    },
    {
        path: 'operations',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/operations/operations.routes').then((m) => m.routes),
    },
    {
        path: 'personnel',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/personnel/personnel.routes').then((m) => m.routes),
    },
    {
        path: 'regions',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/regions/regions.routes').then((m) => m.routes),
    },
    {
        path: 'reports',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/reports/reports.routes').then((m) => m.routes),
    },
    {
        path: 'settings',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/settings/settings.routes').then((m) => m.routes),
    },
    {
        path: 'units',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/units/units.routes').then((m) => m.routes),
    },
    {
        path: 'vehicles',
        canActivate: [AuthGuard],
        loadChildren: () => import('./modules/vehicles/vehicles.routes').then((m) => m.routes),
    },
];
