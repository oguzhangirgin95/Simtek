import { Component, input } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
import { InfoVariant } from '../info/info';

@Component({
  selector: 'app-badge',
  imports: [],
  templateUrl: './badge.html',
  styleUrl: './badge.scss',
})
export class Badge extends BaseComponent {
  readonly text = input<string>('');

  readonly variant = input<InfoVariant>('info');
}
