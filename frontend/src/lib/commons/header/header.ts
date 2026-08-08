import { Component, afterNextRender, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
import { Button } from '../button/button';
import { Menu } from '../menu/menu';
import { Theme } from '../theme/theme';
import { LoginControllerService } from '@lib/services/api/loginController.service';

/** "Pano" butonunun gittiği ekran. */
const HOME = '/monitoring/dashboard/start';

/** Üst şerit: menü, pano kısayolu, tema seçimi, kullanıcı adı ve çıkış. */
@Component({
  selector: 'app-header',
  imports: [Button, Menu, Theme],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header extends BaseComponent {
  private readonly loginService = inject(LoginControllerService);

  private readonly router = inject(Router);

  /**
   * İlk çizimden sonra true olur.
   *
   * Kullanıcıya özel kısımlar (token'dan gelen ad, çıkış butonu) sunucu
   * tarafında bilinmiyor. Doğrudan gösterilirse sunucunun ürettiği HTML ile
   * tarayıcının çizdiği farklı olur ve hidrasyon uyuşmazlığı çıkar; o yüzden
   * bu kısımlar tarayıcı devralana kadar gizli tutuluyor.
   */
  readonly ready = signal(false);

  /** Ekran metinleri. */
  readonly labels = computed(() => ({
    home: this.getResource('MENU_DASHBOARD', 'Pano'),
    logout: this.getResource('BUTTON_LOGOUT', 'Çıkış'),
  }));

  /** Zaten panodaysak "Pano" butonunu göstermeye gerek yok. */
  readonly atHome = computed(() => this.flowService.url() === HOME);

  /** Token 'TOKEN-<kullanıcı>' biçiminde geldiği için önek atılarak ad elde edilir. */
  readonly username = computed(() => (this.flowService.token() ?? '').replace('TOKEN-', ''));

  /** İlk çizim tamamlanınca kullanıcıya özel kısımların önü açılır. */
  constructor() {
    super();
    afterNextRender(() => this.ready.set(true));
  }

  /** Panoya döner. */
  goHome() {
    this.router.navigateByUrl(HOME);
  }

  /**
   * Çıkış. Önce sunucudaki oturum kapatılır, sonra token silinip giriş
   * ekranına dönülür.
   */
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
