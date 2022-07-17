package by.hrachyshkin.idFinance.service;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException {

    private final HttpStatus status;

    public ServiceException(final String message, final HttpStatus status, final Object... args) {
        super(String.format(message, args));
        this.status = status;
    }
}
