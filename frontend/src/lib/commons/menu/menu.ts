import { Component, DOCUMENT, DestroyRef, computed, effect, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';

/** Modül altındaki tek bir ekran. */
interface MenuTransaction {
  /** Gidilecek adres. */
  path: string;
  /** Menüde görünen ad. */
  text: string;
}

/** Menüdeki üst seviye başlık ve altındaki ekranlar. */
interface MenuModule {
  /** Modül kodu; hem kaynak anahtarı hem de açık modülü ayırt etmek için kullanılır. */
  code: string;
  /** Menüde görünen başlık. */
  text: string;
  /** Modülün altındaki ekranlar, gösterilecek sırayla. */
  transactions: MenuTransaction[];
}

/**
 * Ana gezinme menüsü. İçeriği sabit değil, sunucudan gelir (FlowService.menu),
 * dolayısıyla kullanıcının yetkisine göre değişebilir.
 *
 * Geniş ekranda şerit, dar ekranda çekmece olarak çalışır; Escape ikisini de
 * kapatır.
 */
@Component({
  selector: 'app-menu',
  imports: [],
  templateUrl: './menu.html',
  styleUrl: './menu.scss',
  host: {
    '(document:keydown.escape)': 'closeAll()',
  },
})
export class Menu extends BaseComponent {
  private readonly router = inject(Router);

  private readonly document = inject(DOCUMENT);

  /** Hiçbir modül açık değilken gösterilen başlık. */
  readonly title = computed(() => this.getResource('MENU_TITLE', 'Menü'));

  /**
   * Modüller ve altlarındaki ekranlar.
   *
   * Başlıklar kaynak anahtarlarıyla çevrilir; karşılığı yoksa sunucunun
   * gönderdiği metin olduğu gibi kullanılır.
   */
  readonly modules = computed<MenuModule[]>(() =>
    this.flowService.menu().map((module) => ({
      code: module.code ?? '',
      text: this.getResource(module.code ?? '', module.title ?? ''),
      transactions: (module.children ?? []).map((transaction) => ({
        path: transaction.path ?? '',
        text: this.getResource(transaction.code ?? '', transaction.title ?? ''),
      })),
    })),
  );

  /** Dar ekranda çekmecenin açık olup olmadığı. */
  readonly drawerOpen = signal(false);

  /** Açık olan modülün kodu. Boş metin "hiçbiri açık değil" demek. */
  readonly openCode = signal('');

  /** Bulunduğumuz ekranı içeren modül. */
  readonly activeModule = computed(
    () => this.modules().find((module) => module.transactions.some((item) => this.isActive(item.path))),
  );

  /** Dar ekranda başlık yerine, içinde bulunduğumuz modülün adı gösterilir. */
  readonly activeText = computed(() => this.activeModule()?.text ?? this.title());

  /** Menü içeriğini yükler ve çekmece açıkken gövde sınıfını yönetir. */
  constructor() {
    super();
    this.flowService.loadMenu();

    // Çekmece açıkken arkadaki sayfa kaymasın diye gövdeye sınıf eklenir.
    effect(() => this.document.body.classList.toggle('app-menu-open', this.drawerOpen()));

    // Çekmece açıkken menü yok edilirse sınıf gövdede asılı kalır ve bütün
    // uygulamada kaydırma kilitli kalırdı; temizleniyor.
    inject(DestroyRef).onDestroy(() => this.document.body.classList.remove('app-menu-open'));
  }

  /** Verilen adres şu an açık olan ekran mı. */
  isActive(path: string): boolean {
    return this.flowService.url() === path;
  }

  /** Modül, içinde bulunduğumuz ekranı barındırıyor mu. */
  isModuleActive(module: MenuModule): boolean {
    return this.activeModule()?.code === module.code;
  }

  /** Dar ekrandaki çekmeceyi açar ya da kapatır. */
  toggleDrawer(): void {
    this.drawerOpen.update((value) => !value);
    this.openCode.set('');
  }

  /**
   * Modülü açar; aynı modüle tekrar basılırsa kapatır. Tek bir kod tutulduğu
   * için aynı anda yalnızca bir modül açık kalır.
   */
  toggleModule(module: MenuModule): void {
    this.openCode.update((code) => (code === module.code ? '' : module.code));
  }

  /** Açık olan her şeyi kapatır. Escape ve gezinme sonrası çağrılır. */
  closeAll(): void {
    this.openCode.set('');
    this.drawerOpen.set(false);
  }

  /** Ekrana gider ve arkasından menüyü kapatır. */
  go(path: string): void {
    this.closeAll();
    this.router.navigateByUrl(path);
  }
}
