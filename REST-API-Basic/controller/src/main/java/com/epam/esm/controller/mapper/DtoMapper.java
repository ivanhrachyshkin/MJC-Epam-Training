package com.epam.esm.controller.mapper;

import java.util.List;

public interface DtoMapper<M, D> {

    D modelToDto(M m);

    M dtoToModel(D d);

    List<D> modelsToDto(List<M> m);

    List<M> dtoToModels(List<D> r);
}
