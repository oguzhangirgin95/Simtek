import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'tasklist',
    },
    {
        path: 'tasklist',
        loadChildren: () => import('./transactions/tasklist/tasklist.routes').then((m) => m.routes),
    },
    {
        path: 'taskassign',
        loadChildren: () => import('./transactions/taskassign/taskassign.routes').then((m) => m.routes),
    },
];
