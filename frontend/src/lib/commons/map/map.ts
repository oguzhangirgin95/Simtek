import { Component, computed, input, output, signal } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
import { InfoVariant } from '../info/info';
import { Skeleton } from '../skeleton/skeleton';
import { TOOLTIP_HIDDEN, Tooltip, TooltipState } from '../tooltip/tooltip';
import { TURKEY_PROVINCES, TURKEY_VIEWBOX } from './turkey-map';

export interface MapPoint {
  id: string;
  name: string;
  x: number;
  y: number;
  value: number;
}

interface Province {
  name: string;
  path: string;
  cx: number;
  cy: number;
  opacity: number;
  point?: MapPoint;
}

const OPACITY = [0.4, 0.55, 0.7, 0.85, 1];

function plain(text: string): string {
  return text
    .toLowerCase()
    .replace(/ı/g, 'i')
    .replace(/ş/g, 's')
    .replace(/ğ/g, 'g')
    .replace(/ç/g, 'c')
    .replace(/ö/g, 'o')
    .replace(/ü/g, 'u')
    .replace(/i̇/g, 'i')
    .trim();
}

@Component({
  selector: 'app-map',
  imports: [Skeleton, Tooltip],
  templateUrl: './map.html',
  styleUrl: './map.scss',
})
export class Map extends BaseComponent {
  readonly title = input<string>('');

  readonly points = input<MapPoint[]>([]);

  readonly emptyText = input<string>('Veri yok');

  readonly selectedId = input<string>('');

  readonly variant = input<InfoVariant | ''>('');

  readonly pointClicked = output<MapPoint>();

  readonly viewBox = TURKEY_VIEWBOX;

  private readonly thresholds = computed<number[]>(() => {
    const values = [...new Set(this.points().map((point) => point.value))].sort((a, b) => a - b);

    return OPACITY.map((_, index) => values[Math.floor((index * values.length) / OPACITY.length)] ?? 0);
  });

  private opacityOf(value: number): number {
    const index = this.thresholds().filter((threshold) => value >= threshold).length - 1;

    return OPACITY[Math.max(0, index)];
  }

  readonly provinces = computed<Province[]>(() => {
    const byName: Record<string, MapPoint> = {};
    this.points().forEach((point) => (byName[plain(point.name)] = point));

    return TURKEY_PROVINCES.map((province) => {
      const point = byName[plain(province.name)];
      return {
        name: province.name,
        path: province.path,
        cx: province.cx,
        cy: province.cy,
        point,
        opacity: point ? this.opacityOf(point.value) : 1,
      };
    });
  });

  readonly legend = computed(() => {
    const thresholds = this.thresholds();

    return thresholds
      .map((threshold, index) => ({ threshold, opacity: OPACITY[index], text: `${threshold}+` }))
      .filter((item, index) => index === thresholds.length - 1 || item.threshold !== thresholds[index + 1]);
  });

  isSelected(province: Province): boolean {
    return !!province.point && province.point.id === this.selectedId();
  }

  select(province: Province): void {
    if (province.point) {
      this.pointClicked.emit(province.point);
    }
  }

  readonly tooltip = signal<TooltipState>(TOOLTIP_HIDDEN);

  showTooltip(province: Province, event: MouseEvent): void {
    const value = province.point ? String(province.point.value) : this.emptyText();

    this.tooltip.set({
      text: `${province.name}: ${value}`,
      x: event.clientX,
      y: event.clientY,
    });
  }

  hideTooltip(): void {
    this.tooltip.set(TOOLTIP_HIDDEN);
  }
}
