import { Routes } from '@angular/router';
import { TasklistConfig } from './tasklist.config';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'start',
    },
    {
        path: 'start',
        loadComponent: () => import('./tasklist.start').then((m) => m.TasklistStart),
        data: { config: TasklistConfig, step: 'start' },
    },
];
