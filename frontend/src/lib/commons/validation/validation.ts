import { Component, input } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';

@Component({
  selector: 'app-validation',
  imports: [],
  templateUrl: './validation.html',
  styleUrl: './validation.scss',
})
export class Validation extends BaseComponent {
  readonly id = input<string>('');
}
