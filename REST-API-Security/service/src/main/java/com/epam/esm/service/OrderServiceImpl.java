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
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Setter
    private ResourceBundle rb;
    private final UserServiceImpl userService;
    private final GiftCertificateServiceImpl giftCertificateService;
    private final ExceptionStatusPostfixProperties properties;
    private final DtoMapper<Order, OrderDto> mapper;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final PageableValidator paginationValidator;
    private final AuthorityValidator authorityValidator;
    private final AuthenticatedUserProvider userProvider;

    @Override
    @Transactional
    public OrderDto create(final OrderDto orderDto) {
        final User authUser = userProvider.getUserFromAuthentication();
        validateOrderByRoles(authUser, orderDto);
        final Order order = mapper.dtoToModel(orderDto);
        final Order createdOrder = orderRepository.save(prepareOrderForCreation(order));
        return mapper.modelToDto(createdOrder);
    }

    @Override
    @Transactional
    public Page<OrderDto> readAll(final Pageable pageable) {
        paginationValidator.paginationValidate(pageable);
        final User authUser = userProvider.getUserFromAuthentication();
        final Page<Order> orders = getOrdersForReadAllByUserRole(authUser, pageable);
        return mapper.modelsToDto(orders);
    }

    @Override
    @Transactional
    public OrderDto readOne(final int orderId) {
        orderValidator.validateId(orderId);
        final User authUser = userProvider.getUserFromAuthentication();
        final Order order = authorityValidator.isAdmin()
                ? checkExist(orderId)
                : checkExistByUserIdAndId(authUser.getId(), orderId);
        return mapper.modelToDto(order);
    }

    @Override
    @Transactional
    public Page<OrderDto> readByUserId(final int userId, final Pageable pageable) {
        orderValidator.validateId(userId);
        final Page<Order> orders = orderRepository.findOrdersByUserId(userId, pageable);
        return mapper.modelsToDto(orders);
    }

    @Override
    @Transactional
    public OrderDto readOneByUserIdAndOrderId(final int userId, final int orderId) {
        orderValidator.validateId(userId);
        orderValidator.validateId(orderId);
        final Order order = checkExistByUserIdAndId(userId, orderId);
        return mapper.modelToDto(order);
    }

    @Profile("keycloak")
    @Override
    @Transactional
    public OrderDto createKeycloak(final OrderDto orderDto) {
        orderValidator.createValidate(orderDto, true);
        final Order order = mapper.dtoToModel(orderDto);
        final Order createdOrder = orderRepository.save(prepareOrderForCreation(order));
        return mapper.modelToDto(createdOrder);
    }

    @Profile("keycloak")
    @Override
    @Transactional
    public Page<OrderDto> readAllKeycloak(final Pageable pageable) {
        paginationValidator.paginationValidate(pageable);
        final Page<Order> orders = orderRepository.findAll(pageable);
        return mapper.modelsToDto(orders);
    }

    @Profile("keycloak")
    @Override
    @Transactional
    public OrderDto readOneKeycloak(final int orderId) {
        orderValidator.validateId(orderId);
        final Order order = checkExist(orderId);
        return mapper.modelToDto(order);
    }

    private Order checkExist(final int orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("order.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getOrder(), orderId));
    }

    private Order checkExistByUserIdAndId(final int userId, final int orderId) {
        return orderRepository.
                findOrderByUserIdAndId(userId, orderId)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("order.notFound.user.order"),
                        HttpStatus.NOT_FOUND, properties.getOrder(), userId, orderId));
    }

    private Page<Order> getOrdersForReadAllByUserRole(final User user, final Pageable pageable) {
        return authorityValidator.isNotAdmin()
                ? orderRepository.findOrdersByUserId(user.getId(), pageable)
                : orderRepository.findAll(pageable);
    }

    private void validateOrderByRoles(final User user, final OrderDto orderDto) {
        if (authorityValidator.isAdmin()) {
            orderValidator.createValidate(orderDto, true);
            userService.checkExist(orderDto.getUserDto().getId());
        } else {
            orderValidator.createValidate(orderDto, false);
            orderDto.setUserDto(new UserDto(user.getId()));
        }
    }

    private Order prepareOrderForCreation(final Order order) {
        float sum = 0;
        for (GiftCertificate giftCertificate : order.getGiftCertificates()) {
            final GiftCertificate oldGiftCertificate = giftCertificateService.checkExistActive(giftCertificate.getId());
            sum += oldGiftCertificate.getPrice();
        }
        order.setPrice(sum);
        return order;
    }
}
