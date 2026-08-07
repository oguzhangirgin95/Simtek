import { Injectable, Signal, computed, inject, signal } from '@angular/core';
import { MenuControllerService } from '../../services/api/menuController.service';
import { ResourceControllerService } from '../../services/api/resourceController.service';
import { MenuItemModel } from '../../services/model/menuItemModel';

@Injectable({
  providedIn: 'root',
})
export abstract class BaseService {
  private readonly resourceControllerService = inject(ResourceControllerService);

  private readonly menuControllerService = inject(MenuControllerService);

  private readonly state = new Map<string, any>();

  private readonly stateVersion = signal(0);

  public readonly State: any = new Proxy(
    {},
    {
      get: (target, key: string) => this.get(key),
      set: (target, key: string, value: any) => {
        this.set(key, value);
        return true;
      },
    },
  );

  public set(key: string, value: any): void {
    this.state.set(key, value);
    this.stateVersion.update((version) => version + 1);
  }

  public get<T>(key: string): T | undefined {
    this.stateVersion();
    return this.state.get(key) as T;
  }

  public select<T>(key: string): Signal<T | undefined> {
    return computed(() => this.get<T>(key));
  }

  public clear(): void {
    this.state.clear();
    this.stateVersion.update((version) => version + 1);
  }

  public getStateValue<T>(path: string): T | undefined {
    return path
      .split('.')
      .filter((key) => key !== 'State')
      .reduce<any>((value, key) => value?.[key], this.State);
  }

  private readonly resources = signal<Record<string, string>>({});

  private readonly loadedResources = new Set<string>();

  public getResource(key: string, value: string): string {
    return this.resources()[key] ?? value;
  }

  public loadResources(group: string): void {
    if (!group || this.loadedResources.has(group)) {
      return;
    }
    this.loadedResources.add(group);

    this.resourceControllerService
      .get({ transactionName: group })
      .toPromise()
      .then((response) => {
        const loaded: Record<string, string> = {};
        response?.resources?.forEach((item) => (loaded[item.key ?? ''] = item.value ?? ''));
        this.resources.update((current) => ({ ...current, ...loaded }));
      })
      .catch((error) => console.error(`Resource load failed: ${group}`, error));
  }

  private readonly menuItems = signal<MenuItemModel[]>([]);

  private menuLoaded = false;

  public readonly menu = this.menuItems.asReadonly();

  public loadMenu(): void {
    if (this.menuLoaded) {
      return;
    }
    this.menuLoaded = true;

    this.menuControllerService
      .menuList({})
      .toPromise()
      .then((response) => this.menuItems.set(response?.items ?? []))
      .catch((error) => console.error('Menu load failed:', error));
  }
}
