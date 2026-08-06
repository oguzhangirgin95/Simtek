import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'vehiclelist',
    },
    {
        path: 'vehiclelist',
        loadChildren: () => import('./transactions/vehiclelist/vehiclelist.routes').then((m) => m.routes),
    },
];
