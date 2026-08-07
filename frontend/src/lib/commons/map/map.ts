import { Component, computed, input, output, signal } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';
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
  fill: string;
  point?: MapPoint;
}

const NO_DATA = '#16376f';

const SCALE = ['#5c1f34', '#8a1f2f', '#b3151f', '#e30a17'];

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
  imports: [Tooltip],
  templateUrl: './map.html',
  styleUrl: './map.scss',
})
export class Map extends BaseComponent {
  readonly title = input<string>('');

  readonly points = input<MapPoint[]>([]);

  readonly emptyText = input<string>('Veri yok');

  readonly selectedId = input<string>('');

  readonly pointClicked = output<MapPoint>();

  readonly viewBox = TURKEY_VIEWBOX;

  readonly provinces = computed<Province[]>(() => {
    const byName: Record<string, MapPoint> = {};
    this.points().forEach((point) => (byName[plain(point.name)] = point));

    const max = Math.max(1, ...this.points().map((point) => point.value));

    return TURKEY_PROVINCES.map((province) => {
      const point = byName[plain(province.name)];
      return {
        name: province.name,
        path: province.path,
        cx: province.cx,
        cy: province.cy,
        point,
        fill: point ? SCALE[Math.min(SCALE.length - 1, Math.floor((point.value / max) * SCALE.length))] : NO_DATA,
      };
    });
  });

  readonly legend = computed(() => {
    const max = Math.max(1, ...this.points().map((point) => point.value));
    return SCALE.map((color, index) => ({
      color,
      text: `${Math.round((index / SCALE.length) * max)}+`,
    }));
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
