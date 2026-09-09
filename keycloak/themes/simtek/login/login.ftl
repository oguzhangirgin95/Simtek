<#--
  Giris formu. Sarmalayici (html/head/body, baslik, uyari kutusu)
  base/login/template.ftl'den geliyor.

  Ust ve alt seritler burada uretiliyor; CSS'te position:fixed olduklari icin
  kartin disinda, ekranin tepesinde ve dibinde duruyorlar.

  Metinler messages/messages_tr|en.properties dosyalarindan geliyor; Keycloak'in
  kendi mesajlari (hata metinleri gibi) base temanin dil paketinden.
-->
<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=!messagesPerField.existsError('username','password'); section>
    <#if section = "header">
        <img class="brand" src="${url.resourcesPath}/img/police-logo.png" alt="" />
        <span class="brand-text">${msg("loginAccountTitle")}</span>

    <#elseif section = "form">
        <div class="app-bar app-bar--top">
            <img class="app-bar__logo" src="${url.resourcesPath}/img/police-logo.png" alt="" />
            <span class="app-bar__title">${msg("simtekAppTitle")}</span>
            <img class="app-bar__logo" src="${url.resourcesPath}/img/police-logo.png" alt="" />
        </div>

        <#-- Dil secici. Realm'de birden fazla dil aciksa gorunur. -->
        <#if realm.internationalizationEnabled && locale.supported?size gt 1>
            <div class="lang">
                <#list locale.supported as l>
                    <a class="lang__item<#if l.languageTag == locale.currentLanguageTag> lang__item--active</#if>"
                       href="${l.url}" hreflang="${l.languageTag}" title="${l.label}">${l.languageTag?upper_case}</a>
                </#list>
            </div>
        </#if>

        <form id="kc-form-login" action="${url.loginAction}" method="post">
            <div class="field">
                <label for="username">${msg("username")}</label>
                <input id="username" name="username" type="text" value="${(login.username!'')}" dir="ltr"
                       autocomplete="username" autofocus
                       class="<#if messagesPerField.existsError('username','password')>field-error</#if>" />
            </div>

            <div class="field">
                <label for="password">${msg("password")}</label>
                <input id="password" name="password" type="password" autocomplete="current-password"
                       class="<#if messagesPerField.existsError('username','password')>field-error</#if>" />
            </div>

            <#if messagesPerField.existsError('username','password')>
                <p class="error">${kcSanitize(messagesPerField.getFirstError('username','password'))?no_esc}</p>
            </#if>

            <div class="buttons">
                <input type="hidden" name="credentialId" value="${(auth.selectedCredential)!''}" />
                <button id="kc-login" name="login" type="submit">${msg("doLogIn")}</button>
            </div>
        </form>

        <div class="app-bar app-bar--bottom">
            <img class="app-bar__logo" src="${url.resourcesPath}/img/police-logo.png" alt="" />
            <span class="app-bar__title">&copy; ${.now?string('yyyy')} Simtek</span>
            <img class="app-bar__logo" src="${url.resourcesPath}/img/police-logo.png" alt="" />
        </div>
    </#if>
</@layout.registrationLayout>
