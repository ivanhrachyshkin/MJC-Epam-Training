package by.hrachyshkin.idFinance.controller;

import by.hrachyshkin.idFinance.dto.CurrencyDto;
import by.hrachyshkin.idFinance.service.CurrencyService;
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
@RequestMapping(value = "/currencies", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CurrencyController {

    private final HateoasCreator hateoasCreator;
    private final CurrencyService currencyService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<CurrencyDto> readAll(@PageableDefault(page = 0, size = 10) final Pageable pageable) {
        final Page<CurrencyDto> dtoCurrencies = currencyService.readAll(pageable);
        return hateoasCreator.linkCurrencyDtos(dtoCurrencies);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CurrencyDto readOne(@PathVariable final int id) throws ServiceException {
        final CurrencyDto tagDto = currencyService.readOne(id);
        return hateoasCreator.linkCurrencyDto(tagDto);
    }
}
