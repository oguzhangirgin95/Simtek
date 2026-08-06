import { Component, input, output } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';

export interface SelectOption {
  value: string;
  text: string;
}

@Component({
  selector: 'app-select',
  imports: [],
  templateUrl: './select.html',
  styleUrl: './select.scss',
})
export class Select extends BaseComponent {
  readonly label = input<string>('');

  readonly value = input<string>('');

  readonly options = input<SelectOption[]>([]);

  /** bos secenek yazisi, ornek: 'Tumu' */
  readonly placeholder = input<string>('');

  readonly name = input<string>('');

  readonly disabled = input<boolean>(false);

  readonly valueChange = output<string>();

  onChange(event: Event) {
    this.valueChange.emit((event.target as HTMLSelectElement).value);
  }
}
