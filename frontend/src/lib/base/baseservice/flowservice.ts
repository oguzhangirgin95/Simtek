import { Injectable, Injector, computed, effect, inject, signal } from '@angular/core';
import { ActivatedRouteSnapshot, ActivationStart, NavigationEnd, Router } from '@angular/router';
import { firstValueFrom, isObservable } from 'rxjs';
import {
  FlowButton,
  FlowConfig,
  FlowStep,
  ServiceConfig,
  ValidationError,
  ValidationRuleConfig,
} from '../baseconfig/config';
import { BaseService } from './baseservice';
import { Validationservice } from './validationservice';

@Injectable({
  providedIn: 'root',
})
export class FlowService extends BaseService {
  private readonly router = inject(Router);
  private readonly injector = inject(Injector);
  private readonly validationService = inject(Validationservice);

  constructor() {
    super();
    this.router.events.subscribe((event) => {
      if (event instanceof ActivationStart) {
        this.readRoute(event.snapshot);
      }
      if (event instanceof NavigationEnd) {
        this.url.set(event.urlAfterRedirects);
      }
    });

    effect(() => this.writeToken(this.token()));
  }


  public readonly token = signal<string | undefined>(this.readToken());

  private readToken(): string | undefined {
    try {
      return localStorage.getItem('simtek-token') ?? undefined;
    } catch {
      return undefined;
    }
  }

  private writeToken(value: string | undefined): void {
    try {
      value ? localStorage.setItem('simtek-token', value) : localStorage.removeItem('simtek-token');
    } catch {
      return;
    }
  }

  public readonly url = signal<string>('');

  public readonly isLoggedIn = computed<boolean>(() => !!this.token());

  public readonly pendingRequests = signal(0);

  public readonly loading = computed<boolean>(() => this.pendingRequests() > 0);

  public readonly serviceError = this.select<string>('serviceError');

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

  public readonly disableLayout = computed<boolean>(() => this.currentStepConfig()?.disableLayout === true);

  public readonly showHeader = computed<boolean>(() => this.currentStepConfig()?.showHeader !== false);

  public readonly showFooter = computed<boolean>(() => this.currentStepConfig()?.showFooter !== false);

  private readonly validated = signal(false);

  public readonly errors = computed<ValidationError[]>(() => {
    const step = this.currentStepConfig();
    if (!this.validated() || !step) {
      return [];
    }

    const errors = this.validationService.validate(step.validation, (rule) => this.ruleValue(rule));

    return errors.map((error) => ({ id: error.id, message: this.ruleMessage(error.message) }));
  });

  public validateCurrentStep(): Promise<boolean> {
    this.validated.set(true);

    return Promise.resolve(this.errors().length === 0);
  }

  public getError(id: string): string {
    return this.errors().find((error) => error.id === id)?.message ?? '';
  }

  private ruleValue(rule: ValidationRuleConfig): any {
    const path = typeof rule.value === 'string' && rule.value.trim() !== '' ? rule.value.trim() : rule.id;
    return this.getStateValue(path);
  }

  private ruleMessage(message: string): string {
    const index = message.indexOf('|');
    if (index < 0) {
      return message;
    }

    return this.getResource(message.slice(0, index).trim(), message.slice(index + 1).trim());
  }

  public async next(): Promise<void> {
    const isValid = await this.validateCurrentStep();
    if (!isValid) {
      return;
    }

    const nextStep = this.steps()[this.stepIndex() + 1];
    if (nextStep) {
      await this.goTo(nextStep.step);
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

    if (isVisible === undefined || typeof isVisible === 'boolean') {
      return isVisible !== false;
    }
    if (typeof isVisible === 'function') {
      return isVisible({ get: <T>(key: string) => this.getStateValue<T>(key) });
    }

    return this.getStateValue<boolean>(isVisible) === true;
  }

  public clickButton(button: FlowButton): void {
    if (button.navigate) {
      this.router.navigateByUrl(button.navigate);
    }
  }

  private async callService(step: string, config: ServiceConfig): Promise<void> {
    const service = this.injector.get<any>(config.serviceName);
    const params = config.params.map((param) => (typeof param === 'string' ? this.getStateValue(param) : param));
    const result = service[config.methodName](...params);

    this.set(`${step}Response`, isObservable(result) ? await firstValueFrom(result) : await result);
  }

  private readRoute(snapshot: ActivatedRouteSnapshot): void {
    const config = snapshot.data['config'] as FlowConfig | undefined;
    const step = snapshot.routeConfig?.path ?? '';

    if (!config || !step) {
      return;
    }

    const stepConfig = config.config.steps.find((item) => item.step === step);
    const transaction = snapshot.parent?.url.map((segment) => segment.path).join('/') ?? '';
    const previous = this.transaction();

    if (step === 'start' && previous !== '' && previous !== transaction && stepConfig?.keepState !== true) {
      this.clear();
    }

    this.config.set(config);
    this.transaction.set(transaction);
    this.currentStep.set(step);

    this.validated.set(false);

    this.loadResources('general');
    this.loadResources(transaction);

    if (stepConfig?.service) {
      this.callService(step, stepConfig.service).catch((error) => console.error(error));
    }
  }
}
