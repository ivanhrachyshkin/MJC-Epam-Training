package com.epam.esm.service;

import com.epam.esm.dao.OrderRepository;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.AuthorityValidator;
import com.epam.esm.service.validator.OrderValidator;
import com.epam.esm.service.validator.PageableValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest extends AssertionsProvider<OrderDto> {

    @Mock
    private DtoMapper<Order, OrderDto> mapper;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private GiftCertificateServiceImpl giftCertificateService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderValidator orderValidator;
    @Mock
    private PageableValidator pageableValidator;
    @Mock
    private AuthorityValidator authorityValidator;
    @Mock
    private ExceptionStatusPostfixProperties properties;
    @Mock
    private AuthenticatedUserProvider userProvider;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private Order inOrder;
    @Mock
    private Page<Order> orders;
    @Mock
    private Page<OrderDto> dtoOrders;
    @Mock
    private Order outOrder;
    @Mock
    private OrderDto inDtoOrder;
    @Mock
    private OrderDto outDtoOrder;
    @Mock
    private User authUser;
    @Mock
    private UserDto dtoOderUser;
    @Mock
    private GiftCertificate orderGiftCertificate;
    @Mock
    private Pageable pageable;


    private ResourceBundle rb;

    @BeforeEach
    public void setUp() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        rb = new PropertyResourceBundle(contentStream);
        ReflectionTestUtils.setField(orderService, "rb", rb);
    }

    @Test
    void shouldReturnCreatedOrder_On_CreateByAdmin() {
        //Given
        //Check authenticated user role
        when(userProvider.getUserFromAuthentication()).thenReturn(authUser);
        when(authorityValidator.isAdmin()).thenReturn(true);
        //Check if user exists by userId in dtoOrder
        when(inDtoOrder.getUserDto()).thenReturn(dtoOderUser);
        //Map dtoOrder to order
        when(mapper.dtoToModel(inDtoOrder)).thenReturn(inOrder);
        //Check if certificate exists by certificateId in dtoOrder
        when(inOrder.getGiftCertificates()).thenReturn(Collections.singleton(orderGiftCertificate));
        when(giftCertificateService.checkExistActive(orderGiftCertificate.getId()))
                .thenReturn(orderGiftCertificate);
        //Save order
        when(orderRepository.save(inOrder)).thenReturn(outOrder);
        //Map order to dtoOrder
        when(mapper.modelToDto(outOrder)).thenReturn(outDtoOrder);
        //When
        final OrderDto actualOrderDto = orderService.create(inDtoOrder);
        //Then
        assertEquals(outDtoOrder, actualOrderDto);
        verify(userProvider, only()).getUserFromAuthentication();
        verify(authorityValidator, only()).isAdmin();
        verify(orderValidator, only()).createValidate(inDtoOrder, true);
        verify(userService, only()).checkExist(inDtoOrder.getUserDto().getId());
        verify(mapper, times(1)).dtoToModel(inDtoOrder);
        verify(giftCertificateService, only()).checkExistActive(orderGiftCertificate.getId());
        verify(orderRepository, only()).save(inOrder);
        verify(mapper, times(1)).modelToDto(outOrder);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldReturnCreatedOrder_On_CreateByUser() {
        //Given
        //Check authenticated user role
        when(userProvider.getUserFromAuthentication()).thenReturn(authUser);
        when(authorityValidator.isAdmin()).thenReturn(false);
        //Map dtoOrder to order
        when(mapper.dtoToModel(inDtoOrder)).thenReturn(inOrder);
        //Check if certificate exists by certificateId in dtoOrder
        when(inOrder.getGiftCertificates()).thenReturn(Collections.singleton(orderGiftCertificate));
        when(giftCertificateService.checkExistActive(orderGiftCertificate.getId()))
                .thenReturn(orderGiftCertificate);
        //Save order
        when(orderRepository.save(inOrder)).thenReturn(outOrder);
        //Map order to dtoOrder
        when(mapper.modelToDto(outOrder)).thenReturn(outDtoOrder);
        //When
        final OrderDto actualOrderDto = orderService.create(inDtoOrder);
        //Then
        assertEquals(outDtoOrder, actualOrderDto);
        verify(userProvider, only()).getUserFromAuthentication();
        verify(authorityValidator, only()).isAdmin();
        verify(orderValidator, only()).createValidate(inDtoOrder, false);
        verify(mapper, times(1)).dtoToModel(inDtoOrder);
        verify(giftCertificateService, only()).checkExistActive(orderGiftCertificate.getId());
        verify(orderRepository, only()).save(inOrder);
        verify(mapper, times(1)).modelToDto(outOrder);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowException_On_CreateByAdmin_UserNotExist() {
        //Given
        //Check authenticated user role
        when(userProvider.getUserFromAuthentication()).thenReturn(authUser);
        when(authorityValidator.isAdmin()).thenReturn(true);
        //Check if user exists by userId in dtoOrder
        when(inDtoOrder.getUserDto()).thenReturn(dtoOderUser);
        final ServiceException expectedException = new ServiceException(
                rb.getString("user.notFound.id"),
                HttpStatus.NOT_FOUND, properties.getUser(), dtoOderUser.getId());
        when(userService.checkExist(inDtoOrder.getUserDto().getId())).thenThrow(expectedException);
        //When
        final ServiceException actualException
                = assertThrows(ServiceException.class, () -> orderService.create(inDtoOrder));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(userProvider, only()).getUserFromAuthentication();
        verify(authorityValidator, only()).isAdmin();
        verify(orderValidator, only()).createValidate(inDtoOrder, true);
        verify(userService, only()).checkExist(inDtoOrder.getUserDto().getId());
    }

    @Test
    void shouldThrowException_On_CreateByAdmin_CertificateNotExist() {
        //Given
        //Check authenticated user role
        when(userProvider.getUserFromAuthentication()).thenReturn(authUser);
        when(authorityValidator.isAdmin()).thenReturn(true);
        //Check if user exists by userId in dtoOrder
        when(inDtoOrder.getUserDto()).thenReturn(dtoOderUser);
        //Map dtoOrder to order
        when(mapper.dtoToModel(inDtoOrder)).thenReturn(inOrder);
        //Check if certificate exists by certificateId in dtoOrder
        when(inOrder.getGiftCertificates()).thenReturn(Collections.singleton(orderGiftCertificate));
        final ServiceException expectedException = new ServiceException(
                rb.getString("giftCertificate.notFound.id"),
                HttpStatus.NOT_FOUND, properties.getGift(), orderGiftCertificate.getId());
        when(giftCertificateService.checkExistActive(orderGiftCertificate.getId())).thenThrow(expectedException);
        //When
        final ServiceException actualException
                = assertThrows(ServiceException.class, () -> orderService.create(inDtoOrder));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(userProvider, only()).getUserFromAuthentication();
        verify(authorityValidator, only()).isAdmin();
        verify(orderValidator, only()).createValidate(inDtoOrder, true);
        verify(userService, only()).checkExist(inDtoOrder.getUserDto().getId());
        verify(mapper, only()).dtoToModel(inDtoOrder);
        // verify(giftCertificateRepository, only()).findByIdAndActive(orderGiftCertificate.getId(), true);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowException_On_CreateByUser_CertificateNotExist() {
        //Given
        //Check authenticated user role
        when(userProvider.getUserFromAuthentication()).thenReturn(authUser);
        when(authorityValidator.isAdmin()).thenReturn(false);
        //Map dtoOrder to order
        when(mapper.dtoToModel(inDtoOrder)).thenReturn(inOrder);
        //Check if certificate exists by certificateId in dtoOrder
        when(inOrder.getGiftCertificates()).thenReturn(Collections.singleton(orderGiftCertificate));
        final ServiceException expectedException = new ServiceException(
                rb.getString("giftCertificate.notFound.id"),
                HttpStatus.NOT_FOUND, properties.getGift(), orderGiftCertificate.getId());
        when(giftCertificateService.checkExistActive(orderGiftCertificate.getId())).thenThrow(expectedException);
        //When
        final ServiceException actualException
                = assertThrows(ServiceException.class, () -> orderService.create(inDtoOrder));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(userProvider, only()).getUserFromAuthentication();
        verify(authorityValidator, only()).isAdmin();
        verify(orderValidator, only()).createValidate(inDtoOrder, false);
        verify(mapper, only()).dtoToModel(inDtoOrder);
        verify(giftCertificateService, only()).checkExistActive(orderGiftCertificate.getId());
    }

    @Test
    void shouldReturnOrders_On_ReadAllByAdmin() {
        //Given
        when(userProvider.getUserFromAuthentication()).thenReturn(authUser);
        when(authorityValidator.isNotAdmin()).thenReturn(false);
        when(orderRepository.findAll(pageable)).thenReturn(orders);
        when(mapper.modelsToDto(orders)).thenReturn(dtoOrders);
        //When
        final Page<OrderDto> actualDtoOrders = orderService.readAll(pageable);
        //Then
        assertPages(dtoOrders, actualDtoOrders);
        verify(pageableValidator, only()).paginationValidate(pageable);
        verify(authorityValidator, only()).isNotAdmin();
        verify(orderRepository, only()).findAll(pageable);
        verify(mapper, only()).modelsToDto(orders);
    }

    @Test
    void shouldReturnOrders_On_ReadAllByUser() {
        //Given
        when(userProvider.getUserFromAuthentication()).thenReturn(authUser);
        when(authorityValidator.isNotAdmin()).thenReturn(true);
        when(orderRepository.findOrdersByUserId(authUser.getId(), pageable)).thenReturn(orders);
        when(mapper.modelsToDto(orders)).thenReturn(dtoOrders);
        //When
        final Page<OrderDto> actualDtoOrders = orderService.readAll(pageable);
        //Then
        assertPages(dtoOrders, actualDtoOrders);
        verify(pageableValidator, only()).paginationValidate(pageable);
        verify(authorityValidator, only()).isNotAdmin();
        verify(orderRepository, only()).findOrdersByUserId(authUser.getId(), pageable);
        verify(mapper, only()).modelsToDto(orders);
    }

    @Test
    void shouldReturnOrder_On_ReadOneByAdmin() {
        //Given
        when(userProvider.getUserFromAuthentication()).thenReturn(authUser);
        when(authorityValidator.isAdmin()).thenReturn(true);
        when(orderRepository.findById(1)).thenReturn(Optional.of(inOrder));
        when(mapper.modelToDto(inOrder)).thenReturn(inDtoOrder);
        //When
        final OrderDto actualDtoOrder = orderService.readOne(1);
        //Then
        assertEquals(inDtoOrder, actualDtoOrder);
        verify(userProvider, only()).getUserFromAuthentication();
        verify(orderValidator, only()).validateId(1);
        verify(authorityValidator, only()).isAdmin();
        verify(orderRepository, only()).findById(1);
        verify(mapper, only()).modelToDto(inOrder);
    }

    @Test
    void shouldReturnOrder_On_ReadOneByUser() {
        //Given
        when(userProvider.getUserFromAuthentication()).thenReturn(authUser);
        when(authorityValidator.isAdmin()).thenReturn(false);
        when(orderRepository.findOrderByUserIdAndId(authUser.getId(), 1)).thenReturn(Optional.of(inOrder));
        when(mapper.modelToDto(inOrder)).thenReturn(inDtoOrder);
        //When
        final OrderDto actualDtoOrder = orderService.readOne(1);
        //Then
        assertEquals(inDtoOrder, actualDtoOrder);
        verify(userProvider, only()).getUserFromAuthentication();
        verify(orderValidator, only()).validateId(1);
        verify(authorityValidator, only()).isAdmin();
        verify(orderRepository, only()).findOrderByUserIdAndId(authUser.getId(), 1);
        verify(mapper, only()).modelToDto(inOrder);
    }

    @Test
    void shouldThrowServiceException_On_ReadOneByAdmin() {
        //Given
        when(userProvider.getUserFromAuthentication()).thenReturn(authUser);
        when(authorityValidator.isAdmin()).thenReturn(true);
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        final ServiceException expectedException = new ServiceException(
                rb.getString("order.notFound.id"),
                HttpStatus.NOT_FOUND, properties.getOrder(), 1);
        //When
        final ServiceException actualException
                = assertThrows(ServiceException.class, () -> orderService.readOne(1));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(userProvider, only()).getUserFromAuthentication();
        verify(orderValidator, only()).validateId(1);
        verify(authorityValidator, only()).isAdmin();
        verify(orderRepository, only()).findById(1);
    }

    @Test
    void shouldThrowServiceException_On_ReadOneByUser() {
        //Given
        when(userProvider.getUserFromAuthentication()).thenReturn(authUser);
        when(authorityValidator.isAdmin()).thenReturn(false);
        when(orderRepository.findOrderByUserIdAndId(authUser.getId(), 1)).thenReturn(Optional.empty());
        final ServiceException expectedException = new ServiceException(
                rb.getString("order.notFound.user.order"),
                HttpStatus.NOT_FOUND, properties.getOrder(), 1);
        //When
        final ServiceException actualException
                = assertThrows(ServiceException.class, () -> orderService.readOne(1));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(userProvider, only()).getUserFromAuthentication();
        verify(orderValidator, only()).validateId(1);
        verify(authorityValidator, only()).isAdmin();
        verify(orderRepository, only()).findOrderByUserIdAndId(authUser.getId(), 1);
    }

    @Test
    void shouldReturnOrder_On_readOneByUserIdAndOrderId() {
        //Given
        when(orderRepository.findOrderByUserIdAndId(1, 1)).thenReturn(Optional.of(inOrder));
        when(mapper.modelToDto(inOrder)).thenReturn(inDtoOrder);
        //When
        final OrderDto actualDtoOrder = orderService.readOneByUserIdAndOrderId(1, 1);
        //Then
        assertEquals(inDtoOrder, actualDtoOrder);
        verify(orderValidator, times(2)).validateId(1);
        verify(orderRepository, only()).findOrderByUserIdAndId(1, 1);
        verify(mapper, only()).modelToDto(inOrder);
        verifyNoMoreInteractions(orderValidator);
    }

    @Test
    void shouldThrowServiceException_On_readOneByUserIdAndOrderId() {
        //Given
        when(orderRepository.findOrderByUserIdAndId(1, 1)).thenReturn(Optional.empty());
        final ServiceException expectedException = new ServiceException(
                rb.getString("order.notFound.user.order"),
                HttpStatus.NOT_FOUND, properties.getOrder(), 1, 1);
        //When
        final ServiceException actualException
                = assertThrows(ServiceException.class, () -> orderService.readOneByUserIdAndOrderId(1, 1));
        //Then
        assertServiceExceptions(expectedException, actualException);
        verify(orderValidator, times(2)).validateId(1);
        verify(orderRepository, only()).findOrderByUserIdAndId(1, 1);
        verifyNoMoreInteractions(orderValidator);
    }

    @Test
    void shouldReturnOrders_On_readByUserId() {
        //Given
        when(orderRepository.findOrdersByUserId(1, pageable)).thenReturn(orders);
        when(mapper.modelsToDto(orders)).thenReturn(dtoOrders);
        //When
        final Page<OrderDto> actualDtoOrders = orderService.readByUserId(1, pageable);
        //Then
        assertEquals(dtoOrders, actualDtoOrders);
        assertEquals(dtoOrders.getTotalElements(), actualDtoOrders.getTotalElements());
        assertEquals(dtoOrders.getTotalPages(), actualDtoOrders.getTotalPages());
        verify(orderValidator, only()).validateId(1);
        verify(orderRepository, only()).findOrdersByUserId(1, pageable);
        verify(mapper, only()).modelsToDto(orders);
    }
}
