import { Component, computed, input } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';
import { ChartItem } from '../barchart/barchart';

const RENKLER = ['#2563eb', '#16a34a', '#d97706', '#dc2626', '#7c3aed', '#0891b2'];

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

  readonly total = computed(() => this.items().reduce((toplam, item) => toplam + item.value, 0));

  /** her dilim icin cevre uzunlugu ve baslangic noktasi hesaplanir */
  readonly segments = computed(() => {
    const total = this.total() || 1;
    const cevre = 2 * Math.PI * 40;
    let baslangic = 0;

    return this.items().map((item, index) => {
      const uzunluk = (item.value / total) * cevre;
      const segment = {
        label: item.label,
        value: item.value,
        color: RENKLER[index % RENKLER.length],
        dash: `${uzunluk} ${cevre - uzunluk}`,
        offset: -baslangic,
        percent: Math.round((item.value / total) * 100),
      };
      baslangic += uzunluk;
      return segment;
    });
  });
}
