package com.epam.esm.view;

import com.epam.esm.service.dto.RoleDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.view.hateoas.PagedModelDeserializer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTest extends ResponseProvider {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private ResourceBundle rb;

    @BeforeEach
    public void setUp() throws IOException {
        final InputStream contentStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("message.properties");
        assertNotNull(contentStream);
        rb = new PropertyResourceBundle(contentStream);
        objectMapper = new ObjectMapper();
    }

    //Read one method tests
    @Test
    public void shouldThrowException_On_ReadByIdEndPoint_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = UNAUTHORIZED_MESSAGE;
        //When
        final String response = getUnauthorizedForGetMethod("/users/1", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_ReadByIdEndPoint_User() throws Exception {
        //Given
        final String expectedExceptionMessage = ACCESS_DENIED_MESSAGE;
        //When
        final String response = getForbiddenForGetMethod("/users/1", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnUser_On_ReadByIdEndPoint_Admin() throws Exception {
        //Given
        final UserDto expectedDtoUser = new UserDto(
                1, "username1", "email1", "password1",
                Collections.emptySet(), Collections.emptyList());
        //When
        final String response = getOkForGetMethod("/users/1", mockMvc);
        final UserDto outDtoUser = objectMapper.readValue(response, UserDto.class);
        //Then
        assertEquals(expectedDtoUser, outDtoUser);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldThrowException_On_ReadByIdEndPoint_Admin() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("user.notFound.id"), 100);
        //When
        final String response = getNotFoundForGetMethod("/users/100", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    //Read all method tests
    @Test
    public void shouldThrowException_On_ReadAllEndPoint_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = UNAUTHORIZED_MESSAGE;
        //When
        final String response = getUnauthorizedForGetMethod("/users", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_ReadAllEndPoint_User() throws Exception {
        //Given
        final String expectedExceptionMessage = ACCESS_DENIED_MESSAGE;
        //When
        final String response = getForbiddenForGetMethod("/users/", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnUser_On_ReadAllEndPoint_Admin() throws Exception {
        //Given
        final List<UserDto> expectedDtoUsers
                = Arrays.asList(
                new UserDto(1, "username1", "email1", "password1",
                        Collections.emptySet(), Collections.emptyList()),
                new UserDto(2, "username2", "email2", "password2",
                        Collections.emptySet(), Collections.emptyList()),
                new UserDto(3, "username3", "email3", "password3",
                        Collections.emptySet(), Collections.emptyList()),
                new UserDto(4, "username4", "email4", "password4",
                        Collections.emptySet(), Collections.emptyList()),
                new UserDto(5, "username5", "email5", "password5",
                        Collections.emptySet(), Collections.emptyList()));

        //When
        final List<UserDto> outDtoUsers = getOutUsersForGetMethod("/users");
        //Then
        assertEquals(expectedDtoUsers, outDtoUsers);
    }

    //Create method test
    @Test
    public void shouldReturnCreatedUserFor_On_CreateEndPoint_For_Anonymous() throws Exception {
        //Given
        final UserDto inDtoUser = new UserDto("username6", "email6", "password6",
                null, null);
        final String inDtoTagAsString = objectMapper.writeValueAsString(inDtoUser);
        //When
        final String outDtoUserAsString
                = getCreatedForPostMethodForObjectAsString("/users", inDtoTagAsString, mockMvc);
        final UserDto outDtoUser = objectMapper.readValue(outDtoUserAsString, UserDto.class);
        //Then
        assertDtoUser(inDtoUser, outDtoUser);
        assertEquals(outDtoUser.getDtoRoles(), Collections.singletonList(new RoleDto(2, RoleDto.Roles.ROLE_USER)));
        assertThat(outDtoUser.getDtoOrders()).isEmpty();
    }

    @Test
    public void shouldThrowException_On_CreateEndPoint_With_ExistUsername_For_Anonymous() throws Exception {
        //Given
        final UserDto inDtoUser = new UserDto("username1", "email8", "password8",
                null, null);
        final String expectedMessage = rb.getString("user.exists.name");
        //When
        final String inDtoTagAsString = objectMapper.writeValueAsString(inDtoUser);
        final String response
                = getConflictForPostMethod("/users", inDtoTagAsString, mockMvc);
        //Then
        assertThat(response).contains(expectedMessage);
    }

    @Test
    public void shouldThrowException_On_CreateEndPoint_With_ExistEmail_For_Anonymous() throws Exception {
        //Given
        final UserDto inDtoUser = new UserDto("username100", "email1", "password8",
                null, null);
        final String expectedMessage = rb.getString("user.exists.email");
        //When
        final String inDtoTagAsString = objectMapper.writeValueAsString(inDtoUser);
        final String response
                = getConflictForPostMethod("/users", inDtoTagAsString, mockMvc);
        //Then
        assertThat(response).contains(expectedMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldReturnCreatedUserFor_On_CreateEndPoint_For_User() throws Exception {
        //Given
        final UserDto inDtoUser = new UserDto("username7", "email7", "password7",
                null, null);
        final String inDtoTagAsString = objectMapper.writeValueAsString(inDtoUser);
        //When
        final String outDtoUserAsString
                = getCreatedForPostMethodForObjectAsString("/users", inDtoTagAsString, mockMvc);
        final UserDto outDtoUser = objectMapper.readValue(outDtoUserAsString, UserDto.class);
        //Then
        assertDtoUser(inDtoUser, outDtoUser);
        assertEquals(outDtoUser.getDtoRoles(), Collections.singletonList(new RoleDto(2, RoleDto.Roles.ROLE_USER)));
        assertThat(outDtoUser.getDtoOrders()).isEmpty();
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnCreatedUserFor_On_CreateEndPoint_For_Admin() throws Exception {
        //Given
        final UserDto inDtoUser = new UserDto("username8", "email8", "password8",
                null, null);
        final String inDtoTagAsString = objectMapper.writeValueAsString(inDtoUser);
        //When
        final String outDtoUserAsString
                = getCreatedForPostMethodForObjectAsString("/users", inDtoTagAsString, mockMvc);
        final UserDto outDtoUser = objectMapper.readValue(outDtoUserAsString, UserDto.class);
        //Then
        assertDtoUser(inDtoUser, outDtoUser);
        assertEquals(outDtoUser.getDtoRoles(), Collections.singletonList(new RoleDto(2, RoleDto.Roles.ROLE_USER)));
        assertThat(outDtoUser.getDtoOrders()).isEmpty();
    }

    private List<UserDto> getOutUsersForGetMethod(final String url) throws Exception {
        final MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        final PagedModelDeserializer<UserDto> deserializedResponse
                = objectMapper.readValue(
                response.getContentAsString(), new TypeReference<PagedModelDeserializer<UserDto>>() {
                });
        return deserializedResponse.getContent();
    }

    private void assertDtoUser(final UserDto inDtoUser, final UserDto outDtoUser) throws Exception {
        assertThat(outDtoUser.getId()).isNotNull();
        assertEquals(inDtoUser.getUsername(), outDtoUser.getUsername());
        assertEquals(inDtoUser.getEmail(), outDtoUser.getEmail());
        assertThat(outDtoUser.getPassword()).isNotNull();
        assertNotEquals(inDtoUser.getPassword(), outDtoUser.getPassword());
    }
}