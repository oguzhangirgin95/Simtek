import { Routes } from '@angular/router';
import { RegionlistConfig } from './regionlist.config';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'start',
    },
    {
        path: 'start',
        loadComponent: () => import('./regionlist.start').then((m) => m.RegionlistStart),
        data: { config: RegionlistConfig, step: 'start' },
    },
];
