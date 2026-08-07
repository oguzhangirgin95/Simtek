import { Routes } from '@angular/router';
import { TasktrendConfig } from './tasktrend.config';

export const routes: Routes = [
    {
        path: 'start',
        loadComponent: () => import('./tasktrend.start').then((m) => m.TasktrendStart),
        data: { config: TasktrendConfig },
    },
];
