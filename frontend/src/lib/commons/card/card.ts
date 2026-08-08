import { Component, input } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';

@Component({
  selector: 'app-card',
  imports: [],
  templateUrl: './card.html',
  styleUrl: './card.scss',
})
export class Card extends BaseComponent {
  readonly title = input<string>('');

  readonly subtitle = input<string>('');
}
