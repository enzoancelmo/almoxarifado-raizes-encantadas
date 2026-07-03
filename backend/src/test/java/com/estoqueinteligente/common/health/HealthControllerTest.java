package com.estoqueinteligente.common.health;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
class HealthControllerTest{@Test void shouldReturnApplicationHealth()throws Exception{MockMvc mockMvc=MockMvcBuilders.standaloneSetup(new HealthController()).build();mockMvc.perform(get("/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP")).andExpect(jsonPath("$.application").value("Estoque Inteligente"));}}
