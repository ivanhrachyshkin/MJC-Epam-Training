//package com.epam.esm.view;
//
//import com.epam.esm.service.dto.GiftCertificateDto;
//import com.epam.esm.service.dto.OrderDto;
//import com.epam.esm.service.dto.UserDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@AutoConfigureMockMvc
//public class OrderControllerTest extends ResponseProvider {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private ObjectMapper objectMapper;
//
//    private ResourceBundle rb;
//
//    @BeforeEach
//    public void setUp() throws IOException {
//        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
//                .getResourceAsStream("message.properties");
//        assertNotNull(contentStream);
//        rb = new PropertyResourceBundle(contentStream);
//        objectMapper = new ObjectMapper();
//    }
//
//    @Test
//    public void shouldThrowException_On_ReadByIdEndPoint_Anonymous() throws Exception {
//        //Given
//        final String expectedExceptionMessage = UNAUTHORIZED_MESSAGE;
//        //When
//        final String response = getUnauthorizedForGetMethod("/orders/1", mockMvc);
//        //Then
//        assertThat(response).contains(expectedExceptionMessage);
//    }
//
//    @Test
//    @WithMockUser(authorities = "ROLE_ADMIN")
//    public void shouldReturnOrder_On_ReadByIdEndPoint_Admin() throws Exception {
//        //Given
//        final OrderDto orderDto = new OrderDto(new UserDto(1), Collections.singleton(new GiftCertificateDto(1)));
//        final int expectedOrderId = 1;
//        final int expectedUserId = 1;
//        final float expectedPrice = 3.0F;
//        final int expectedGiftCertificatesSize = 2;
//        //When
//        final String response = getOkForGetMethod("/orders/1", mockMvc);
//        final OrderDto outDtoOrder = objectMapper.readValue(response, OrderDto.class);
//        //Then
//        assertEquals(expectedOrderId, outDtoOrder.getId());
//        assertEquals(expectedUserId, outDtoOrder.getUserDto().getId());
//        assertEquals(expectedGiftCertificatesSize, outDtoOrder.getPrice());
//        assertEquals(expectedPrice, outDtoOrder.getPrice());
//        assertThat(outDtoOrder.getDate()).isNotNull();
//        assertEquals(expectedGiftCertificatesSize, outDtoOrder.getDtoGiftCertificates().size());
//    }
//}
