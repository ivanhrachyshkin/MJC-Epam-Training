package by.hrachyshkin.idFinance.service;

import by.hrachyshkin.idFinance.dto.UserDto;

public interface UserService {

    UserDto create(final UserDto userDto);

    public void stopLogging();
}
