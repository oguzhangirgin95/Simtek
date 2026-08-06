import { Component, input } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';

export interface ListItem {
  key: string;
  value: any;
}

@Component({
  selector: 'app-list',
  imports: [],
  templateUrl: './list.html',
  styleUrl: './list.scss',
})
export class List extends BaseComponent {
  readonly title = input<string>('');

  readonly items = input<ListItem[]>([]);

  readonly emptyText = input<string>('Kayıt yok');
}
