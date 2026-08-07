import { InjectionToken } from '@angular/core';

/**
 * Tarayici istekleri '/api/...' adresine gider, Node sunucusu backend'e iletir.
 * Sunucu tarafi render'inda gorece adres calismadigi icin backend adresi buradan verilir.
 */
export const API_ORIGIN = new InjectionToken<string>('API_ORIGIN', {
  providedIn: 'root',
  factory: () => '',
});
