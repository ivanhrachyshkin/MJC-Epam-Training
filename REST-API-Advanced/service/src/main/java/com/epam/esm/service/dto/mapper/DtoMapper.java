package com.epam.esm.service.dto.mapper;

import org.springframework.data.domain.Page;

import java.util.List;

public interface DtoMapper<M, D> {

    D modelToDto(M m);

    M dtoToModel(D d);

    Page<D> modelsToDto(Page<M> m);

    List<M> dtoToModels(List<D> r);
}
