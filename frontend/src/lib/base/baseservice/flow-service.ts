import { Injectable, Signal, computed, signal } from '@angular/core';
import { BaseService } from './base-service';

@Injectable({
  providedIn: 'root',
})
export class FlowService extends BaseService {
  /** state alanlari */
  private readonly state = new Map<string, any>();

  /** state her degistiginde artar, signal reaktifligi bunun uzerinden calisir */
  private readonly stateVersion = signal(0);

  public set(key: string, value: any): void {
    this.state.set(key, value);
    this.stateVersion.update((v) => v + 1);
  }

  public get<T>(key: string): T | undefined {
    // Register dependency for Angular Signals.
    this.stateVersion();
    return this.state.get(key) as T;
  }

  public select<T>(key: string): Signal<T | undefined> {
    return computed(() => this.get<T>(key));
  }

  public clear(): void {
    this.state.clear();
    this.stateVersion.update((v) => v + 1);
  }
}
