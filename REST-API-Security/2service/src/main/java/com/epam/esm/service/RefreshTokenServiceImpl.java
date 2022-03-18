package com.epam.esm.service;

import com.epam.esm.dao.RefreshTokenRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.model.RefreshToken;
import com.epam.esm.model.User;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.RefreshTokenDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RefreshTokenServiceImpl {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;
    private final DtoMapper<RefreshToken, RefreshTokenDto> mapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenDto findByToken(String token) {
        final RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new ServiceException("token not found",
                        HttpStatus.NOT_FOUND, properties.getAuth()));
        final RefreshToken verifiedToken = verifyExpiration(refreshToken);
        return mapper.modelToDto(verifiedToken);
    }

    @Transactional
    public RefreshTokenDto createRefreshToken(final int userId, final long refreshTokenDurationMs) {
        RefreshToken refreshToken = new RefreshToken();
        final User user = checkExistUserById(userId);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return mapper.modelToDto(refreshToken);
    }

    private User checkExistUserById(final int userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("user.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getUser(), userId));
    }

    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new ServiceException("Refresh token was expired. Please make a new signin request",
                    HttpStatus.UNAUTHORIZED, properties.getAuth());
        }
        return token;
    }
}
