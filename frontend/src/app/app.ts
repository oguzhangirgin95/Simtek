import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { BaseComponent } from '../lib/base/basecomponent/basecomponent';
import { CommonsModule } from '../lib/commons/commons-module';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonsModule],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App extends BaseComponent {}
