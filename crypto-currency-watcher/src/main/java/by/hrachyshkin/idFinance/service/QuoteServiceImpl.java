package by.hrachyshkin.idFinance.service;

import by.hrachyshkin.idFinance.config.Properties;
import by.hrachyshkin.idFinance.dto.DtoMapper;
import by.hrachyshkin.idFinance.dto.QuoteDto;
import by.hrachyshkin.idFinance.model.Quote;
import by.hrachyshkin.idFinance.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {

    private final Properties properties;
    private final Validator validator;
    private final DtoMapper<Quote, QuoteDto> dtoMapper;
    private final QuoteRepository quoteRepository;

    @Override
    public Page<QuoteDto> readAll(final Pageable pageable) {
        final Page<Quote> quotes = quoteRepository.findAll(pageable);
        return dtoMapper.modelsToDto(quotes);
    }

    @Override
    public QuoteDto readOne(final long id) throws ServiceException {
        validator.longValidate(id);
        final Quote quote = quoteRepository
                .findById(id)
                .orElseThrow(() -> new ServiceException(properties.getNotFound(),
                        HttpStatus.NOT_FOUND, "quote", id));

        return dtoMapper.modelToDto(quote);
    }

    @Override
    public QuoteDto readTopOneByCurrency(final int id) throws ServiceException {
        validator.intValidate(id);
        final Quote quote = quoteRepository
                .findTopQuoteByCurrencyId(id)
                .orElseThrow(() -> new ServiceException(properties.getNotFound(),
                        HttpStatus.NOT_FOUND, "currency", id));
        return dtoMapper.modelToDto(quote);
    }
}
