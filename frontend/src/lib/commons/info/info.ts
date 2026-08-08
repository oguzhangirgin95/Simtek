import { Component, input } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';

export type InfoVariant = 'info' | 'success' | 'warning' | 'error' | 'teal' | 'violet';

@Component({
  selector: 'app-info',
  imports: [],
  templateUrl: './info.html',
  styleUrl: './info.scss',
})
export class Info extends BaseComponent {
  readonly message = input<string>('');

  readonly variant = input<InfoVariant>('info');
}
