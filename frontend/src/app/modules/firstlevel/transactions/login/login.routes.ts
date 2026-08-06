import { Routes } from '@angular/router';
import { LoginConfig } from './login.config';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'start',
    },
    {
        path: 'start',
        loadComponent: () => import('./login.start').then((m) => m.LoginStart),
        data: { config: LoginConfig, step: 'start' },
    },
];
