package by.hrachyshkin.idFinance.dto;

import by.hrachyshkin.idFinance.model.Quote;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuoteDtoMapper implements DtoMapper<Quote, QuoteDto> {

    private final ModelMapper modelMapper;

    @Override
    public QuoteDto modelToDto(final Quote quote) {
        return modelMapper.map(quote, QuoteDto.class);
    }

    @Override
    public Quote dtoToModel(final QuoteDto quoteDto) {
        return modelMapper.map(quoteDto, Quote.class);
    }

    @Override
    public Page<QuoteDto> modelsToDto(Page<Quote> quotes) {
        final List<QuoteDto> collect = quotes
                .getContent()
                .stream()
                .map(quote -> modelMapper.map(quote, QuoteDto.class))
                .collect(Collectors.toList());

        return new PageImpl<>(collect, quotes.getPageable(), quotes.getTotalElements());
    }
}
