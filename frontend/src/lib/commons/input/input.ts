import { Component, input, output } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';

@Component({
  selector: 'app-input',
  imports: [],
  templateUrl: './input.html',
  styleUrl: './input.scss',
})
export class Input extends BaseComponent {
  readonly label = input<string>('');

  readonly value = input<string>('');

  /** text, password, date, number ... */
  readonly type = input<string>('text');

  readonly name = input<string>('');

  readonly disabled = input<boolean>(false);

  readonly error = input<string>('');

  readonly valueChange = output<string>();

  onInput(event: Event) {
    this.valueChange.emit((event.target as HTMLInputElement).value);
  }
}
