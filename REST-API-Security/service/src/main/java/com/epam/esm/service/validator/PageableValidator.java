package com.epam.esm.service.validator;

import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class PageableValidator {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;

    public void paginationValidate(final Pageable pageable) {

        if (ObjectUtils.isEmpty(pageable)) {
            throwValidationException("validator.null.pagination");
        }

        if (pageable.getPageNumber() < 0 || pageable.getPageSize() < 1) {
            throwValidationException("validator.negative.pagination");
        }

        pageable.getSort().toList().forEach(this::validateSort);
    }

    private void validateSort(final Sort.Order sort) {
        final List<String> allowedSorts = Arrays.asList("id", "name", "createDate");

        if (!allowedSorts.contains(sort.getProperty())){
            throwValidationException("validator.sort.invalid");
        }
    }

    private void throwValidationException(final String rbKey) {
        throw new ValidationException(rb.getString(rbKey), HttpStatus.BAD_REQUEST, properties.getPagination());
    }
}
