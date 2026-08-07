import { Component, computed, input } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';
import { ChartItem } from '../barchart/barchart';

const COLORS = ['#4da3ff', '#2fbf5f', '#f5b301', '#e30a17', '#a78bfa', '#00c7be'];

@Component({
  selector: 'app-donutchart',
  imports: [],
  templateUrl: './donutchart.html',
  styleUrl: './donutchart.scss',
})
export class Donutchart extends BaseComponent {
  readonly title = input<string>('');

  readonly items = input<ChartItem[]>([]);

  readonly emptyText = input<string>('Veri yok');

  readonly total = computed(() => this.items().reduce((sum, item) => sum + item.value, 0));

  readonly segments = computed(() => {
    const total = this.total() || 1;
    const circumference = 2 * Math.PI * 40;
    let start = 0;

    return this.items().map((item, index) => {
      const length = (item.value / total) * circumference;
      const segment = {
        label: item.label,
        value: item.value,
        color: COLORS[index % COLORS.length],
        dash: `${length} ${circumference - length}`,
        offset: -start,
        percent: Math.round((item.value / total) * 100),
      };
      start += length;
      return segment;
    });
  });
}
