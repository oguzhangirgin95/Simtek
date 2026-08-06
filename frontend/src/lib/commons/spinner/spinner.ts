import { Component, input } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';

@Component({
  selector: 'app-spinner',
  imports: [],
  templateUrl: './spinner.html',
  styleUrl: './spinner.scss',
})
export class Spinner extends BaseComponent {
  readonly text = input<string>('');

  readonly size = input<number>(24);
}
