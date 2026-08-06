import { Routes } from '@angular/router';
import { VehiclelistConfig } from './vehiclelist.config';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'start',
    },
    {
        path: 'start',
        loadComponent: () => import('./vehiclelist.start').then((m) => m.VehiclelistStart),
        data: { config: VehiclelistConfig, step: 'start' },
    },
];
