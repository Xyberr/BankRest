package com.example.bankcards.controller;

import com.example.bankcards.service.transferService.ITransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITransferService transferService;

    @Test
    @WithMockUser(username = "1", roles = {"USER"})
    void shouldReturn204() throws Exception {

        mockMvc.perform(post("/api/user/transfers")
                        .contentType(APPLICATION_JSON)
                        .content("""
                            {
                              "fromCardId": 1,
                              "toCardId": 2,
                              "amount": 10.00
                            }
                            """))
                .andExpect(status().isNoContent());

        verify(transferService)
                .transferBetweenOwnCards(any(), eq(1L));
    }
}