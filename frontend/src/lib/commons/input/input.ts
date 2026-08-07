import { Component, input, output } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';
import { Validation } from '../validation/validation';

@Component({
  selector: 'app-input',
  host: { '[attr.id]': 'null' },
  imports: [Validation],
  templateUrl: './input.html',
  styleUrl: './input.scss',
})
export class Input extends BaseComponent {
  readonly id = input<string>('');

  readonly label = input<string>('');

  readonly value = input<string>('');

  readonly type = input<string>('text');

  readonly disabled = input<boolean>(false);

  readonly valueChange = output<string>();

  onInput(event: Event) {
    this.valueChange.emit((event.target as HTMLInputElement).value);
  }
}
