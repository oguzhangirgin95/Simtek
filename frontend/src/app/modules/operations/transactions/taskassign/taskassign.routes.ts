import { Routes } from '@angular/router';
import { TaskassignConfig } from './taskassign.config';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'start',
    },
    {
        path: 'start',
        loadComponent: () => import('./taskassign.start').then((m) => m.TaskassignStart),
        data: { config: TaskassignConfig, step: 'start' },
    },
    {
        path: 'confirm',
        loadComponent: () =>
            import('../../../../../lib/base/basecomponent/commonconfirm/commonconfirm').then((m) => m.Commonconfirm),
        data: { config: TaskassignConfig, step: 'confirm' },
    },
    {
        path: 'execute',
        loadComponent: () =>
            import('../../../../../lib/base/basecomponent/commonexecute/commonexecute').then((m) => m.Commonexecute),
        data: { config: TaskassignConfig, step: 'execute' },
    },
];
