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
      reportName: '',
      startDate: '',
      endDate: '',
    };
    this.State.executeRequest = {
      reportName: '',
      startDate: '',
      endDate: '',
    };

  }

  // input degisince state'i gunceller (yeni nesne veriyoruz ki signal tetiklensin)
  setField(key: string, event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.State.confirmRequest = { ...this.State.confirmRequest, [key]: value };
    this.State.executeRequest = { ...this.State.executeRequest, [key]: value };
  }
}
