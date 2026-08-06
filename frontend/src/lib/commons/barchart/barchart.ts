import { Component, computed, input, output } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';

export interface ChartItem {
  label: string;
  value: number;
}

@Component({
  selector: 'app-barchart',
  imports: [],
  templateUrl: './barchart.html',
  styleUrl: './barchart.scss',
})
export class Barchart extends BaseComponent {
  readonly title = input<string>('');

  readonly items = input<ChartItem[]>([]);

  readonly emptyText = input<string>('Veri yok');

  readonly barClicked = output<ChartItem>();

  /** en buyuk deger, cubuk genislikleri buna gore hesaplanir */
  readonly maxValue = computed(() => Math.max(1, ...this.items().map((item) => item.value)));

  percent(value: number): number {
    return (value / this.maxValue()) * 100;
  }
}
