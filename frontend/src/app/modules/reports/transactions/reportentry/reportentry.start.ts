import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../../../../lib/base/basecomponent/basecomponent';

@Component({
  imports: [],
  templateUrl: './reportentry.start.html',
  styleUrl: './reportentry.start.scss',
})
export class ReportentryStart extends BaseComponent implements OnInit {
  constructor() {
    super();
  }

  ngOnInit() {
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
