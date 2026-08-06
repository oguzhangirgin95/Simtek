import { Directive, inject } from '@angular/core';
import { FlowService } from '../baseservice/flow-service';

@Directive()
export abstract class BaseComponent {
  /** tum componentlerin ortak flow servisi */
  protected readonly flowService = inject(FlowService);

  /** proxy ile flowService'e baglanan signal destekli state: this.State.deneme */
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
}
