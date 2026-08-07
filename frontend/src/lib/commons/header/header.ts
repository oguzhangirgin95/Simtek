import { Component, afterNextRender, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { BaseComponent } from '../../base/basecomponent/basecomponent';
import { Button } from '../button/button';
import { Menu } from '../menu/menu';
import { Theme } from '../theme/theme';
import { LoginControllerService } from '../../services/api/loginController.service';

const HOME = '/monitoring/dashboard/start';

@Component({
  selector: 'app-header',
  imports: [Button, Menu, Theme],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header extends BaseComponent {
  private readonly loginService = inject(LoginControllerService);
  
  private readonly router = inject(Router);
  
  readonly ready = signal(false);

  readonly labels = computed(() => ({
    home: this.getResource('MENU_DASHBOARD', 'Pano'),
    logout: this.getResource('BUTTON_LOGOUT', 'Çıkış'),
  }));

  readonly atHome = computed(() => this.flowService.url() === HOME);

  readonly username = computed(() => (this.flowService.token() ?? '').replace('TOKEN-', ''));

  constructor() {
    super();
    afterNextRender(() => this.ready.set(true));
  }

  goHome() {
    this.router.navigateByUrl(HOME);
  }

  logout() {
    this.loginService
      .logout({ token: this.flowService.token() })
      .toPromise()
      .then(() => {
        this.flowService.token.set(undefined);
        this.router.navigateByUrl('/firstlevel');
      })
      .catch((error) => console.error('Logout:', error));
  }
}
