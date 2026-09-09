export const environment = {
  name: 'development',
  production: false,
  apiUrl: '/api',
  defaultLanguage: 'tr',
  cryptoKey: 'SimtekLocalKey0123456789ABCDEF!!',
  keycloak: {
    url: 'http://localhost:8081',
    realm: 'simtek',
    clientId: 'simtek-frontend',
    // Asagidakileri uygulama kullanmaz; keycloak/setup.ps1 realm'i
    // kurarken okur.
    origins: ['http://localhost:4200', 'http://localhost:4000'],
    loginTheme: 'simtek',
    locales: ['tr', 'en'],
  },
};
