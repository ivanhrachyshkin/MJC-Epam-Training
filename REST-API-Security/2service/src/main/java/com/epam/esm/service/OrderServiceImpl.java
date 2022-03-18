package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dao.OrderRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.AuthorityValidator;
import com.epam.esm.service.validator.OrderValidator;
import com.epam.esm.service.validator.PageValidator;
import com.epam.esm.service.validator.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static com.epam.esm.model.Role.Roles.ROLE_USER;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Setter
    private ResourceBundle rb;
    private final Clock clock;
    private final ExceptionStatusPostfixProperties properties;
    private final DtoMapper<Order, OrderDto> mapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final OrderValidator orderValidator;
    private final PageValidator paginationValidator;
    private final AuthorityValidator authorityValidator;

    @Override
    @Transactional
    public OrderDto create(final OrderDto orderDto) {
        final User user = getUserFromAuthentication();
        validateOrderByRoles(user, orderDto);
        final Order order = mapper.dtoToModel(orderDto);
        final Order createdOrder = orderRepository.save(prepareOrderForCreation(order));
        return mapper.modelToDto(createdOrder);
    }

    @Override
    @Transactional
    public Page<OrderDto> readAll(final Pageable pageable) {
        paginationValidator.paginationValidate(pageable);
        final User principal = getUserFromAuthentication();
        final Page<Order> orders = getOrdersForReadAllByUserRole(principal, pageable);
        return mapper.modelsToDto(orders);
    }

    @Override
    @Transactional
    public Page<OrderDto> readAllByUserId(final int userId, final Pageable pageable) {
        paginationValidator.paginationValidate(pageable);
        final Page<Order> orders = orderRepository.findOrdersByUserId(userId, pageable);
        return mapper.modelsToDto(orders);
    }

    @Override
    @Transactional
    public OrderDto readOne(final int id) {
        orderValidator.validateId(id);
        final Order order = checkExist(id);
        return mapper.modelToDto(order);
    }

    @Override
    public OrderDto readOneByUserIdAndOrderId(final int userId, final int orderId) {
        orderValidator.validateId(userId);
        orderValidator.validateId(orderId);
        final Order order = orderRepository.
                findOrdersByUserIdAndId(userId, orderId)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("order.notFound.user.order"),
                        HttpStatus.NOT_FOUND, properties.getOrder(), userId, orderId));
        return mapper.modelToDto(order);
    }

    private Order checkExist(final int id) {
        orderValidator.validateId(id);
        return orderRepository
                .findById(id)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("order.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getOrder(), id));
    }

    private User checkExistUser(final int userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("user.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getUser(), userId));
    }

    private GiftCertificate checkExistGiftCertificateId(final int giftCertificateId) {
        return giftCertificateRepository
                .findById(giftCertificateId)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("giftCertificate.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getGift(), giftCertificateId));
    }

    private Page<Order> getOrdersForReadAllByUserRole(final User user, final Pageable pageable) {
        if (ROLE_USER.equals(user.getRoles().get(0).getRoleName())) {
            return orderRepository.findOrdersByUserId(user.getId(), pageable);
        } else {
            return orderRepository.findAll(pageable);
        }
    }

    private void validateOrderByRoles(final User user, final OrderDto orderDto) {
        if (authorityValidator.validateAuthorityAdmin()) {
            orderValidator.createValidate(orderDto, true);
            checkExistUser(orderDto.getUserDto().getId());
        } else {
            orderValidator.createValidate(orderDto, false);
            orderDto.setUserDto(new UserDto(user.getId()));
        }
    }

    private Order prepareOrderForCreation(final Order order) {
        float sum = 0;
        for (GiftCertificate giftCertificate : order.getGiftCertificates()) {
            final GiftCertificate oldGiftCertificate = checkExistGiftCertificateId(giftCertificate.getId());
            sum += oldGiftCertificate.getPrice();
        }
        order.setPrice(sum);
        order.setDate(LocalDateTime.now(clock));// todo mock method service=spy privatemethod=@visiblefortest
        return order;
    }

    private User getUserFromAuthentication() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String name = authentication.getName();
        return userRepository
                .findByUsername(name).orElseThrow(() ->
                        new ValidationException(
                                rb.getString("user.exists.name"),
                                HttpStatus.NOT_FOUND, properties.getUser()));
    }
}
