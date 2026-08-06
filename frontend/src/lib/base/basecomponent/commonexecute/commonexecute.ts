import { Component } from '@angular/core';
import { BaseComponent } from '../basecomponent';

@Component({
  imports: [],
  templateUrl: './commonexecute.html',
  styleUrl: './commonexecute.scss',
})
export class Commonexecute extends BaseComponent {
  readonly response = this.flowService.select<any>('executeResponse');
}
