import { Component, DOCUMENT, DestroyRef, computed, effect, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { BaseComponent } from '../../base/basecomponent/basecomponent';

interface MenuTransaction {
  path: string;
  text: string;
}

interface MenuModule {
  code: string;
  text: string;
  transactions: MenuTransaction[];
}

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

  readonly title = computed(() => this.getResource('MENU_TITLE', 'Menü'));

  /** Moduller ve altlarindaki islemler */
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

  /** Dar ekranda menunun kendisi acik mi */
  readonly drawerOpen = signal(false);

  /** Acilmis olan modulun kodu */
  readonly openCode = signal('');

  readonly activeModule = computed(
    () => this.modules().find((module) => module.transactions.some((item) => this.isActive(item.path))),
  );

  readonly activeText = computed(() => this.activeModule()?.text ?? this.title());

  constructor() {
    super();
    this.flowService.loadMenu();

    effect(() => this.document.body.classList.toggle('app-menu-open', this.drawerOpen()));

    inject(DestroyRef).onDestroy(() => this.document.body.classList.remove('app-menu-open'));
  }

  isActive(path: string): boolean {
    return this.flowService.url() === path;
  }

  isModuleActive(module: MenuModule): boolean {
    return this.activeModule()?.code === module.code;
  }

  toggleDrawer(): void {
    this.drawerOpen.update((value) => !value);
    this.openCode.set('');
  }

  toggleModule(module: MenuModule): void {
    this.openCode.update((code) => (code === module.code ? '' : module.code));
  }

  closeAll(): void {
    this.openCode.set('');
    this.drawerOpen.set(false);
  }

  go(path: string): void {
    this.closeAll();
    this.router.navigateByUrl(path);
  }
}
