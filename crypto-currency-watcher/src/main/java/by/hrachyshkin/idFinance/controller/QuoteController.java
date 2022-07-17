package by.hrachyshkin.idFinance.controller;

import by.hrachyshkin.idFinance.dto.QuoteDto;
import by.hrachyshkin.idFinance.service.QuoteService;
import by.hrachyshkin.idFinance.service.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/quotes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class QuoteController {

    private final HateoasCreator hateoasCreator;
    private final QuoteService quoteService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<QuoteDto> readAll(@PageableDefault(page = 0, size = 10) final Pageable pageable) {
        final Page<QuoteDto> quoteDtos = quoteService.readAll(pageable);
        return hateoasCreator.linkQuotesDtos(quoteDtos);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public QuoteDto readOne(@PathVariable final long id) throws ServiceException {
        return hateoasCreator.linkQuoteDto(quoteService.readOne(id));
    }

    @GetMapping(value = "/currencies/{id}/price")
    @ResponseStatus(HttpStatus.OK)
    public QuoteDto readTopOneByCurrency(@PathVariable final int id) throws ServiceException {
        return hateoasCreator.linkQuoteDto(quoteService.readTopOneByCurrency(id));
    }
}
