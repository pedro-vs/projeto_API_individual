package store.account;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import store.account.repository.AccountRepository;

@SpringBootTest
@AutoConfigureMockMvc
class AccountResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    void createAccountReturnsCreatedAccount() throws Exception {
        mockMvc.perform(post("/accounts")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "name": "Ada",
                      "email": "ada@example.com",
                      "password": "securepass"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value("Ada"))
            .andExpect(jsonPath("$.email").value("ada@example.com"));
    }

    @Test
    void createAccountReturnsConflictForDuplicateEmail() throws Exception {
        createAccountAndReturnId();

        mockMvc.perform(post("/accounts")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "name": "Ada 2",
                      "email": "ADA@example.com",
                      "password": "securepass"
                    }
                    """))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.title").value("Account email already exists"));
    }

    @Test
    void findByIdReturnsExistingAccount() throws Exception {
        UUID accountId = createAccountAndReturnId();

        mockMvc.perform(get("/accounts/{id}", accountId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(accountId.toString()))
            .andExpect(jsonPath("$.email").value("ada@example.com"));
    }

    @Test
    void searchReturnsAccountWhenCredentialsMatch() throws Exception {
        UUID accountId = createAccountAndReturnId();

        mockMvc.perform(post("/accounts/search")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "email": "ada@example.com",
                      "password": "securepass"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(accountId.toString()))
            .andExpect(jsonPath("$.name").value("Ada"));
    }

    @Test
    void searchReturnsUnauthorizedWhenPasswordIsWrong() throws Exception {
        createAccountAndReturnId();

        mockMvc.perform(post("/accounts/search")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "email": "ada@example.com",
                      "password": "wrongpass"
                    }
                    """))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.title").value("Invalid credentials"));
    }

    @Test
    void healthcheckReturnsOk() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.service").value("account-service"))
            .andExpect(jsonPath("$.status").value("ok"));
    }

    private UUID createAccountAndReturnId() throws Exception {
        MvcResult result = mockMvc.perform(post("/accounts")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "name": "Ada",
                      "email": "ada@example.com",
                      "password": "securepass"
                    }
                    """))
            .andExpect(status().isCreated())
            .andReturn();

        JsonNode responseBody = objectMapper.readTree(result.getResponse().getContentAsString());
        return UUID.fromString(responseBody.get("id").asText());
    }
}
