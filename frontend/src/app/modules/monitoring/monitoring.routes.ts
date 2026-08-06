import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'dashboard',
    },
    {
        path: 'dashboard',
        loadChildren: () => import('./transactions/dashboard/dashboard.routes').then((m) => m.routes),
    },
];
