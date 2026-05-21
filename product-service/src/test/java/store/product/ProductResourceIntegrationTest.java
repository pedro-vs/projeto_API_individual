package store.product;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import store.product.model.Product;
import store.product.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
class ProductResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoSpyBean
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        reset(productRepository);
    }

    @Test
    void createProductReturnsCreatedProduct() throws Exception {
        mockMvc.perform(post("/products")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "name": "Tomato",
                      "price": 10.12,
                      "unit": "kg"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value("Tomato"))
            .andExpect(jsonPath("$.price").value(10.12))
            .andExpect(jsonPath("$.unit").value("kg"));
    }

    @Test
    void healthcheckReturnsOk() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.service").value("product-service"))
            .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void findAllReturnsSavedProducts() throws Exception {
        productRepository.save(Product.builder()
            .name("Cheese")
            .price(new BigDecimal("0.62"))
            .unit("slice")
            .build());

        productRepository.save(Product.builder()
            .name("Tomato")
            .price(new BigDecimal("10.12"))
            .unit("kg")
            .build());

        mockMvc.perform(get("/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("Cheese"))
            .andExpect(jsonPath("$[1].name").value("Tomato"));
    }

    @Test
    void findByIdReturnsExistingProduct() throws Exception {
        UUID productId = createProductAndReturnId();

        mockMvc.perform(get("/products/{id}", productId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(productId.toString()))
            .andExpect(jsonPath("$.name").value("Tomato"));
    }

    @Test
    void findByIdUsesCacheOnRepeatedRequests() throws Exception {
        UUID productId = createProductAndReturnId();
        reset(productRepository);

        mockMvc.perform(get("/products/{id}", productId))
            .andExpect(status().isOk());

        mockMvc.perform(get("/products/{id}", productId))
            .andExpect(status().isOk());

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void deleteRemovesExistingProduct() throws Exception {
        UUID productId = createProductAndReturnId();

        mockMvc.perform(delete("/products/{id}", productId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/products/{id}", productId))
            .andExpect(status().isNotFound());
    }

    @Test
    void createProductWithInvalidBodyReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/products")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "name": "",
                      "price": 0,
                      "unit": ""
                    }
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value("Invalid request body"))
            .andExpect(jsonPath("$.errors.name").value("name is required"))
            .andExpect(jsonPath("$.errors.price").value("price must be greater than zero"))
            .andExpect(jsonPath("$.errors.unit").value("unit is required"));
    }

    @Test
    void findByIdReturnsNotFoundForUnknownId() throws Exception {
        mockMvc.perform(get("/products/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.title").value("Product not found"));
    }

    private UUID createProductAndReturnId() throws Exception {
        MvcResult result = mockMvc.perform(post("/products")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "name": "Tomato",
                      "price": 10.12,
                      "unit": "kg"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(header().string("Content-Type", org.hamcrest.Matchers.containsString(APPLICATION_JSON.toString())))
            .andReturn();

        JsonNode responseBody = objectMapper.readTree(result.getResponse().getContentAsString());
        return UUID.fromString(responseBody.get("id").asText());
    }
}
