import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./transactions/login.start/login.start').then((m) => m.LoginStart),
    },
];
