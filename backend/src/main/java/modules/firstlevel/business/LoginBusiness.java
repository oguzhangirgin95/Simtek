package modules.firstlevel.business;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.example.backend.CryptologyService;

import models.firstlevel.request.LoginRequest;
import models.firstlevel.response.CurrentUserResponse;
import models.firstlevel.response.LoginResponse;
import modules.features.business.FeatureBusiness;

@Service
public class LoginBusiness {

    private final CryptologyService cryptologyService;

    private final FeatureBusiness featureBusiness;

    private final RestClient restClient = RestClient.create();

    /** Keycloak'in token ucu; issuer adresinden turetiliyor. */
    private final String tokenUri;

    private final String clientId;

    public LoginBusiness(CryptologyService cryptologyService, FeatureBusiness featureBusiness,
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri,
            @Value("${app.keycloak.client-id}") String clientId) {
        this.cryptologyService = cryptologyService;
        this.featureBusiness = featureBusiness;
        this.tokenUri = issuerUri + "/protocol/openid-connect/token";
        this.clientId = clientId;
    }

    /**
     * Giris.
     *
     * Sifre onyuzden sifreli geliyor, burada cozulup Keycloak'a soruluyor.
     * Boylece sifre tarayicidan Keycloak'a gitmiyor ve uygulama kendi token'ini
     * uretmiyor; donen token Keycloak'in imzaladigi token.
     */
    public LoginResponse Login(LoginRequest loginRequest) {

        LoginResponse loginResponse = new LoginResponse(false);

        if (loginRequest == null || loginRequest.username == null || loginRequest.password == null) {
            return loginResponse;
        }

        String password = cryptologyService.decryption(loginRequest.password);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", clientId);
        form.add("grant_type", "password");
        form.add("username", loginRequest.username);
        form.add("password", password);

        try {
            Map<?, ?> answer = restClient.post()
                    .uri(tokenUri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(Map.class);

            Object token = answer == null ? null : answer.get("access_token");
            if (token == null) {
                return loginResponse;
            }

            loginResponse.success = true;
            loginResponse.token = token.toString();
            loginResponse.features = featureBusiness.EnabledFeatures();

        } catch (Exception exception) {
            // Keycloak hatali kullanici adi/sifrede hata donduruyor; ekranda
            // "kullanici adi veya sifre hatali" gorunmesi icin success false kaliyor.
            return loginResponse;
        }

        return loginResponse;
    }

    /**
     * Oturum bilgisi.
     *
     * Token'i Keycloak uretiyor; gecersiz token guvenlik filtresinde durdugu
     * icin buraya gelen jwt her zaman dogrulanmis oluyor.
     */
    public CurrentUserResponse CurrentUser(Jwt jwt) {

        CurrentUserResponse currentUserResponse = new CurrentUserResponse();
        currentUserResponse.valid = true;
        currentUserResponse.username = jwt.getClaimAsString("preferred_username");
        currentUserResponse.token = jwt.getTokenValue();
        currentUserResponse.features = featureBusiness.EnabledFeatures();

        return currentUserResponse;
    }
}
