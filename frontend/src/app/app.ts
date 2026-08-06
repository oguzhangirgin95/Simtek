import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonsModule } from '../lib/commons/commons-module';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonsModule],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('frontend');
}
