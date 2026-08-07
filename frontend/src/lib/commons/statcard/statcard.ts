import { Component, booleanAttribute, input, output } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';
import { InfoVariant } from '../info/info';

@Component({
  selector: 'app-statcard',
  imports: [],
  templateUrl: './statcard.html',
  styleUrl: './statcard.scss',
})
export class Statcard extends BaseComponent {
  readonly label = input<string>('');

  readonly value = input<string | number>('');

  readonly hint = input<string>('');

  readonly variant = input<InfoVariant>('info');

  readonly clickable = input(false, { transform: booleanAttribute });

  readonly clicked = output<void>();
}
