import { Component, DOCUMENT, DestroyRef, effect, inject, input, output } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';

/**
 * Ortalanmış diyalog. İçerik ng-content ile dışarıdan verilir.
 *
 * Açık olup olmadığını kendi tutmaz: open girdisiyle yönetilir, kapanmak
 * istediğinde closed yayar. Böylece diyaloğun görünürlüğü onu açan ekranın
 * durumuyla tek bir yerde kalır.
 */
@Component({
  selector: 'app-modal',
  imports: [],
  templateUrl: './modal.html',
  styleUrl: './modal.scss',
  host: {
    '(document:keydown.escape)': 'onEscape()',
  },
})
export class Modal extends BaseComponent {
  private readonly document = inject(DOCUMENT);

  /** Diyaloğun başlığı. */
  readonly title = input<string>('');

  /** Diyaloğun görünür olup olmadığı. */
  readonly open = input<boolean>(false);

  /** Kapatma isteği: çarpıya basıldığında, dışına tıklandığında ya da Escape ile. */
  readonly closed = output<void>();

  /** Diyalog açıkken sayfanın kaymasını engelleyen gövde sınıfını yönetir. */
  constructor() {
    super();

    // Diyalog açıkken arkadaki sayfa kaymasın diye gövdeye sınıf eklenir.
    effect(() => this.document.body.classList.toggle('app-modal-open', this.open()));

    // Diyalog açıkken sayfadan çıkılırsa sınıf gövdede asılı kalır ve bütün
    // uygulamada kaydırma kilitli kalırdı; yok edilirken temizleniyor.
    inject(DestroyRef).onDestroy(() => this.document.body.classList.remove('app-modal-open'));
  }

  /**
   * Escape tuşu. Dinleyici belgeye bağlı olduğu için kapalı diyaloglar da
   * olayı görür; bu yüzden yalnızca açıkken kapatma isteği üretilir.
   */
  onEscape(): void {
    if (this.open()) {
      this.closed.emit();
    }
  }
}
