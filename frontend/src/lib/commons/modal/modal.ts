import { Component, DOCUMENT, DestroyRef, effect, inject, input, output } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';

@Component({
  selector: 'app-modal',
  imports: [],
  templateUrl: './modal.html',
  styleUrl: './modal.scss',
  host: {
    '(document:keydown.escape)': 'onEscape()',
  },
})
export class Modal extends BaseComponent {
  private readonly document = inject(DOCUMENT);

  readonly title = input<string>('');

  readonly open = input<boolean>(false);

  readonly closed = output<void>();

  constructor() {
    super();

    effect(() => this.document.body.classList.toggle('app-modal-open', this.open()));

    inject(DestroyRef).onDestroy(() => this.document.body.classList.remove('app-modal-open'));
  }

  onEscape(): void {
    if (this.open()) {
      this.closed.emit();
    }
  }
}
