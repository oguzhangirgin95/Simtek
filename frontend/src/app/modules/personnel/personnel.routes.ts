import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: 'policelist',
        loadChildren: () => import('./transactions/policelist/policelist.routes').then((m) => m.routes),
    },
];
