package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dao.OrderRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.OrderValidator;
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

    private static final String POSTFIX = "04";

    @Setter
    private ResourceBundle rb;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final OrderValidator orderValidator;
    private final DtoMapper<Order, OrderDto> mapper;

    @Override
    @Transactional
    public OrderDto create(final OrderDto orderDto) {
        orderValidator.createValidate(orderDto);
        final Order order = mapper.dtoToModel(orderDto);
        final Integer userId = order.getUser().getId();
        final Integer giftCertificateId = order.getGiftCertificate().getId();
        final User user = userRepository
                .readOne(userId)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("user.notFound.id"), HttpStatus.NOT_FOUND, POSTFIX, userId));
        final GiftCertificate giftCertificate = giftCertificateRepository.readOne(giftCertificateId)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("giftCertificate.notFound.id"), HttpStatus.NOT_FOUND, POSTFIX, giftCertificateId));
        order.setPrice(giftCertificate.getPrice());
        order.setUser(user);
        order.setGiftCertificate(giftCertificate);
        checkExistByIds(order);
        final Order newOrder = orderRepository.create(order);
        return mapper.modelToDto(newOrder);
    }

    @Override
    @Transactional
    public List<OrderDto> readAll(final Integer userId) {
        final List<Order> orders = orderRepository.readAll(userId);
        return mapper.modelsToDto(orders);
    }

    @Override
    @Transactional
    public OrderDto readOne(final int id) {
        final Order order = checkExist(id);
        return mapper.modelToDto(order);
    }

    private Order checkExist(final int id) {
        return orderRepository
                .readOne(id)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("order.notFound.id"), HttpStatus.CONFLICT, POSTFIX, id));
    }

    private void checkExistByIds(final Order order) {
        orderRepository
                .readOneByIds(order.getUser().getId(), order.getGiftCertificate().getId())
                .ifPresent(order1 -> {
                    throw new ServiceException(
                            rb.getString("order.alreadyExists"), HttpStatus.CONFLICT, POSTFIX);
                });
    }
}
