import { Component } from '@angular/core';
import { BaseComponent } from '../../../../../lib/base/basecomponent/base-component';

@Component({
  imports: [],
  templateUrl: './reportentry.start.html',
  styleUrl: './reportentry.scss',
})
export class ReportentryStart extends BaseComponent {
  constructor() {
    super();
  }

  OnInit() { 
     this.State.confirmRequest = {
      username: '',
      password: '',
    };
    this.State.executeRequest = {
      username: '',
      password: '',
    };

  }
}
