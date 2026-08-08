import { InjectionToken } from '@angular/core';

/**
 * Tarayıcı istekleri '/api/...' adresine gider, Node sunucusu bunları backend'e
 * iletir. Sunucu tarafı render'ında göreli adresin karşılığı olmadığı için
 * backend'in mutlak adresi buradan veriliyor.
 */
export const API_ORIGIN = new InjectionToken<string>('API_ORIGIN', {
  providedIn: 'root',
  factory: () => '',
});
