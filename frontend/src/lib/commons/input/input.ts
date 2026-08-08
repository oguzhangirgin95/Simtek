import { Component, ElementRef, afterNextRender, inject, input, output } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
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

  private readonly element = inject(ElementRef);

  constructor() {
    super();

    afterNextRender(() => {
      const field = this.element.nativeElement.querySelector('input') as HTMLInputElement | null;

      if (field && field.value !== this.value()) {
        this.valueChange.emit(field.value);
      }
    });
  }

  onInput(event: Event) {
    this.valueChange.emit((event.target as HTMLInputElement).value);
  }
}
