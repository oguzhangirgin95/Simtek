import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: 'unitlist',
        loadChildren: () => import('./transactions/unitlist/unitlist.routes').then((m) => m.routes),
    },
];
