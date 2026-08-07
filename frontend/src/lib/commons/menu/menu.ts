import { Component, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { BaseComponent } from '../../base/basecomponent/basecomponent';
import { MenuItemModel } from '../../services/model/menuItemModel';

@Component({
  selector: 'app-menu',
  imports: [],
  templateUrl: './menu.html',
  styleUrl: './menu.scss',
})
export class Menu extends BaseComponent {
  private readonly router = inject(Router);

  readonly title = computed(() => this.getResource('MENU_TITLE', 'Menü'));

  readonly items = computed(() =>
    this.flowService.menu().map((item) => ({
      path: item.path ?? '',
      text: this.getResource(item.code ?? '', item.title ?? ''),
    })),
  );

  constructor() {
    super();
    this.flowService.loadMenu();
  }

  isActive(path: string): boolean {
    return this.router.url === path;
  }

  open(path: string): void {
    this.router.navigateByUrl(path);
  }
}
