import { Component, input, output } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';

export interface TabItem {
  id: string;
  title: string;
}

@Component({
  selector: 'app-tabs',
  imports: [],
  templateUrl: './tabs.html',
  styleUrl: './tabs.scss',
})
export class Tabs extends BaseComponent {
  readonly tabs = input<TabItem[]>([]);

  readonly active = input<string>('');

  readonly activeChange = output<string>();
}
