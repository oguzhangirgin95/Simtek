import { Routes } from '@angular/router';
import { ReportlistConfig } from './reportlist.config';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'start',
    },
    {
        path: 'start',
        loadComponent: () => import('./reportlist.start').then((m) => m.ReportlistStart),
        data: { config: ReportlistConfig, step: 'start' },
    },
];
