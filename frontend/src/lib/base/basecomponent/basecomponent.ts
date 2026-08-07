import { Directive, inject } from '@angular/core';
import { FlowButton } from '../baseconfig/config';
import { CryptologyService } from '../baseservice/cryptologyservice';
import { FlowService } from '../baseservice/flowservice';
import { ThemeService } from '../baseservice/themeservice';

@Directive()
export abstract class BaseComponent {
  protected readonly flowService = inject(FlowService);

  protected readonly cryptologyService = inject(CryptologyService);

  protected readonly themeService = inject(ThemeService);

  public readonly State = this.flowService.State;

  public readonly currentStep = this.flowService.currentStep;

  public readonly showContinueButton = this.flowService.showContinueButton;
  public readonly showBackButton = this.flowService.showBackButton;

  public readonly buttons = this.flowService.buttons;

  public readonly disableLayout = this.flowService.disableLayout;

  public readonly showHeader = this.flowService.showHeader;
  
  public readonly showFooter = this.flowService.showFooter;

  public readonly loading = this.flowService.loading;

  public readonly serviceError = this.flowService.serviceError;

  public readonly isLoggedIn = this.flowService.isLoggedIn;

  public getResource(key: string, value: string): string {
    return this.flowService.getResource(key, value);
  }

  public encryption(value: string): string {
    return this.cryptologyService.encryption(value);
  }

  public decryption(value: string): string {
    return this.cryptologyService.decryption(value);
  }

  public getError(id: string): string {
    return this.flowService.getError(id);
  }

  public validateCurrentStep(): Promise<boolean> {
    return this.flowService.validateCurrentStep();
  }

  public next(): Promise<void> {
    return this.flowService.next();
  }

  public back(): Promise<void> {
    return this.flowService.back();
  }

  public isButtonVisible(button: FlowButton): boolean {
    return this.flowService.isButtonVisible(button);
  }

  public clickButton(button: FlowButton): void {
    this.flowService.clickButton(button);
  }
}
