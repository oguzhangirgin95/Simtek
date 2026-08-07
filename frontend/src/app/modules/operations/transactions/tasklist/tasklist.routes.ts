import { Routes } from '@angular/router';
import { TasklistConfig } from './tasklist.config';

export const routes: Routes = [
    {
        path: 'start',
        loadComponent: () => import('./tasklist.start').then((m) => m.TasklistStart),
        data: { config: TasklistConfig },
    },
];
