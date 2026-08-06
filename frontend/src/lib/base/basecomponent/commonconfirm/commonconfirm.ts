import { Component, computed } from '@angular/core';
import { BaseComponent } from '../basecomponent';

@Component({
  imports: [],
  templateUrl: './commonconfirm.html',
  styleUrl: './commonconfirm.scss',
})
export class Commonconfirm extends BaseComponent {
  readonly items = computed(() => {
    const request =
      this.flowService.get<Record<string, any>>('confirmRequest') ??
      this.flowService.get<Record<string, any>>('Request') ??
      {};

    return Object.entries(request).map(([key, value]) => ({ key, value }));
  });
}
