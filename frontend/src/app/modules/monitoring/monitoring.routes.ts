import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: 'dashboard',
        loadComponent: () => import('./transactions/dashboard/dashboard.start').then((m) => m.DashboardStart),
    },
];