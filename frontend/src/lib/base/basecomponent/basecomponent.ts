import { Directive, inject } from '@angular/core';
import { FlowButton } from '../baseconfig/config';
import { FlowService } from '../baseservice/flowservice';

@Directive()
export abstract class BaseComponent {
  protected readonly flowService = inject(FlowService);

  public State: any = new Proxy(
    {},
    {
      get: (target, prop: string) => this.flowService.get(prop),
      set: (target, prop: string, value: any) => {
        this.flowService.set(prop, value);
        return true;
      },
    },
  );

  public readonly currentStep = this.flowService.currentStep;

  public readonly showContinueButton = this.flowService.showContinueButton;
  public readonly showBackButton = this.flowService.showBackButton;

  public readonly buttons = this.flowService.buttons;

  public readonly disableLayout = this.flowService.disableLayout;

  public readonly showHeader = this.flowService.showHeader;
  
  public readonly showFooter = this.flowService.showFooter;

  public readonly errors = this.flowService.errors;

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
