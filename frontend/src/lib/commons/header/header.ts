import { Component, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { BaseComponent } from '../../base/basecomponent/basecomponent';
import { Button } from '../button/button';
import { LoginControllerService } from '../../services/api/loginController.service';

@Component({
  selector: 'app-header',
  imports: [Button],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header extends BaseComponent {
  private readonly loginService = inject(LoginControllerService);
  private readonly router = inject(Router);

  readonly labels = computed(() => ({
    title: this.getResource('APP_TITLE', 'Simtek'),
    logout: this.getResource('BUTTON_LOGOUT', 'Çıkış'),
  }));

  readonly username = computed(() => (this.flowService.token() ?? '').replace('TOKEN-', ''));

  logout() {
    this.loginService
      .logout({ token: this.flowService.token() })
      .toPromise()
      .then(() => {
        this.flowService.token.set(undefined);
        this.router.navigateByUrl('/firstlevel');
      })
      .catch((error) => console.error('Cikis:', error));
  }
}
