import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { FlowService } from '../baseservice/flowservice';

export const AuthGuard: CanActivateFn = () => {
  const flowService = inject(FlowService);
  const router = inject(Router);

  if (flowService.isLoggedIn()) {
    return true;
  }

  return router.parseUrl('/firstlevel');
};
