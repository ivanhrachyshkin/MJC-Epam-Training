package com.epam.esm.service.dto.mapper;

import com.epam.esm.model.RefreshToken;
import com.epam.esm.model.User;
import com.epam.esm.service.dto.RefreshTokenDto;
import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RefreshTokenDtoMapper implements DtoMapper<RefreshToken, RefreshTokenDto> {

    private final ModelMapper modelMapper;

    @Override
    public RefreshTokenDto modelToDto(final RefreshToken refreshToken) {
        final RefreshTokenDto refreshTokenDto = modelMapper.map(refreshToken, RefreshTokenDto.class);
        final UserDto dtoUser = modelMapper.map(refreshToken.getUser(), UserDto.class);
        final List<RoleDto> dtoRoles = refreshToken.getUser().getRoles()
                .stream()
                .map(role -> modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toList());
        dtoUser.setDtoRoles(dtoRoles);
        refreshTokenDto.setUserDto(dtoUser);
        return refreshTokenDto;
    }

    @Override
    public RefreshToken dtoToModel(final RefreshTokenDto refreshTokenDto) {
        final RefreshToken refreshToken = modelMapper.map(refreshTokenDto, RefreshToken.class);
        final User user = modelMapper.map(refreshTokenDto.getUserDto(), User.class);
        refreshToken.setUser(user);
        return refreshToken;
    }

    @Override
    public Page<RefreshTokenDto> modelsToDto(Page<RefreshToken> m) {
        return null;
    }
}
