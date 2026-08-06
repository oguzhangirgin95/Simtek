import { Injectable, Signal, computed, inject, signal } from '@angular/core';
import { ActivatedRouteSnapshot, ActivationStart, Router } from '@angular/router';
import { firstValueFrom, isObservable } from 'rxjs';
import { FlowButton, FlowConfig, FlowStep, ServiceConfig, ValidationError } from '../baseconfig/config';
import { BaseService } from './baseservice';
import { Validationservice } from './validationservice';

@Injectable({
  providedIn: 'root',
})
export class FlowService extends BaseService {
  private readonly router = inject(Router);
  private readonly validationService = inject(Validationservice);

  private readonly services = new Map<string, any>();

  private readonly state = new Map<string, any>();

  private readonly stateVersion = signal(0);

  public set(key: string, value: any): void {
    this.state.set(key, value);
    this.stateVersion.update((v) => v + 1);
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
    this.stateVersion.update((v) => v + 1);
  }

  public getPath<T>(path: string): T | undefined {
    const parts = path.split('.');
    const first = this.get<any>(parts[0]);
    return parts.slice(1).reduce((value, key) => (value == null ? undefined : value[key]), first) as T | undefined;
  }

  public readonly transaction = signal<string>('');

  public readonly currentStep = signal<string>('');

  public readonly config = signal<FlowConfig | undefined>(undefined);

  public readonly steps = computed<FlowStep[]>(() => this.config()?.config.steps ?? []);

  public readonly currentStepConfig = computed<FlowStep | undefined>(() =>
    this.steps().find((step) => step.step === this.currentStep()),
  );

  public readonly stepIndex = computed<number>(() => this.steps().findIndex((step) => step.step === this.currentStep()));

  public readonly showContinueButton = computed<boolean>(() => this.currentStepConfig()?.showContinueButton === true);
  public readonly showBackButton = computed<boolean>(() => this.currentStepConfig()?.showBackButton === true);
  public readonly buttons = computed<FlowButton[]>(() => this.currentStepConfig()?.buttons ?? []);

  public readonly errors = this.select<ValidationError[]>('validationErrors');

  constructor() {
    super();
    this.router.events.subscribe((event) => {
      if (event instanceof ActivationStart) {
        this.readRoute(event.snapshot);
      }
    });
  }

  public async next(): Promise<void> {
    const step = this.currentStepConfig();
    if (!step) {
      return;
    }

    const errors = this.validationService.validate(step.validation, (path) => this.validationValue(path));
    this.set('validationErrors', errors);
    if (errors.length > 0) {
      return;
    }

    if (step.service) {
      await this.callService(step.service);
    }

    const nextStep = this.steps()[this.stepIndex() + 1];
    if (nextStep) {
      const opened = await this.goTo(nextStep.step);
      if (!opened) {
        console.warn(`FlowService: '${nextStep.step}' stepi icin route bulunamadi.`);
      }
    }
  }

  public async back(): Promise<void> {
    const previousStep = this.steps()[this.stepIndex() - 1];
    if (previousStep) {
      await this.goTo(previousStep.step);
    }
  }

  public goTo(step: string): Promise<boolean> {
    const segments = this.router.url.split('?')[0].split('/');
    segments[segments.length - 1] = step;
    return this.router.navigateByUrl(segments.join('/'));
  }

  public isButtonVisible(button: FlowButton): boolean {
    const isVisible = button.isVisible;

    if (isVisible === undefined) {
      return true;
    }
    if (typeof isVisible === 'boolean') {
      return isVisible;
    }
    if (typeof isVisible === 'function') {
      return isVisible({ get: <T>(key: string) => this.getPath<T>(key) });
    }
    return this.getPath<boolean>(isVisible.replace(/^state\./, '')) === true;
  }

  public clickButton(button: FlowButton): void {
    if (button.navigate) {
      this.router.navigateByUrl(button.navigate);
    }
  }

  public registerService(name: string, service: any): void {
    this.services.set(name, service);
  }

  private async callService(config: ServiceConfig): Promise<void> {
    const service = this.services.get(config.serviceName);
    const method = service ? service[config.methodName] : undefined;

    if (typeof method !== 'function') {
      console.warn(`FlowService: '${config.serviceName}.${config.methodName}' bulunamadi.`);
      return;
    }

    const params =
      config.params.length > 0
        ? config.params.map((param) => (typeof param === 'string' ? this.getPath(param) : param))
        : [this.get(`${config.methodName}Request`)];

    const result = method.apply(service, params);
    const response = isObservable(result) ? await firstValueFrom(result) : await result;

    this.set(`${config.methodName}Response`, response);
  }

  private validationValue(id: string): any {
    const value = this.getPath(id);
    return value === undefined ? this.getPath(`Request.${id}`) : value;
  }

  private readRoute(snapshot: ActivatedRouteSnapshot): void {
    const config = snapshot.data['config'] as FlowConfig | undefined;
    const step = snapshot.data['step'] as string | undefined;

    if (!config || !step) {
      return;
    }

    const stepConfig = config.config.steps.find((item) => item.step === step);

    if (step === 'start' && stepConfig?.keepState !== true) {
      this.clear();
    }

    this.config.set(config);
    this.transaction.set(snapshot.parent?.url.map((segment) => segment.path).join('/') ?? '');
    this.currentStep.set(step);
  }
}
