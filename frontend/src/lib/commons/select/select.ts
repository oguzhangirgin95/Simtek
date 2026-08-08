import { Component, input, output } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
import { Validation } from '../validation/validation';

/** Açılır listedeki tek seçenek. */
export interface SelectOption {
  /** Seçildiğinde dışarı verilen değer. */
  value: string;
  /** Kullanıcının gördüğü metin. */
  text: string;
}

/**
 * Etiketli açılır liste. Doğrulama mesajını kendi altında gösterir.
 *
 * Input gibi değeri içeride tutmaz; seçimi dışarı bildirir ve neyin seçili
 * göründüğünü value girdisinden okur.
 *
 * host'ta id null'a çekiliyor çünkü id'nin asıl sahibi içerideki <select>
 * elemanı; aksi halde <label for> eşleşmesi ve doğrulama hedefi şaşar.
 */
@Component({
  selector: 'app-select',
  host: { '[attr.id]': 'null' },
  imports: [Validation],
  templateUrl: './select.html',
  styleUrl: './select.scss',
})
export class Select extends BaseComponent {
  /** Alan kimliği; etiket eşleşmesi ve doğrulama hatası bu id ile bulunur. */
  readonly id = input<string>('');

  /** Alanın üstünde görünen etiket. Boşsa etiket çizilmez. */
  readonly label = input<string>('');

  /** Seçili seçeneğin değeri. Boş metin, placeholder seçeneğine karşılık gelir. */
  readonly value = input<string>('');

  /** Seçenekler, verildikleri sırayla listelenir. */
  readonly options = input<SelectOption[]>([]);

  /** Boş seçeneğin metni, örneğin "Tümü". Filtrelerde "hepsi" anlamına gelir. */
  readonly placeholder = input<string>('');

  /** Pasif alan açılmaz ve soluk görünür. */
  readonly disabled = input<boolean>(false);

  /** Seçilen seçeneğin değeri. */
  readonly valueChange = output<string>();

  /** Seçim değişimini dışarı bildirir. */
  onChange(event: Event) {
    this.valueChange.emit((event.target as HTMLSelectElement).value);
  }
}
