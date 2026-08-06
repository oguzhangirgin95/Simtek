import { Component, input, output } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';

@Component({
  selector: 'app-modal',
  imports: [],
  templateUrl: './modal.html',
  styleUrl: './modal.scss',
})
export class Modal extends BaseComponent {
  readonly title = input<string>('');

  readonly open = input<boolean>(false);

  readonly closed = output<void>();
}
