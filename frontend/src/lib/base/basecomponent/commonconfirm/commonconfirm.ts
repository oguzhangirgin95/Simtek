import { Component, computed } from '@angular/core';
import { BaseComponent } from '../basecomponent';

@Component({
  imports: [],
  templateUrl: './commonconfirm.html',
  styleUrl: './commonconfirm.scss',
})
export class Commonconfirm extends BaseComponent {
  readonly items = computed(() => {
    const response = this.flowService.get<Record<string, any>>('confirmResponse') ?? {};
    return Object.entries(response).map(([key, value]) => ({ key, value }));
  });
}
