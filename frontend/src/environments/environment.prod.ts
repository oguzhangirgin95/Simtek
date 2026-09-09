export const environment = {
  name: 'prod',
  production: true,
  apiUrl: '/api',
  defaultLanguage: 'tr',
  cryptoKey: 'SimtekProdKey0123456789ABCDEF!!!',
  // Unity WebGL ciktisinin adresi; icinde Unity'nin kendi index.html'i beklenir.
  unity: { url: '/unity/' },
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
