import { Component, booleanAttribute, computed, input, output, signal } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
import { Skeleton } from '../skeleton/skeleton';
import { TOOLTIP_HIDDEN, Tooltip, TooltipState } from '../tooltip/tooltip';

export interface ChartItem {
  label: string;
  value: number;
  color?: string;
}

@Component({
  selector: 'app-barchart',
  imports: [Skeleton, Tooltip],
  templateUrl: './barchart.html',
  styleUrl: './barchart.scss',
})
export class Barchart extends BaseComponent {
  readonly title = input<string>('');

  readonly items = input<ChartItem[]>([]);

  readonly emptyText = input<string>('Veri yok');

  readonly clickable = input(false, { transform: booleanAttribute });

  readonly barClicked = output<ChartItem>();

  readonly maxValue = computed(() => Math.max(1, ...this.items().map((item) => item.value)));

  percent(value: number): number {
    return (value / this.maxValue()) * 100;
  }

  readonly tooltip = signal<TooltipState>(TOOLTIP_HIDDEN);

  readonly total = computed(() => this.items().reduce((sum, item) => sum + item.value, 0));

  showTooltip(item: ChartItem, event: MouseEvent): void {
    const share = this.total() === 0 ? 0 : Math.round((item.value / this.total()) * 100);

    this.tooltip.set({
      text: `${item.label}: ${item.value} (%${share})`,
      x: event.clientX,
      y: event.clientY,
    });
  }

  hideTooltip(): void {
    this.tooltip.set(TOOLTIP_HIDDEN);
  }
}
