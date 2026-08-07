import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: 'tasktrend',
        loadChildren: () => import('./transactions/tasktrend/tasktrend.routes').then((m) => m.routes),
    },
];
