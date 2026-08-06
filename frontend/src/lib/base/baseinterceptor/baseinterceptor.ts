import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, finalize, throwError } from 'rxjs';
import { FlowService } from '../baseservice/flowservice';

export const BaseInterceptor: HttpInterceptorFn = (request, next) => {
  const flowService = inject(FlowService);

  const headers: Record<string, string> = {};

  if (!request.headers.has('Accept')) {
    headers['Accept'] = 'application/json';
  }

  headers['Accept-Language'] = flowService.get<string>('language') ?? 'tr';

  const token = flowService.get<string>('token');
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  if (flowService.transaction()) {
    headers['X-Transaction'] = flowService.transaction();
  }
  if (flowService.currentStep()) {
    headers['X-Step'] = flowService.currentStep();
  }

  flowService.set('serviceError', undefined);
  flowService.pendingRequests.update((count) => count + 1);

  return next(request.clone({ setHeaders: headers })).pipe(
    catchError((error: HttpErrorResponse) => {
      flowService.set('serviceError', `${error.status} - ${error.statusText}`);
      return throwError(() => error);
    }),
    finalize(() => flowService.pendingRequests.update((count) => count - 1)),
  );
};
