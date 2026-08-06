import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../../../../lib/base/basecomponent/basecomponent';

@Component({
  selector: 'app-login-start',
  imports: [],
  templateUrl: './login.start.html',
  styleUrl: './login.scss',
})
export class LoginStart extends BaseComponent implements OnInit {
  constructor() {
    super();
  }

  ngOnInit() {
    this.State.Request = {
      username: '',
      password: '',
    };
  }

  /** input degisince state'i gunceller (yeni nesne veriyoruz ki signal tetiklensin) */
  setField(key: string, event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.State.Request = { ...this.State.Request, [key]: value };
  }
}
