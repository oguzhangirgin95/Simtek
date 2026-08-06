import { Component } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';

@Component({
  selector: 'app-footer',
  imports: [],
  templateUrl: './footer.html',
  styleUrl: './footer.scss',
})
export class Footer extends BaseComponent {
  readonly year = new Date().getFullYear();
}
