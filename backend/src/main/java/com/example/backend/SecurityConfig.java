package com.example.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Keycloak token dogrulamasi.
 *
 * Istekteki 'Authorization: Bearer <token>' basligi, application.properties'teki
 * issuer-uri adresinden alinan anahtarlarla dogrulanir. Token gecersizse istek
 * controller'a hic ulasmaz.
 */
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Token tasiyan API'de cerez yok, CSRF korumasi da gereksiz.
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Tarayicinin CORS on kontrolu (preflight) token tasimaz.
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Giris ucu: kullanici henuz token almadigi icin acik.
                        .requestMatchers("/login/eligable").permitAll()
                        // Swagger ve saglik kontrolu acik kalir.
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}
