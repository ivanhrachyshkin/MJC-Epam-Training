package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dao.OrderRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.OrderValidator;
import com.epam.esm.service.validator.PaginationValidator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    private final PaginationValidator paginationValidator;

    @Override
    @Transactional
    public OrderDto create(final OrderDto orderDto) {
        orderValidator.createValidate(orderDto);
        final Order order = mapper.dtoToModel(orderDto);
        checkExistByIds(order);
        final Integer userId = order.getUser().getId();
        final Integer giftCertificateId = order.getGiftCertificate().getId();
        userRepository
                .readOne(userId)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("user.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getUser(), userId));
        final GiftCertificate giftCertificate = giftCertificateRepository
                .readOne(giftCertificateId)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("giftCertificate.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getGift(), giftCertificateId));
        order.setPrice(giftCertificate.getPrice());
        final Order newOrder = orderRepository.create(order);
        return mapper.modelToDto(newOrder);
    }

    @Override
    @Transactional
    public List<OrderDto> readAll(final Integer page, final Integer size) {
        paginationValidator.paginationValidate(page, size);
        final List<Order> orders = orderRepository.readAll(page, size);
        return mapper.modelsToDto(orders);
    }

    @Override
    @Transactional
    public List<OrderDto> readAllByUserId(final int userId, final Integer page, final Integer size) {
        paginationValidator.paginationValidate(page, size);
        final List<Order> orders = orderRepository.readAllByUserId(userId, page, size);
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
                readOneByUserIdAndOrderId(userId, orderId)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("order.notFound.user.order"),
                        HttpStatus.NOT_FOUND, properties.getOrder(), userId, orderId));
        return mapper.modelToDto(order);
    }

    private Order checkExist(final int id) {
        orderValidator.validateId(id);
        return orderRepository
                .readOne(id)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("order.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getOrder(), id));
    }

    private void checkExistByIds(final Order order) {
        orderRepository
                .readOneByUserIdAndGiftCertificateId(order.getUser().getId(), order.getGiftCertificate().getId())
                .ifPresent(order1 -> {
                    throw new ServiceException(
                            rb.getString("order.alreadyExists"), HttpStatus.CONFLICT, properties.getOrder());
                });
    }
}
