import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Header } from './header/header';
import { Body } from './body/body';
import { Footer } from './footer/footer';

@NgModule({
  imports: [CommonModule, Header, Body, Footer],
  exports: [Header, Body, Footer],
})
export class CommonsModule {}
