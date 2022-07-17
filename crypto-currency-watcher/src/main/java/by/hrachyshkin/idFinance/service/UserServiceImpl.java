package by.hrachyshkin.idFinance.service;

import by.hrachyshkin.idFinance.config.Properties;
import by.hrachyshkin.idFinance.dto.DtoMapper;
import by.hrachyshkin.idFinance.dto.UserDto;
import by.hrachyshkin.idFinance.model.User;
import by.hrachyshkin.idFinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final Properties properties;
    private final DtoMapper<User, UserDto> dtoMapper;
    private final Validator validator;
    private final QuoteService quoteService;
    private final UserRepository userRepository;
    private User user;

    @Override
    public UserDto create(final UserDto userDto) {
        validator.userValidate(userDto);
        user = dtoMapper.dtoToModel(userDto);
        userRepository.findByName(user.getName()).ifPresent(giftCertificate -> {
            throw new ServiceException(properties.getConflict(), HttpStatus.CONFLICT, user.getName());
        });

        final Float price = quoteService.readTopOneByCurrency(user.getCurrencyId()).getPrice();
        user.setPrice(price);
        final User createdUser = userRepository.save(user);
        return dtoMapper.modelToDto(createdUser);
    }

    @Override
    public void stopLogging() {
        user = null;
    }

    @EventListener(QuotesEvent.class)
    public void logPriceChange() {
        if (user != null) {
            final Float oldPrice = user.getPrice();
            final Float newPrice = quoteService.readTopOneByCurrency(user.getCurrencyId()).getPrice();
            final Float priceChange = (oldPrice - newPrice) / oldPrice * 100;
            if (Math.abs(priceChange) > 1) {
                LOGGER.warn(user.getCurrencyId() + "  " + user.getName() + "  " + priceChange);
            }
        }
    }
}
