package store.auth.resource;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.auth.dto.IdentityOut;
import store.auth.dto.LoginIn;
import store.auth.dto.RegisterIn;
import store.auth.dto.TokenIn;
import store.auth.dto.TokenOut;
import store.auth.dto.WhoAmIOut;
import store.auth.exception.MissingAccountHeaderException;
import store.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthResource {

    private static final String AUTH_COOKIE = "__store_jwt_token";

    private final AuthService authService;

    @Value("${store.jwt.http-only}")
    private boolean httpOnly;

    @Value("${store.jwt.secure}")
    private boolean secure;

    @Value("${store.jwt.same-site}")
    private String sameSite;

    @Value("${store.jwt.duration}")
    private long duration;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterIn registerIn) {
        authService.register(registerIn);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginIn loginIn) {
        TokenOut tokenOut = authService.login(loginIn.email(), loginIn.password());
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, buildTokenCookie(tokenOut.token(), duration).toString())
            .build();
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, buildTokenCookie("", 0).toString())
            .build();
    }

    @PostMapping("/solve")
    public IdentityOut solve(@Valid @RequestBody TokenIn tokenIn) {
        return new IdentityOut(authService.solveToken(tokenIn.token()));
    }

    @GetMapping("/whoami")
    public WhoAmIOut whoAmI(@RequestHeader(value = "id-account", required = false) String idAccount) {
        if (idAccount == null || idAccount.isBlank()) {
            throw new MissingAccountHeaderException();
        }
        return authService.whoAmI(idAccount);
    }

    @GetMapping("/")
    public Map<String, String> healthcheck() {
        return Map.of(
            "service", "auth-service",
            "status", "ok"
        );
    }

    private ResponseCookie buildTokenCookie(String token, long maxAgeMillis) {
        return ResponseCookie.from(AUTH_COOKIE, token)
            .httpOnly(httpOnly)
            .secure(secure)
            .sameSite(sameSite)
            .path("/")
            .maxAge(Duration.ofMillis(maxAgeMillis))
            .build();
    }
}
