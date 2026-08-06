import { Component, computed } from '@angular/core';
import { BaseComponent } from '../basecomponent';

@Component({
  imports: [],
  templateUrl: './commonexecute.html',
  styleUrl: './commonexecute.scss',
})
export class Commonexecute extends BaseComponent {
  readonly items = computed(() => {
    const response = this.flowService.get<Record<string, any>>('executeResponse') ?? {};
    return Object.entries(response).map(([key, value]) => ({ key, value }));
  });
}
