import { Component, input, output } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';
import { InfoVariant } from '../info/info';

@Component({
  selector: 'app-statcard',
  imports: [],
  templateUrl: './statcard.html',
  styleUrl: './statcard.scss',
})
export class Statcard extends BaseComponent {
  /** ornek: 'Sahada' */
  readonly label = input<string>('');

  /** ornek: 128 */
  readonly value = input<string | number>('');

  /** kucuk aciklama, ornek: 'gunluk limiti asan 4 kisi' */
  readonly hint = input<string>('');

  readonly variant = input<InfoVariant>('info');

  readonly clicked = output<void>();
}
