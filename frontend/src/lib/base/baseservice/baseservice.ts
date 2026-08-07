import { Injectable, Signal, computed, inject, signal } from '@angular/core';
import { ResourceControllerService } from '../../services/api/resourceController.service';

@Injectable({
  providedIn: 'root',
})
export abstract class BaseService {
  private readonly resourceControllerService = inject(ResourceControllerService);

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
        const gelen: Record<string, string> = {};
        response?.resources?.forEach((item) => (gelen[item.key ?? ''] = item.value ?? ''));
        this.resources.update((current) => ({ ...current, ...gelen }));
      })
      .catch((error) => console.error(`Resource yuklenemedi: ${group}`, error));
  }
}
