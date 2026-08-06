import { ApplicationConfig, importProvidersFrom, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { AppModule } from './app-module';
import { BaseInterceptor } from '../lib/base/baseinterceptor/baseinterceptor';
import { provideApi } from '../lib/services/provide-api';
import { environment } from '../environments/environment';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes), provideClientHydration(withEventReplay()),
    provideHttpClient(withFetch(), withInterceptors([BaseInterceptor])),
    provideApi(environment.apiUrl),
    importProvidersFrom(AppModule)
  ]
};
