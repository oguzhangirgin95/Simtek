import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: 'vehiclelist',
        loadChildren: () => import('./transactions/vehiclelist/vehiclelist.routes').then((m) => m.routes),
    },
];
