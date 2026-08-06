import { Component, OnInit, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { BaseComponent } from '../../../../../lib/base/basecomponent/basecomponent';
import { LoginControllerService } from '../../../../../lib/services/api/loginController.service';
import { LoginResponse } from '../../../../../lib/services/model/loginResponse';

@Component({
  selector: 'app-login-start',
  imports: [],
  templateUrl: './login.start.html',
  styleUrl: './login.scss',
})
export class LoginStart extends BaseComponent implements OnInit {
  private readonly loginService = inject(LoginControllerService);
  private readonly router = inject(Router);

  readonly title = computed(() => this.getResource('LOGIN_TITLE', 'Giriş'));

  readonly usernameLabel = computed(() => this.getResource('LOGIN_USERNAME', 'Kullanıcı adı'));

  readonly passwordLabel = computed(() => this.getResource('LOGIN_PASSWORD', 'Şifre'));

  readonly loginButton = computed(() => this.getResource('LOGIN_BUTTON', 'Giriş yap'));

  constructor() {
    super();
  }

  ngOnInit() {
    this.State.Request = {
      username: '',
      password: '',
    };
  }

  setField(key: string, event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.State.Request = { ...this.State.Request, [key]: value };
  }

  login() {
    this.loginService.login(this.State.Request).toPromise().then((response: LoginResponse | undefined) => {
        if (response?.token) {
          this.flowService.token.set(response.token);
          this.router.navigateByUrl('/monitoring/dashboard');
        } else {
          this.State.loginError = 'Kullanıcı adı veya şifre hatalı.';
        }
    }).catch(error => {
      console.error('Login error:', error);
    });
  }

}
