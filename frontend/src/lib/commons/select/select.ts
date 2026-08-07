import { Component, input, output } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';
import { Validation } from '../validation/validation';

export interface SelectOption {
  value: string;
  text: string;
}

@Component({
  selector: 'app-select',
  host: { '[attr.id]': 'null' },
  imports: [Validation],
  templateUrl: './select.html',
  styleUrl: './select.scss',
})
export class Select extends BaseComponent {
  readonly id = input<string>('');

  readonly label = input<string>('');

  readonly value = input<string>('');

  readonly options = input<SelectOption[]>([]);

  readonly placeholder = input<string>('');

  readonly disabled = input<boolean>(false);

  readonly valueChange = output<string>();

  onChange(event: Event) {
    this.valueChange.emit((event.target as HTMLSelectElement).value);
  }
}
