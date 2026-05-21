package store.auth;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import store.auth.client.AccountClient;
import store.auth.client.dto.AccountResponse;

@SpringBootTest
@AutoConfigureMockMvc
class AuthResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountClient accountClient;

    @Test
    void registerReturnsCreated() throws Exception {
        when(accountClient.create(new store.auth.client.dto.AccountCreateRequest("Ada", "ada@example.com", "securepass")))
            .thenReturn(new AccountResponse(UUID.randomUUID(), "Ada", "ada@example.com"));

        mockMvc.perform(post("/auth/register")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "name": "Ada",
                      "email": "ada@example.com",
                      "password": "securepass"
                    }
                    """))
            .andExpect(status().isCreated());
    }

    @Test
    void loginSetsJwtCookieAndSolveReturnsIdentity() throws Exception {
        UUID accountId = UUID.randomUUID();
        when(accountClient.findByEmailAndPassword(new store.auth.client.dto.AccountCredentialsRequest("ada@example.com", "securepass")))
            .thenReturn(new AccountResponse(accountId, "Ada", "ada@example.com"));

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "email": "ada@example.com",
                      "password": "securepass"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString("__store_jwt_token=")))
            .andReturn();

        String cookie = loginResult.getResponse().getHeader("Set-Cookie");
        String token = cookie.split(";", 2)[0].split("=", 2)[1];

        mockMvc.perform(post("/auth/solve")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "token": "%s"
                    }
                    """.formatted(token)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idAccount").value(accountId.toString()));
    }

    @Test
    void whoAmIReturnsCurrentAccount() throws Exception {
        UUID accountId = UUID.randomUUID();
        when(accountClient.findById(accountId)).thenReturn(new AccountResponse(accountId, "Ada", "ada@example.com"));

        mockMvc.perform(get("/auth/whoami")
                .header("id-account", accountId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(accountId.toString()))
            .andExpect(jsonPath("$.email").value("ada@example.com"));
    }

    @Test
    void logoutClearsCookie() throws Exception {
        mockMvc.perform(get("/auth/logout"))
            .andExpect(status().isOk())
            .andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString("__store_jwt_token=")));
    }

    @Test
    void healthcheckReturnsOk() throws Exception {
        mockMvc.perform(get("/auth/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.service").value("auth-service"))
            .andExpect(jsonPath("$.status").value("ok"));
    }
}
