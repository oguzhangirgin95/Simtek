import { Component, input } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
import { Skeleton } from '../skeleton/skeleton';

export interface ListItem {
  key: string;
  value: any;
}

@Component({
  selector: 'app-list',
  imports: [Skeleton],
  templateUrl: './list.html',
  styleUrl: './list.scss',
})
export class List extends BaseComponent {
  readonly title = input<string>('');

  readonly items = input<ListItem[]>([]);

  readonly emptyText = input<string>('Kayıt yok');
}
