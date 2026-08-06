import { Routes } from '@angular/router';
import { PolicelistConfig } from './policelist.config';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'start',
    },
    {
        path: 'start',
        loadComponent: () => import('./policelist.start').then((m) => m.PolicelistStart),
        data: { config: PolicelistConfig, step: 'start' },
    },
];
