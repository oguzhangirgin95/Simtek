import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'tasktrend',
    },
    {
        path: 'tasktrend',
        loadChildren: () => import('./transactions/tasktrend/tasktrend.routes').then((m) => m.routes),
    },
];
