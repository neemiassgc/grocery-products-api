package com.api.controller;

import com.api.entity.Price;
import com.api.repository.PriceRepository;
import com.api.repository.ProductRepository;
import com.api.service.DomainMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebMvcTest(value = {PriceController.class, GlobalErrorHandlingController.class})
public class PriceControllerTest {

    @MockBean
    private PriceRepository priceRepository;

    @MockBean
    private DomainMapper domainMapper;

    private MockMvc mockMvc;

    private List<Price> usefulPrices = List.of(
        new Price(
            UUID.fromString("5b3e4ff1-99de-4d82-927e-3b26d868925c"),
            new BigDecimal("34.5"), Instant.now(), null
        ),
        new Price(
            UUID.fromString("e1960bf6-381b-458f-bb1f-2a41236337cd"),
            new BigDecimal("4.52"), Instant.now(), null
        ),
        new Price(
            UUID.fromString("b92b558b-b851-46cc-a2a3-b566e7e44966"),
            new BigDecimal("16.75"), Instant.now(), null
        ),
        new Price(
            UUID.fromString("6bba668b-eea7-46c7-99de-d80c965c847b"),
            new BigDecimal("12.12"), Instant.now(), null
        ),
        new Price(
            UUID.fromString("45578afd-75ad-4ba0-9aa6-f0391bca9d2a"),
            new BigDecimal("6.39"), Instant.now(), null
        )
    );

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(new PriceController(priceRepository), new GlobalErrorHandlingController())
        .alwaysDo(print()).build();
    }

    @Test
    @DisplayName("GET /api/prices/b92b558b-b851-46cc-a2a3-b566e7e44966 - 200 OK")
    void should_return_one_only_price_by_id() throws Exception {
        final UUID uuid = UUID.fromString("b92b558b-b851-46cc-a2a3-b566e7e44966");

        given(priceRepository.findById(eq(uuid))).willReturn(Optional.of(usefulPrices.get(2)));

        mockMvc.perform(get("/api/prices/"+uuid)
            .characterEncoding(StandardCharsets.UTF_8)
            .accept(MediaType.ALL)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.value").value("16.75"));

        verify(priceRepository, times(1)).findById(eq(uuid));
        verify(priceRepository, only()).findById(eq(uuid));
    }
}
