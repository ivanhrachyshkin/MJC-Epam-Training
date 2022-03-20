package com.epam.esm.service;

import com.epam.esm.service.dto.RefreshTokenDto;

public interface RefreshTokenService {

    RefreshTokenDto findByToken(String token);

    RefreshTokenDto createRefreshToken(int userId, long refreshTokenDurationMs);
}
