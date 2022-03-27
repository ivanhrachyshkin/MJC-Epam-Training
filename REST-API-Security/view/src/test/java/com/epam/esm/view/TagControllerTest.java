package com.epam.esm.view;

import com.epam.esm.service.dto.TagDto;
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
import java.util.Arrays;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TagControllerTest extends ResponseProvider {

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

    //Read one method test
    @Test
    public void shouldReturnTag_On_ReadByIdEndPoint_ForActiveTag_Anonymous() throws Exception {
        //Given
        final TagDto expectedDtoTag = new TagDto(1, "tag1", true);
        //When
        final String response = getOkForGetMethod("/tags/1", mockMvc);
        final TagDto outDtoTag = objectMapper.readValue(response, TagDto.class);
        //Then
        assertEquals(expectedDtoTag, outDtoTag);
    }

    @Test
    public void shouldThrowException_On_ReadByIdEndPoint_ForNotPersistedTag_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("tag.notFound.id"), 100);
        //When
        final String response = getNotFoundForGetMethod("/tags/100", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    public void shouldThrowExceptionInvalid_On_ReadByIdEndPoint_ForNotPersistedTag_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = rb.getString("invalid.input");
        //When
        final String response = getBadRequestForGetMethod("/tags/a", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    public void shouldThrowException_On_ReadByIdEndPoint_ForDisabledTag_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("tag.notFound.id"), 3);
        //When
        final String response = getNotFoundForGetMethod("/tags/3", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldReturnTag_On_ReadByIdEndPoint_For_ActiveTag_User() throws Exception {
        //Given
        final TagDto expectedDtoTag = new TagDto(1, "tag1", true);
        //When
        final String response = getOkForGetMethod("/tags/1", mockMvc);
        final TagDto outDtoTag = objectMapper.readValue(response, TagDto.class);
        //Then
        assertEquals(expectedDtoTag, outDtoTag);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_ReadByIdEndPoint_ForNotPersistedTag_User() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("tag.notFound.id"), 100);
        //When
        final String response = getNotFoundForGetMethod("/tags/100", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_ReadByIdEndPoint_ForDisabledTag_User() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("tag.notFound.id"), 3);
        //When
        final String response = getNotFoundForGetMethod("/tags/3", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowExceptionInvalid_On_ReadByIdEndPoint_ForNotPersistedTag_User() throws Exception {
        //Given
        final String expectedExceptionMessage = rb.getString("invalid.input");
        //When
        final String response = getBadRequestForGetMethod("/tags/a", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnTag_On_ReadByIdEndPoint_For_ActiveTag_Admin() throws Exception {
        //Given
        final TagDto expectedDtoTag = new TagDto(1, "tag1", true);
        //When
        final String response = getOkForGetMethod("/tags/1", mockMvc);
        final TagDto outDtoTag = objectMapper.readValue(response, TagDto.class);
        //Then
        assertEquals(expectedDtoTag, outDtoTag);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnTag_On_ReadByIdEndPoint_For_DisableTag_Admin() throws Exception {
        //Given
        final TagDto expectedDtoTag = new TagDto(3, "tag3", false);
        //When
        final String response = getOkForGetMethod("/tags/3", mockMvc);
        final TagDto outDtoTag = objectMapper.readValue(response, TagDto.class);
        //Then
        assertEquals(expectedDtoTag, outDtoTag);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldThrowException_On_ReadByIdEndPoint_ForNotPersistedTag_Admin() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("tag.notFound.id"), 100);
        //When
        final String response = getNotFoundForGetMethod("/tags/100", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldThrowExceptionInvalid_On_ReadByIdEndPoint_ForNotPersistedTag_Admin() throws Exception {
        //Given
        final String expectedExceptionMessage = rb.getString("invalid.input");
        //When
        final String response = getBadRequestForGetMethod("/tags/a", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    //Read all method test
    @Test
    public void shouldReturnActiveTags_On_ReadEndPoint_Anonymous() throws Exception {
        //Given
        final List<TagDto> expectedDtoTags = Arrays.asList(
                new TagDto(1, "tag1", true),
                new TagDto(2, "tag2", true),
                new TagDto(4, "tag4", true),
                new TagDto(5, "tag5", true),
                new TagDto(6, "tag6", true));
        //When
        final List<TagDto> outDtoTags = getOutTagsForGetMethod("/tags");
        //Then
        assertEquals(expectedDtoTags, outDtoTags);
        assertEquals(expectedDtoTags.size(), outDtoTags.size());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldReturnActiveTags_On_ReadEndPoint_User() throws Exception {
        //Given
        final List<TagDto> expectedDtoTags = Arrays.asList(
                new TagDto(1, "tag1", true),
                new TagDto(2, "tag2", true),
                new TagDto(4, "tag4", true),
                new TagDto(5, "tag5", true),
                new TagDto(6, "tag6", true));
        //When
        final List<TagDto> outDtoTags = getOutTagsForGetMethod("/tags");
        //Then
        assertEquals(expectedDtoTags, outDtoTags);
        assertEquals(expectedDtoTags.size(), outDtoTags.size());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnAllTags_On_ReadEndPoint_Admin() throws Exception {
        //Given
        final List<TagDto> expectedDtoTags = Arrays.asList(
                new TagDto(1, "tag1", true),
                new TagDto(2, "tag2", true),
                new TagDto(3, "tag3", false),
                new TagDto(4, "tag4", true),
                new TagDto(5, "tag5", true));
        //When
        final List<TagDto> outDtoTags = getOutTagsForGetMethod("/tags");
        //Then
        assertEquals(expectedDtoTags, outDtoTags);
        assertEquals(expectedDtoTags.size(), outDtoTags.size());
    }

    //Delete method test
    @Test
    public void shouldThrowException_On_DeleteEndPoint_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = UNAUTHORIZED_MESSAGE;
        //When
        final String response = getUnauthorizedForDeleteMethod("/tags/7", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_DeleteEndPoint_User() throws Exception {
        //Given
        final String expectedExceptionMessage = ACCESS_DENIED_MESSAGE;
        //When
        final String response = getForbiddenForDeleteMethod("/tags/7", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnDeletedTag_On_DeleteEndPoint_ForActiveTag_Admin() throws Exception {
        //When
        final String contentAsString = mockMvc.perform(MockMvcRequestBuilders.delete("/tags/7"))
                .andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn().getResponse().getContentAsString();
        //Then
        // todo return tag
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldThrowException_On_DeleteEndPoint_For_DisabledTag_Admin() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("tag.notFound.id"), 3);
        //When
        final String response = getNotFoundForDeleteMethod("/tags/3", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    //Create method tests
    @Test
    public void shouldThrowException_On_CreateEndPoint_Anonymous() throws Exception {
        final String expectedExceptionMessage = UNAUTHORIZED_MESSAGE;
        //When
        final String response = getUnauthorizedForPostMethod("/tags", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_CreateEndPoint_User() throws Exception {
        //Given
        final String expectedExceptionMessage = ACCESS_DENIED_MESSAGE;
        //When
        final String response = getForbiddenForPostMethod("/tags", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturn_On_CreateEndPoint_For_NewTag_Admin() throws Exception {
        //Given
        final TagDto inDtoTag = new TagDto("tag9");
        final String inDtoTagAsString = objectMapper.writeValueAsString(inDtoTag);
        //When
        final String outDtoTagAsString
                = getCreatedForPostMethodForObjectAsString("/tags", inDtoTagAsString, mockMvc);
        final TagDto outDtoTag = objectMapper.readValue(outDtoTagAsString, TagDto.class);
        //Then
        assertThat(outDtoTag.getId()).isNotNull();
        assertEquals(inDtoTag.getName(), outDtoTag.getName());
        assertThat(outDtoTag.getActive()).isEqualTo(true);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldThrowException_On_CreateEndPoint_For_ExistTagName_Admin() throws Exception {
        //Given
        final TagDto inDtoTag = new TagDto("tag1");
        final String inDtoTagAsString = objectMapper.writeValueAsString(inDtoTag);
        final String expectedMessage = String.format(rb.getString("tag.alreadyExists.name"), inDtoTag.getName());
        //When
        final String outDtoTagAsString
                = getConflictForPostMethod("/tags", inDtoTagAsString, mockMvc);
        //Then
        assertThat(outDtoTagAsString).contains(expectedMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldThrowException_On_CreateEndPoint_With_Id_Admin() throws Exception {
        //Given
        final TagDto inDtoTag = new TagDto(1);
        final String inDtoTagAsString = objectMapper.writeValueAsString(inDtoTag);
        final String expectedMessage = rb.getString("validator.id.should.not.passed");
        //When
        final String response
                = getBadRequestForPostMethod("/tags", inDtoTagAsString, mockMvc);
        //Then
        assertThat(response).contains(expectedMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldThrowException_On_CreateEndPoint_With_EmptyName_Admin() throws Exception {
        //Given
        final TagDto inDtoTag = new TagDto("");
        final String inDtoTagAsString = objectMapper.writeValueAsString(inDtoTag);
        final String expectedMessage = rb.getString("validator.tag.name.required");
        //When
        final String response
                = getBadRequestForPostMethod("/tags", inDtoTagAsString, mockMvc);
        //Then
        assertThat(response).contains(expectedMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldThrowException_On_CreateEndPoint_With_Active_Admin() throws Exception {
        //Given
        final TagDto inDtoTag = new TagDto("n", true);
        final String inDtoTagAsString = objectMapper.writeValueAsString(inDtoTag);
        final String expectedMessage = rb.getString("validator.active.should.not.passed");
        //When
        final String response
                = getBadRequestForPostMethod("/tags", inDtoTagAsString, mockMvc);
        //Then
        assertThat(response).contains(expectedMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnTag_On_CreateEndPoint_For_DisableTag_Admin() throws Exception {
        //Given
        final TagDto inDtoTag = new TagDto("tag8");
        final String inDtoTagAsString = objectMapper.writeValueAsString(inDtoTag);
        //When
        final String outDtoTagAsString
                = getCreatedForPostMethodForObjectAsString("/tags", inDtoTagAsString, mockMvc);
        final TagDto outDtoTag = objectMapper.readValue(outDtoTagAsString, TagDto.class);
        //Then
        assertThat(outDtoTag.getId()).isNotNull();
        assertEquals(inDtoTag.getName(), outDtoTag.getName());
        assertThat(outDtoTag.getActive()).isEqualTo(true);
    }

    private List<TagDto> getOutTagsForGetMethod(final String url) throws Exception {
        final MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        final PagedModelDeserializer<TagDto> deserializedResponse
                = objectMapper.readValue(
                response.getContentAsString(), new TypeReference<PagedModelDeserializer<TagDto>>() {
                });
        return deserializedResponse.getContent();
    }
}
