package com.epam.esm.view;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest extends ResponseProvider {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private ResourceBundle rb;

    final GiftCertificateDto inDtoGiftCertificate = new GiftCertificateDto();
    final UserDto inDtoUser = new UserDto();
    final OrderDto inDtoOrder = new OrderDto();


    @BeforeEach
    public void setUp() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        rb = new PropertyResourceBundle(contentStream);
        objectMapper = new ObjectMapper();
    }

    //Read one method tests
    @Test
    public void shouldThrowException_On_ReadByIdEndPoint_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = UNAUTHORIZED_MESSAGE;
        //When
        final String response = getUnauthorizedForGetMethod("/orders/1", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER", username = "username6")
    public void shouldReturnOrder_On_ReadByIdEndPoint_User() throws Exception {
        //Given
        inDtoGiftCertificate.setId(8);
        inDtoGiftCertificate.setName("gift8");
        inDtoGiftCertificate.setDescription("d8");
        inDtoGiftCertificate.setPrice(8.0F);
        inDtoGiftCertificate.setDuration(8);
        inDtoGiftCertificate.setActive(true);

        inDtoUser.setId(6);

        inDtoOrder.setId(1);
        inDtoOrder.setPrice(1.0F);
        inDtoOrder.setUserDto(inDtoUser);
        inDtoOrder.setDtoGiftCertificates(Collections.singleton(inDtoGiftCertificate));
        //When
        final String response = getOkForGetMethod("/orders/1", mockMvc);
        final OrderDto outDtoOrder = objectMapper.readValue(response, OrderDto.class);
        //Then
        assertEquals(inDtoOrder, outDtoOrder);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER", username = "username1")
    public void shouldThrowException_On_ReadByIdEndPoint_User() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("order.notFound.user.order"), 1, 1);
        //When
        final String response = getNotFoundForGetMethod("/orders/1", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN", username = "username1")
    public void shouldReturnOrder_On_ReadByIdEndPoint_Admin() throws Exception {
        //Given
        inDtoGiftCertificate.setId(8);
        inDtoGiftCertificate.setName("gift8");
        inDtoGiftCertificate.setDescription("d8");
        inDtoGiftCertificate.setPrice(8.0F);
        inDtoGiftCertificate.setDuration(8);
        inDtoGiftCertificate.setActive(true);

        inDtoUser.setId(6);

        inDtoOrder.setId(1);
        inDtoOrder.setPrice(1.0F);
        inDtoOrder.setUserDto(inDtoUser);
        inDtoOrder.setDtoGiftCertificates(Collections.singleton(inDtoGiftCertificate));
        //When
        final String response = getOkForGetMethod("/orders/1", mockMvc);
        final OrderDto outDtoOrder = objectMapper.readValue(response, OrderDto.class);
        //Then
        assertEquals(inDtoOrder, outDtoOrder);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN", username = "username1")
    public void shouldThrowException_On_ReadByIdEndPoint_Admin() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("order.notFound.id"), 100);
        //When
        final String response = getNotFoundForGetMethod("/orders/100", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    //Create method tests
    @Test
    public void shouldThrowException_On_CreateEndPoint_For_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = UNAUTHORIZED_MESSAGE;
        //When
        final String response = getUnauthorizedForPostMethod("/orders", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER", username = "username1")
    public void shouldThrowException_On_CreateEndPoint_With_NotPersistedGiftCertificate_For_User() throws Exception {
        //Given
        inDtoGiftCertificate.setId(100);
        inDtoOrder.setDtoGiftCertificates(Collections.singleton(inDtoGiftCertificate));

        final String inDtoOrderAsString = objectMapper.writeValueAsString(inDtoOrder);
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("giftCertificate.notFound.id"), 100);
        //When
        final String response = getNotFoundForPostMethod("/orders", inDtoOrderAsString, mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN", username = "username1")
    public void shouldThrowException_On_CreateEndPoint_With_NotPersistedUser_For_Admin() throws Exception {
        //Given
        inDtoUser.setId(100);
        inDtoGiftCertificate.setId(1);
        inDtoOrder.setUserDto(inDtoUser);
        inDtoOrder.setDtoGiftCertificates(Collections.singleton(inDtoGiftCertificate));

        final String inDtoOrderAsString = objectMapper.writeValueAsString(inDtoOrder);
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("user.notFound.id"), 100);
        //When
        final String response = getNotFoundForPostMethod("/orders", inDtoOrderAsString, mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN", username = "username1")
    public void shouldThrowException_On_CreateEndPoint_With_NotGiftCertificate_For_Admin() throws Exception {
        //Given
        inDtoUser.setId(1);
        inDtoGiftCertificate.setId(100);
        inDtoOrder.setUserDto(inDtoUser);
        inDtoOrder.setDtoGiftCertificates(Collections.singleton(inDtoGiftCertificate));

        final String inDtoOrderAsString = objectMapper.writeValueAsString(inDtoOrder);
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("giftCertificate.notFound.id"), 100);
        //When
        final String response = getNotFoundForPostMethod("/orders", inDtoOrderAsString, mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }
}
