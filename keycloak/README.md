# Keycloak

Giris ekrani artik uygulamada degil, Keycloak'ta. `themes/simtek` temasi eski
login ekraninin gorunumunu tasir.

**Depoda realm dosyasi, kurulum betigi ve kullanici bilgisi yok.** Realm, client
ve kullanicilar Keycloak'in kendi veritabaninda yasar; asagidaki kurulum bir kez
admin konsolundan yapilir. Uygulamanin bildigi degerler
`frontend/src/environments/environment*.ts` icindeki `keycloak` blogunda:

```ts
keycloak: {
  url: 'http://localhost:8081',
  realm: 'simtek',
  clientId: 'simtek-frontend',
  origins: ['http://localhost:4200', 'http://localhost:4000'],
  loginTheme: 'simtek',
  locales: ['tr', 'en'],
},
```

Kurulumda girilen her deger bu bloktan okunur; ikisi ayrisirsa giris calismaz.

Backend 8080'de oldugu icin Keycloak 8081'de calisir.

## Sunucuyu baslatma

Keycloak dagitimi `.keycloak/keycloak-26.7.3` altinda duruyor (git'e girmez).
Tema klasoru sunucunun icine kopyalanir; ilk acilista yonetici hesabi olusur:

```bash
cp -r keycloak/themes/simtek .keycloak/keycloak-26.7.3/themes/
KC_BOOTSTRAP_ADMIN_USERNAME=admin KC_BOOTSTRAP_ADMIN_PASSWORD=... \
  .keycloak/keycloak-26.7.3/bin/kc.bat start-dev --http-port=8081
```

Dagitim yoksa keycloak.org/downloads adresinden 26.7.3 zip'ini indirip
`.keycloak/` altina acin. Yonetici hesabi bir kez olustuktan sonra
`KC_BOOTSTRAP_ADMIN_*` degiskenleri gereksiz.

Temayi degistirdikce kopyalama tekrarlanir; tema dosyalari sunucunun icinden
okunur.

Tema uzerinde calisirken sunucuyu her seferinde yeniden baslatmamak icin:

```
--spi-theme--cache-themes=false --spi-theme--cache-templates=false --spi-theme--static-max-age=-1
```

## Realm kurulumu (bir kez)

Admin konsolu: http://localhost:8081

1. **Realm olustur** - sol ustteki realm secici > Create realm.
   Realm name: `simtek` (ortam dosyasindaki `realm`).
2. **Realm settings > General**
   - Display name: `Simtek`
   - Require SSL: `external requests`
3. **Realm settings > Themes**
   - Login theme: `simtek` (ortam dosyasindaki `loginTheme`)
4. **Realm settings > Localization**
   - Internationalization: `On`
   - Supported locales: Turkce ve Ingilizce (ortam dosyasindaki `locales`)
   - Default locale: Turkce (listedeki ilk dil)
5. **Clients > Create client**
   - Client ID: `simtek-frontend` (ortam dosyasindaki `clientId`)
   - Client authentication: `Off` (public client)
   - Authentication flow: yalnizca `Standard flow` isaretli; Direct access
     grants ve Service accounts kapali
   - Valid redirect URIs: `origins` listesindeki her adres `/*` ekiyle:
     `http://localhost:4200/*` ve `http://localhost:4000/*`
   - Valid post logout redirect URIs: ayni iki adres
   - Web origins: `http://localhost:4200` ve `http://localhost:4000`
6. **Clients > simtek-frontend > Advanced > Advanced settings**
   - Proof Key for Code Exchange Code Challenge Method: `S256`

Adim 6 zorunlu: uygulama PKCE ile geliyor, kapaliysa giris reddedilir.

## Kullanici ekleme

Users > Add user. Kullanici adi ve **e-postayi** doldurun (e-posta bos kalirsa
Keycloak ilk giriste "profili tamamla" ekranini gosterir), kaydedip Credentials
sekmesinden sifre verin, Temporary = `Off`.

## Baska bir sunucuya tasirken

Ilgili `environment*.ts` icindeki `keycloak` blogunu guncelleyin ve ayni
degerleri yeni Keycloak'ta realm/client ayarlarina girin.

## Tema

| Dosya | Isi |
| --- | --- |
| `theme.properties` | `parent=base`; base sablonlarinin yazdigi sinif adlarini uygulamadakilerle ayni yapar |
| `login.ftl` | Yalnizca baslik, form ve seritler; html/head/body base'den gelir |
| `resources/css/login.css` | Gorunum. Degerler `frontend/src/styles.scss` ile ayni |
| `resources/img/police-logo.png` | Seritlerdeki ve kart ustundeki rozet |
| `messages/messages_tr.properties` | Turkce metinler; yazilmayanlar Keycloak'in kendi paketinden gelir |
| `messages/messages_en.properties` | Ayni anahtarlarin Ingilizcesi |

Yeni dil eklemek icin: `theme.properties` icindeki `locales` satirina dili
ekleyin, `messages/messages_<kod>.properties` dosyasini olusturun, ortam
dosyasindaki `locales` dizisine ve realm'in Supported locales listesine ekleyin.
Karttaki TR/EN dugmeleri `login.ftl` icinde `locale.supported` uzerinden
uretildigi icin yeni dil kendiliginden gorunur.
