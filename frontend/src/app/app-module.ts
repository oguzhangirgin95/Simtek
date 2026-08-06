import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseModule } from '../lib/base/base-module';

// Feature modulleri (firstlevel, analytics, ...) buraya import ETMEYIN:
// app.routes.ts uzerinden loadChildren ile lazy yuklenirler; buraya eklemek
// lazy loading'i bozar ve RouterModule.forChild route'larini root'a tasir.
@NgModule({
  imports: [CommonModule, BaseModule],
})
export class AppModule {}
