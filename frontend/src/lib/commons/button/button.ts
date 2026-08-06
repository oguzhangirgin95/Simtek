import { Component, input, output } from '@angular/core';
import { FlowButtonVariant } from '../../base/baseconfig/config';
import { BaseComponent } from '../../base/basecomponent/basecomponent';

@Component({
  selector: 'app-button',
  imports: [],
  templateUrl: './button.html',
  styleUrl: './button.scss',
})
export class Button extends BaseComponent {
  readonly label = input<string>('');

  readonly variant = input<FlowButtonVariant>('primary');

  readonly type = input<'button' | 'submit'>('button');

  readonly disabled = input<boolean>(false);

  readonly clicked = output<void>();
}
