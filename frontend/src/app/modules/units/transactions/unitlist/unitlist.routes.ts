import { Routes } from '@angular/router';
import { UnitlistConfig } from './unitlist.config';

export const routes: Routes = [
    {
        path: 'start',
        loadComponent: () => import('./unitlist.start').then((m) => m.UnitlistStart),
        data: { config: UnitlistConfig },
    },
];
