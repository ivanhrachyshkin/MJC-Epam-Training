package by.hrachyshkin.idFinance.controller;

import by.hrachyshkin.idFinance.dto.CurrencyDto;
import by.hrachyshkin.idFinance.dto.QuoteDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class HateoasCreator {

    private final PagedResourcesAssembler<CurrencyDto> pagedResourcesAssemblerCurrency;
    private final PagedResourcesAssembler<QuoteDto> pagedResourcesAssemblerQuote;

    @SneakyThrows
    public CurrencyDto linkCurrencyDto(final CurrencyDto currencyDto) {
        return currencyDto
                .add(linkTo(methodOn(CurrencyController.class)
                        .readOne(currencyDto.getId())).withSelfRel());
    }

    public PagedModel<CurrencyDto> linkCurrencyDtos(Page<CurrencyDto> currencyDtos) {
        return pagedResourcesAssemblerCurrency.toModel(currencyDtos, this::linkCurrencyDto);
    }

    @SneakyThrows
    public QuoteDto linkQuoteDto(final QuoteDto quoteDto) {
        return quoteDto
                .add(linkTo(methodOn(QuoteController.class)
                        .readOne(quoteDto.getId())).withSelfRel());
    }

    public PagedModel<QuoteDto> linkQuotesDtos(Page<QuoteDto> quoteDtos) {
        return pagedResourcesAssemblerQuote.toModel(quoteDtos, this::linkQuoteDto);
    }
}
