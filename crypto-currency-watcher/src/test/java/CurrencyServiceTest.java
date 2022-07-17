import by.hrachyshkin.idFinance.config.Properties;
import by.hrachyshkin.idFinance.dto.CurrencyDto;
import by.hrachyshkin.idFinance.dto.DtoMapper;
import by.hrachyshkin.idFinance.model.Currency;
import by.hrachyshkin.idFinance.repository.CurrencyRepository;
import by.hrachyshkin.idFinance.service.CurrencyServiceImp;
import by.hrachyshkin.idFinance.service.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private DtoMapper<Currency, CurrencyDto> mapper;
    @Mock
    private Properties properties;
    @InjectMocks
    private CurrencyServiceImp currencyService;

    @Mock
    private Currency out;
    @Mock
    private CurrencyDto dtoOut;
    @Mock
    private Page<Currency> outPage;
    @Mock
    private Page<CurrencyDto> outDtoPage;
    @Mock
    private Pageable page;

    @Test
    void shouldReturnCurrencies_On_ReadAll() {
        //Given
        when(currencyRepository.findAll(page)).thenReturn(outPage);
        when(mapper.modelsToDto(outPage)).thenReturn(outDtoPage);
        //When
        final Page<CurrencyDto> actualOutDtoPage = currencyService.readAll(page);
        //Then
        assertEquals(outDtoPage, actualOutDtoPage);
        verify(currencyRepository, only()).findAll(page);
        verify(mapper, only()).modelsToDto(outPage);
    }

    @Test
    void shouldReturnCurrency_On_ReadOne() {
        //Given
        when(currencyRepository.findById(1)).thenReturn(Optional.of(out));
        when(mapper.modelToDto(out)).thenReturn(dtoOut);
        //When
        final CurrencyDto actualDtoCurrency = currencyService.readOne(1);
        //Then
        assertEquals(dtoOut, actualDtoCurrency);
        verify(currencyRepository, only()).findById(1);
        verify(mapper, only()).modelToDto(out);
    }

    @Test
    void shouldThrowServiceException_On_FindById() {
        //Given
        final String expectedMessage = "Not found currency with id = %s";
        when(currencyRepository.findById(1)).thenReturn(Optional.empty());
        when(properties.getNotFound()).thenReturn(expectedMessage);
        final ServiceException expectedException = new ServiceException(
                expectedMessage,
                HttpStatus.NOT_FOUND, 1);
        //When
        final ServiceException actualException = assertThrows(ServiceException.class,
                () -> currencyService.readOne(1));
        //Then
        assertEquals(expectedException.getStatus(), actualException.getStatus());
        verify(currencyRepository, only()).findById(1);
    }
}