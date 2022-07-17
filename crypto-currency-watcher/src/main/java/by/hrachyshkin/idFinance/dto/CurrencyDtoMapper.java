package by.hrachyshkin.idFinance.dto;

import by.hrachyshkin.idFinance.model.Currency;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CurrencyDtoMapper implements DtoMapper<Currency, CurrencyDto> {

    private final ModelMapper modelMapper;

    @Override
    public CurrencyDto modelToDto(final Currency tag) {
        return modelMapper.map(tag, CurrencyDto.class);
    }

    @Override
    public Currency dtoToModel(final CurrencyDto tagDto) {
        return modelMapper.map(tagDto, Currency.class);
    }

    @Override
    public Page<CurrencyDto> modelsToDto(Page<Currency> currencies) {
        final List<CurrencyDto> collect = currencies
                .getContent()
                .stream()
                .map(currency -> modelMapper.map(currency, CurrencyDto.class))
                .collect(Collectors.toList());

        return new PageImpl<>(collect, currencies.getPageable(), currencies.getTotalElements());
    }
}
