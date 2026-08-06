import { Component, input } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';
import { InfoVariant } from '../info/info';

@Component({
  selector: 'app-badge',
  imports: [],
  templateUrl: './badge.html',
  styleUrl: './badge.scss',
})
export class Badge extends BaseComponent {
  /** ornek: 'Sahada', 'Izinde', 'Raporlu' */
  readonly text = input<string>('');

  readonly variant = input<InfoVariant>('info');
}
