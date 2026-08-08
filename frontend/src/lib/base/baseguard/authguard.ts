import { PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CanActivateFn, Router } from '@angular/router';
import { FlowService } from '../baseservice/flowservice';

/**
 * Oturum kontrolü. Token yoksa giriş ekranına yönlendirir.
 *
 * Sunucu tarafı render'ında token localStorage'dan okunamadığı için kontrol
 * atlanıyor; yoksa her SSR isteği girişe yönlenir ve tarayıcıya boş sayfa
 * giderdi. Asıl kontrol tarayıcıda yapılıyor.
 */
export const AuthGuard: CanActivateFn = () => {
  const flowService = inject(FlowService);
  
  const router = inject(Router);

  if (!isPlatformBrowser(inject(PLATFORM_ID))) {
    return true;
  }

  if (flowService.isLoggedIn()) {
    return true;
  }

  return router.parseUrl('/firstlevel');
};
