package by.hrachyshkin.idFinance.dto;

import by.hrachyshkin.idFinance.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDtoMapper implements DtoMapper<User, UserDto> {

    private final ModelMapper modelMapper;

    @Override
    public UserDto modelToDto(final User user) {
        return modelMapper.map(user, UserDto.class);

    }

    @Override
    public User dtoToModel(final UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    @Override
    public Page<UserDto> modelsToDto(Page<User> users) {
        final List<UserDto> collect = users
                .getContent()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        return new PageImpl<>(collect, users.getPageable(), users.getTotalElements());
    }
}
