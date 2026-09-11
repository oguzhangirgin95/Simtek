<#--
  Giris ekrani. Sarmalayici (html/head/body, uyari kutusu) base/login/template.ftl'den
  geliyor; bu dosya yalnizca "header" ve "form" bolumlerini dolduruyor.

  Yerlesim:
    ust serit        : ikon | baslik | ikon   (ortada)
    ust koseler      : resim + iki satir yazi
    orta             : buyuk resim, altinda giris kutusu
    kutunun alti     : parolami unuttum | destek
    alt koseler      : resim
    alt serit        : ikon | yazi | ikon     (ortada)

  Seritler ve koseler CSS'te position:fixed; kartin disinda, ekranin kenarlarinda
  dururlar. Resimlerin hepsi simdilik ayni rozet; farkli bir resim icin
  resources/img altina dosyayi koyup ilgili satirdaki adi degistirmek yeterli.

  Metinler messages/messages_tr|en.properties dosyalarinda.
-->
<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=!messagesPerField.existsError('username','password'); section>
    <#assign resim = url.resourcesPath + "/img/police-logo.png">

    <#if section = "header">
        <#-- Sayfanin basligi bu resim; ekran okuyucu icin alt metni var. -->
        <img class="hero" src="${resim}" alt="${msg("loginAccountTitle")}" />

    <#elseif section = "form">
        <div class="app-bar app-bar--top">
            <span class="app-bar__group">
                <img class="app-bar__icon" src="${resim}" alt="" />
                <span class="app-bar__title">${msg("simtekAppTitle")}</span>
                <img class="app-bar__icon" src="${resim}" alt="" />
            </span>

            <#-- Dil secici. Realm'de birden fazla dil aciksa gorunur. -->
            <#if realm.internationalizationEnabled && locale.supported?size gt 1>
                <span class="lang">
                    <#list locale.supported as l>
                        <a class="lang__item<#if l.languageTag == locale.currentLanguageTag> lang__item--active</#if>"
                           href="${l.url}" hreflang="${l.languageTag}" title="${l.label}">${l.languageTag?upper_case}</a>
                    </#list>
                </span>
            </#if>
        </div>

        <div class="corner corner--top-left">
            <img class="corner__image" src="${resim}" alt="" />
            <p class="corner__text"><span>${msg("simtekLeftLine1")}</span><span>${msg("simtekLeftLine2")}</span></p>
        </div>

        <div class="corner corner--top-right">
            <p class="corner__text"><span>${msg("simtekRightLine1")}</span><span>${msg("simtekRightLine2")}</span></p>
            <img class="corner__image" src="${resim}" alt="" />
        </div>

        <div class="corner corner--bottom-left"><img class="corner__image" src="${resim}" alt="" /></div>
        <div class="corner corner--bottom-right"><img class="corner__image" src="${resim}" alt="" /></div>

        <div class="form-box">
            <form id="kc-form-login" action="${url.loginAction}" method="post">
                <div class="field">
                    <label for="username">${msg("username")}</label>
                    <span class="field__control">
                        <svg class="field__icon" viewBox="0 0 24 24" aria-hidden="true">
                            <circle cx="12" cy="8" r="4" />
                            <path d="M4 21c0-4 3.6-7 8-7s8 3 8 7" />
                        </svg>
                        <input id="username" name="username" type="text" value="${(login.username!'')}" dir="ltr"
                               autocomplete="username" autofocus
                               class="<#if messagesPerField.existsError('username','password')>field-error</#if>" />
                    </span>
                </div>

                <div class="field">
                    <label for="password">${msg("password")}</label>
                    <span class="field__control">
                        <svg class="field__icon" viewBox="0 0 24 24" aria-hidden="true">
                            <rect x="5" y="11" width="14" height="10" rx="2" />
                            <path d="M8 11V7a4 4 0 0 1 8 0v4" />
                        </svg>
                        <input id="password" name="password" type="password" autocomplete="current-password"
                               class="<#if messagesPerField.existsError('username','password')>field-error</#if>" />
                    </span>
                </div>

                <#if messagesPerField.existsError('username','password')>
                    <p class="error">${kcSanitize(messagesPerField.getFirstError('username','password'))?no_esc}</p>
                </#if>

                <div class="buttons">
                    <input type="hidden" name="credentialId" value="${(auth.selectedCredential)!''}" />
                    <button id="kc-login" name="login" type="submit">${msg("doLogIn")}</button>
                </div>
            </form>
        </div>

        <#-- Parolami unuttum, realm'de "Forgot password" acik olmalidir. -->
        <div class="form-links">
            <a href="${url.loginResetCredentialsUrl}">${msg("doForgotPassword")}</a>
            <a href="${properties.simtekSupportUrl!'#'}">${msg("simtekSupport")}</a>
        </div>

        <p class="notice">
            <svg class="notice__icon" viewBox="0 0 24 24" aria-hidden="true">
                <path d="M12 3 2 20h20L12 3z" />
                <path d="M12 10v4" />
                <path d="M12 17h.01" />
            </svg>
            <span>${msg("simtekNotice")}</span>
        </p>

        <div class="app-bar app-bar--bottom">
            <span class="app-bar__group">
                <img class="app-bar__icon" src="${resim}" alt="" />
                <span class="app-bar__title">&copy; ${.now?string('yyyy')} Simtek</span>
                <img class="app-bar__icon" src="${resim}" alt="" />
            </span>
        </div>
    </#if>
</@layout.registrationLayout>
