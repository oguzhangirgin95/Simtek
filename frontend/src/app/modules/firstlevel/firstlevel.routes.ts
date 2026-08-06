import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'login',
    },
    {
        path: 'login',
        loadChildren: () => import('./transactions/login/login.routes').then((m) => m.routes),
    },
];
