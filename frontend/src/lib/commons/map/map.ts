import { Component, computed, input, output } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';

export interface MapPoint {
  id: string;
  name: string;
  x: number;
  y: number;
  value: number;
}

@Component({
  selector: 'app-map',
  imports: [],
  templateUrl: './map.html',
  styleUrl: './map.scss',
})
export class Map extends BaseComponent {
  readonly title = input<string>('');

  readonly points = input<MapPoint[]>([]);

  readonly imageUrl = input<string>('');

  readonly emptyText = input<string>('Veri yok');

  readonly pointClicked = output<MapPoint>();

  readonly selectedId = input<string>('');

  private readonly maxValue = computed(() => Math.max(1, ...this.points().map((point) => point.value)));

  radius(value: number): number {
    return 1.5 + (value / this.maxValue()) * 3.5;
  }
}
