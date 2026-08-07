import { mergeApplicationConfig, ApplicationConfig } from '@angular/core';
import { provideServerRendering, withRoutes } from '@angular/ssr';
import { API_ORIGIN } from '../lib/base/baseconfig/apiorigin';
import { appConfig } from './app.config';
import { serverRoutes } from './app.routes.server';

const apiAdresi = process.env['API_URL'] ?? 'http://localhost:8080';

const apiUrl = apiAdresi.startsWith('http') ? apiAdresi : `https://${apiAdresi}`;

const serverConfig: ApplicationConfig = {
  providers: [
    provideServerRendering(withRoutes(serverRoutes)),
    { provide: API_ORIGIN, useValue: apiUrl }
  ]
};

export const config = mergeApplicationConfig(appConfig, serverConfig);
