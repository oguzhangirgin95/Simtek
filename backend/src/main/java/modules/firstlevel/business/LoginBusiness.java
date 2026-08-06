package modules.firstlevel.business;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import models.firstlevel.entity.AppUser;
import models.firstlevel.request.CurrentUserRequest;
import models.firstlevel.request.LoginRequest;
import models.firstlevel.request.LogoutRequest;
import models.firstlevel.response.CurrentUserResponse;
import models.firstlevel.response.LoginResponse;
import models.firstlevel.response.LogoutResponse;
import modules.firstlevel.repositories.AppUserRepository;

@Service
public class LoginBusiness {

    /** gecerli oturumlar: token -> kullanici adi */
    private static final Map<String, String> TOKENS = new ConcurrentHashMap<>();

    private final AppUserRepository appUserRepository;

    public LoginBusiness(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    /** Kullanici adi ve sifre veritabanindan dogrulanir */
    @Transactional(readOnly = true)
    public LoginResponse Login(LoginRequest loginRequest) {

        LoginResponse loginResponse = new LoginResponse(false);

        if (loginRequest == null || loginRequest.username == null || loginRequest.password == null) {
            return loginResponse;
        }

        Optional<AppUser> user = appUserRepository.findByUsernameAndPassword(loginRequest.username,
                loginRequest.password);

        if (user.isEmpty()) {
            return loginResponse;
        }

        loginResponse.success = true;
        loginResponse.token = "TOKEN-" + user.get().username.toUpperCase();

        TOKENS.put(loginResponse.token, user.get().username);

        return loginResponse;
    }

    /** Cikis: token gecersiz kilinir */
    public LogoutResponse Logout(LogoutRequest logoutRequest) {

        if (logoutRequest == null || logoutRequest.token == null) {
            return new LogoutResponse(false, "Token bulunamadi.");
        }

        String username = TOKENS.remove(logoutRequest.token);

        return username == null
                ? new LogoutResponse(false, "Oturum zaten kapali.")
                : new LogoutResponse(true, "Cikis yapildi.");
    }

    /** Token gecerli mi; sayfa yenilendiginde oturumu dogrulamak icin */
    public CurrentUserResponse CurrentUser(CurrentUserRequest currentUserRequest) {

        CurrentUserResponse currentUserResponse = new CurrentUserResponse();
        currentUserResponse.valid = false;

        if (currentUserRequest == null || currentUserRequest.token == null) {
            return currentUserResponse;
        }

        String username = TOKENS.get(currentUserRequest.token);
        if (username == null) {
            return currentUserResponse;
        }

        currentUserResponse.valid = true;
        currentUserResponse.username = username;
        currentUserResponse.token = currentUserRequest.token;

        return currentUserResponse;
    }
}
