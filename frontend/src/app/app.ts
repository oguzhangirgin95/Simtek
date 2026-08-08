import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
import { Body } from '@lib/commons/body/body';
import { Footer } from '@lib/commons/footer/footer';
import { Header } from '@lib/commons/header/header';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Body, Footer],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App extends BaseComponent {}
