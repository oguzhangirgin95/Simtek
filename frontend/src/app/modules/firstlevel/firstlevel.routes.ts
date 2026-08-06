import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./transactions/login/login.start').then((m) => m.LoginStart),
    },
];
