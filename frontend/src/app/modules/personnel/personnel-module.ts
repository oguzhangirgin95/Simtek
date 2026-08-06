import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { routes } from './personnel.routes';

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes)],
})
export class PersonnelModule {}
