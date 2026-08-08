import { Component, computed, input, signal } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';
import { ChartItem } from '../barchart/barchart';
import { Skeleton } from '../skeleton/skeleton';
import { TOOLTIP_HIDDEN, Tooltip, TooltipState } from '../tooltip/tooltip';

const COLORS = [
  'var(--color-info)',
  'var(--color-success)',
  'var(--color-warning)',
  'var(--color-error)',
  'var(--color-violet)',
  'var(--color-teal)',
];

@Component({
  selector: 'app-donutchart',
  imports: [Skeleton, Tooltip],
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
        color: item.color || COLORS[index % COLORS.length],
        dash: `${length} ${circumference - length}`,
        offset: -start,
        percent: Math.round((item.value / total) * 100),
      };
      start += length;
      return segment;
    });
  });

  readonly tooltip = signal<TooltipState>(TOOLTIP_HIDDEN);

  showTooltip(segment: { label: string; value: number; percent: number }, event: MouseEvent): void {
    this.tooltip.set({
      text: `${segment.label}: ${segment.value} (%${segment.percent})`,
      x: event.clientX,
      y: event.clientY,
    });
  }

  hideTooltip(): void {
    this.tooltip.set(TOOLTIP_HIDDEN);
  }
}
