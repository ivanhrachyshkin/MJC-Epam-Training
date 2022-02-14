package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dao.OrderRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.OrderValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    private final DummyRb dummyRb = new DummyRb();
    @Mock
    private DtoMapper<Order, OrderDto> mapper;
    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderValidator orderValidator;
    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private OrderDto orderDto;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(orderService, "rb", dummyRb);

        final User user = new User(1);
        final GiftCertificate giftCertificate = new GiftCertificate(1);
        final Tag tag1 = new Tag(1);
        final Tag tag2 = new Tag(2);
        giftCertificate.setTags(new HashSet<>(Arrays.asList(tag1, tag2)));

        order = new Order(1);
        order.setUser(user);
        order.setGiftCertificate(giftCertificate);

        final UserDto userDto = new UserDto(1);
        final GiftCertificateDto giftCertificateDto = new GiftCertificateDto(1);
        final TagDto dtoTag1 = new TagDto(1);
        final TagDto dtoTag2 = new TagDto(2);
        giftCertificateDto.setDtoTags(new HashSet<>(Arrays.asList(dtoTag1, dtoTag2)));

        orderDto = new OrderDto(1);
        orderDto.setUserDto(userDto);
        orderDto.setGiftCertificateDto(giftCertificateDto);
    }

    @Test
    void shouldCreateGiftCertificate_On_Create() {
        //Given
        final User user = order.getUser();
        final GiftCertificate giftCertificate = order.getGiftCertificate();

        when(userRepository.readOne(user.getId())).thenReturn(Optional.of(user));
        when(giftCertificateRepository.readOne(giftCertificate.getId())).thenReturn(Optional.of(giftCertificate));
        when(orderRepository.readOneByUserIdAndGiftCertificateId(user.getId(), giftCertificate.getId())).thenReturn(Optional.empty());
        when(orderRepository.create(order)).thenReturn(order);
        when(mapper.dtoToModel(orderDto)).thenReturn(order);
        when(mapper.modelToDto(order)).thenReturn(orderDto);

        //When
        final OrderDto actualOrderDto = orderService.create(orderDto);

        //Then
        assertEquals(orderDto, actualOrderDto);

        verify(orderValidator, only()).createValidate(orderDto);
        verify(giftCertificateRepository, only()).readOne(giftCertificate.getId());
        verify(userRepository, only()).readOne(user.getId());
        verify(orderRepository, times(1)).create(order);
        verify(orderRepository, times(1)).readOneByUserIdAndGiftCertificateId(user.getId(), giftCertificate.getId());
        verifyNoMoreInteractions(orderRepository);
        verify(mapper, times(1)).dtoToModel(orderDto);
        verify(mapper, times(1)).modelToDto(order);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldThrowUserException_On_Create() {
        //Given
        final User user = order.getUser();
        dummyRb.setMessage("user.notFound.id", "User with id = %s not found");
        final String message = String.format("User with id = %s not found", user.getId());
        when(mapper.dtoToModel(orderDto)).thenReturn(order);
        when(userRepository.readOne(user.getId())).thenReturn(Optional.empty());

        //When
        final ServiceException serviceException
                = assertThrows(ServiceException.class, () -> orderService.create(orderDto));
        //Then
        assertEquals(message, serviceException.getMessage());

        verify(orderValidator, only()).createValidate(orderDto);
        verify(mapper, times(1)).dtoToModel(orderDto);
        verify(userRepository, only()).readOne(user.getId());
    }

    @Test
    void shouldThrowGiftCertificateException_On_Create() {
        //Given
        final User user = order.getUser();
        final GiftCertificate giftCertificate = order.getGiftCertificate();
        dummyRb.setMessage("giftCertificate.notFound.id", "Gift certificate with id = %s not found");
        final String message = String.format("Gift certificate with id = %s not found", giftCertificate.getId());
        when(mapper.dtoToModel(orderDto)).thenReturn(order);
        when(userRepository.readOne(user.getId())).thenReturn(Optional.of(user));
        when(giftCertificateRepository.readOne(giftCertificate.getId())).thenReturn(Optional.empty());

        //When
        final ServiceException serviceException
                = assertThrows(ServiceException.class, () -> orderService.create(orderDto));
        //Then
        assertEquals(message, serviceException.getMessage());

        verify(orderValidator, only()).createValidate(orderDto);
        verify(mapper, times(1)).dtoToModel(orderDto);
        verify(userRepository, only()).readOne(user.getId());
        verify(giftCertificateRepository, only()).readOne(giftCertificate.getId());
    }

    @Test
    void shouldThrowOrderException_On_Create() {
        //Given
        final User user = order.getUser();
        final GiftCertificate giftCertificate = order.getGiftCertificate();
        when(userRepository.readOne(user.getId())).thenReturn(Optional.of(user));
        when(giftCertificateRepository.readOne(giftCertificate.getId())).thenReturn(Optional.of(giftCertificate));
        when(orderRepository.readOneByUserIdAndGiftCertificateId(user.getId(), giftCertificate.getId())).thenReturn(Optional.of(order));
        when(mapper.dtoToModel(orderDto)).thenReturn(order);
        dummyRb.setMessage("order.alreadyExists", "Order already exists");
        final String message = "Order already exists";

        //When
        final ServiceException serviceException
                = assertThrows(ServiceException.class, () -> orderService.create(orderDto));

        //Then
        assertEquals(message, serviceException.getMessage());

        verify(orderValidator, only()).createValidate(orderDto);
        verify(giftCertificateRepository, only()).readOne(giftCertificate.getId());
        verify(userRepository, only()).readOne(user.getId());
        verify(orderRepository, only()).readOneByUserIdAndGiftCertificateId(user.getId(), giftCertificate.getId());
        verify(mapper, only()).dtoToModel(orderDto);
    }

    @Test
    void shouldReturnOrders_On_ReadAll() {
        //Given
        final List<Order> orders = Collections.singletonList(order);
        final List<OrderDto> expectedOrders = Collections.singletonList(orderDto);
        //When
        when(orderRepository.readAll()).thenReturn(orders);
        when(mapper.modelsToDto(orders)).thenReturn(expectedOrders);

        final List<OrderDto> actualOrders
                = orderService.readAll();
        //Then
        assertEquals(expectedOrders, actualOrders);
        verify(orderRepository, only()).readAll();
        verify(mapper, only()).modelsToDto(orders);
    }

    @Test
    void shouldReturnOrder_On_ReadOne() {
        //When
        when(orderRepository.readOne(order.getId())).thenReturn(Optional.of(order));
        when(mapper.modelToDto(order)).thenReturn(orderDto);
        final OrderDto actual = orderService.readOne(orderDto.getId());
        //Then
        assertEquals(orderDto, actual);
        verify(orderRepository, only()).readOne(order.getId());
        verify(mapper, only()).modelToDto(order);
    }

    @Test
    void shouldThrowException_On_ReadOne() {
        //Given
        dummyRb.setMessage("order.notFound.id", "Order with id = %s not found");
        when(orderRepository.readOne(order.getId())).thenReturn(Optional.empty());
        final String message = String.format("Order with id = %s not found", order.getId());
        //When
        final ServiceException serviceException = assertThrows(ServiceException.class,
                () -> orderService.readOne(order.getId()));
        //Then
        assertEquals(message, serviceException.getMessage());
        verify(orderRepository, only()).readOne(order.getId());
    }
}