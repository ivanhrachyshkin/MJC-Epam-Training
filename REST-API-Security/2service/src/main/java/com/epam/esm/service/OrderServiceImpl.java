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
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
    private final ExceptionStatusPostfixProperties properties;
    private final DtoMapper<Order, OrderDto> mapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final OrderValidator orderValidator;
    private final PageValidator paginationValidator;
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

    private User checkExistUserById(final int userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("user.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getUser(), userId));
    }

    private GiftCertificate checkExistGiftCertificateById(final int giftCertificateId) {
        return giftCertificateRepository
                .findByIdAndActive(giftCertificateId, true)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("giftCertificate.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getGift(), giftCertificateId));
    }

    private Page<Order> getOrdersForReadAllByUserRole(final User user, final Pageable pageable) {
        return authorityValidator.isNotAdmin()
                ? orderRepository.findOrdersByUserId(user.getId(), pageable)
                : orderRepository.findAll(pageable);
    }

    private void validateOrderByRoles(final User user, final OrderDto orderDto) {
        if (authorityValidator.isAdmin()) {
            orderValidator.createValidate(orderDto, true);
            checkExistUserById(orderDto.getUserDto().getId());
        } else {
            orderValidator.createValidate(orderDto, false);
            orderDto.setUserDto(new UserDto(user.getId()));
        }
    }

    private Order prepareOrderForCreation(final Order order) {
        float sum = 0;
        for (GiftCertificate giftCertificate : order.getGiftCertificates()) {
            final GiftCertificate oldGiftCertificate = checkExistGiftCertificateById(giftCertificate.getId());
            sum += oldGiftCertificate.getPrice();
        }
        order.setPrice(sum);
        return order;
    }
}
