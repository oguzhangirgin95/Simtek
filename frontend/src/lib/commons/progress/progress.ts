import { Component, computed, input } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
import { InfoVariant } from '../info/info';

@Component({
  selector: 'app-progress',
  imports: [],
  templateUrl: './progress.html',
  styleUrl: './progress.scss',
})
export class Progress extends BaseComponent {
  readonly label = input<string>('');

  readonly value = input<number>(0);

  readonly max = input<number>(100);

  readonly variant = input<InfoVariant>('info');

  readonly showValue = input<boolean>(true);

  readonly percent = computed(() => {
    const max = this.max() || 1;
    return Math.min(100, Math.max(0, (this.value() / max) * 100));
  });
}
