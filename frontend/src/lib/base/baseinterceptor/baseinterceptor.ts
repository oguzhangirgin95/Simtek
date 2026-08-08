import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, finalize, throwError } from 'rxjs';
import { environment } from '@env/environment';
import { API_ORIGIN } from '../baseconfig/apiorigin';
import { FlowService } from '../baseservice/flowservice';

/**
 * Bütün HTTP isteklerinin geçtiği ortak halka. Dört iş yapar:
 *
 * - Accept, dil, oturum ve transaction başlıklarını ekler
 * - Devam eden istek sayacını tutar (global yükleniyor göstergesi bunu okur)
 * - Hata mesajını State'teki 'serviceError' alanına yazar
 * - Sunucu tarafı render'ında göreli '/api/...' adresi çalışmadığı için
 *   adresi mutlak hale getirir
 */
export const BaseInterceptor: HttpInterceptorFn = (request, next) => {
  const flowService = inject(FlowService);
  const apiOrigin = inject(API_ORIGIN);

  const headers: Record<string, string> = {};

  // İstek kendi Accept başlığını verdiyse ona dokunulmaz.
  if (!request.headers.has('Accept')) {
    headers['Accept'] = 'application/json';
  }

  headers['Accept-Language'] = flowService.get<string>('language') ?? environment.defaultLanguage;

  const token = flowService.token();

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  // Sunucu tarafında hangi ekrandan gelindiğini bilmek günlükleme ve yetki
  // kontrolü için işe yarıyor.
  if (flowService.transaction()) {
    headers['X-Transaction'] = flowService.transaction();
  }
  if (flowService.currentStep()) {
    headers['X-Step'] = flowService.currentStep();
  }

  // Yeni istek önceki hatayı temizler; sayaç aşağıda finalize ile düşürülür.
  flowService.set('serviceError', undefined);
  flowService.pendingRequests.update((count) => count + 1);

  // slice(4) '/api' önekini atar: '/api/vehicle/list' -> '<origin>/vehicle/list'
  const url = apiOrigin && request.url.startsWith('/api/') ? apiOrigin + request.url.slice(4) : request.url;

  return next(request.clone({ url, setHeaders: headers })).pipe(
    catchError((error: HttpErrorResponse) => {
      flowService.set('serviceError', `${error.status} - ${error.statusText}`);
      // Hata yutulmaz; çağıran taraf kendi işlemesini yapabilsin diye yeniden fırlatılır.
      return throwError(() => error);
    }),
    // Başarılı da olsa hatalı da olsa sayaç mutlaka düşmeli, yoksa yükleniyor
    // göstergesi ekranda takılı kalır.
    finalize(() => flowService.pendingRequests.update((count) => count - 1)),
  );
};
