import { Component, input } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';
import { ListItem } from '../list/list';

@Component({
  selector: 'app-detailcard',
  imports: [],
  templateUrl: './detailcard.html',
  styleUrl: './detailcard.scss',
})
export class Detailcard extends BaseComponent {
  /** ornek: polis adi soyadi */
  readonly title = input<string>('');

  /** ornek: rutbe */
  readonly subtitle = input<string>('');

  /** fotograf adresi (polis fotosu, arac fotosu ...) */
  readonly imageUrl = input<string>('');

  /** alan listesi: yas, puan, plaka ... */
  readonly items = input<ListItem[]>([]);

  readonly emptyText = input<string>('Kayıt seçilmedi');
}
