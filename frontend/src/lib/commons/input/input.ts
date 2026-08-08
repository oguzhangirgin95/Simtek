import { Component, ElementRef, afterNextRender, inject, input, output } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
import { Validation } from '../validation/validation';

/**
 * Etiketli metin girişi. Doğrulama mesajını kendi altında gösterir.
 *
 * Değeri içeride tutmaz: her tuş vuruşunda valueChange yayar ve ekranda ne
 * yazacağını her zaman value girdisinden okur. Gecikme gerekiyorsa (arama
 * alanlarında olduğu gibi) bunu çağıran ekran ayarlar, bileşen karışmaz.
 *
 * host'ta id null'a çekiliyor çünkü id'nin asıl sahibi içerideki <input>
 * elemanı; aksi halde hem sarmalayıcıda hem input'ta aynı id olur, <label for>
 * eşleşmesi ve doğrulama hedefi şaşar.
 */
@Component({
  selector: 'app-input',
  host: { '[attr.id]': 'null' },
  imports: [Validation],
  templateUrl: './input.html',
  styleUrl: './input.scss',
})
export class Input extends BaseComponent {
  /** Alan kimliği; etiket eşleşmesi ve doğrulama hatası bu id ile bulunur. */
  readonly id = input<string>('');

  /** Alanın üstünde görünen etiket. Boşsa etiket çizilmez. */
  readonly label = input<string>('');

  /** Gösterilecek değer. */
  readonly value = input<string>('');

  /** HTML input türü: text, password, number, date gibi. */
  readonly type = input<string>('text');

  /** Pasif alan yazılamaz ve soluk görünür. */
  readonly disabled = input<boolean>(false);

  /** Kullanıcı yazdıkça yayılan güncel değer. */
  readonly valueChange = output<string>();

  private readonly element = inject(ElementRef);

  /** Tarayıcı otomatik doldurmasını yakalamak için ilk çizim sonrası kontrol kurar. */
  constructor() {
    super();

    // Tarayıcının şifre/adres otomatik doldurması alanı ilk çizimden sonra
    // doldurabiliyor ve bu bir input olayı üretmiyor. Kontrol edilmezse
    // kullanıcı dolu bir formu gönderiyor ama bizim elimizdeki değer boş
    // kalıyor; o yüzden ilk çizimden sonra bir kez karşılaştırıp bildiriyoruz.
    afterNextRender(() => {
      const field = this.element.nativeElement.querySelector('input') as HTMLInputElement | null;

      if (field && field.value !== this.value()) {
        this.valueChange.emit(field.value);
      }
    });
  }

  /** Her tuş vuruşunda yeni değeri dışarı bildirir. */
  onInput(event: Event) {
    this.valueChange.emit((event.target as HTMLInputElement).value);
  }
}
