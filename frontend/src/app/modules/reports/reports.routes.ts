import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'reportentry',
    },
    {
        path: 'reportentry',
        loadChildren: () => import('./transactions/reportentry/reportentry.routes').then((m) => m.routes),
    },
];
