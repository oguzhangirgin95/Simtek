import { Component, input } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';
import { Skeleton } from '../skeleton/skeleton';
import { ListItem } from '../list/list';

@Component({
  selector: 'app-detailcard',
  imports: [Skeleton],
  templateUrl: './detailcard.html',
  styleUrl: './detailcard.scss',
})
export class Detailcard extends BaseComponent {
  readonly title = input<string>('');

  readonly subtitle = input<string>('');

  readonly imageUrl = input<string>('');

  readonly items = input<ListItem[]>([]);

  readonly emptyText = input<string>('Kayıt seçilmedi');
}
