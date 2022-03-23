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

    @Test
    public void shouldReturnTag_On_ReadByIdEndPoint_ForActiveTag_Anonymous() throws Exception {
        //Given
        final TagDto expectedDtoTag = new TagDto(1, "tag1", true);
        //When
        final String response = getResponseOkForGetMethod("/tags/1", mockMvc);
        final TagDto outDtoTag = objectMapper.readValue(response, TagDto.class);
        //Then
        assertEquals(expectedDtoTag, outDtoTag);
    }

    @Test
    public void shouldThrowException_On_ReadByIdEndPoint_ForNotPersistedTag_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("tag.notFound.id"), 10);
        //When
        final String response = getResponseNotFoundForGetMethod("/tags/10", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    public void shouldThrowException_On_ReadByIdEndPoint_ForDisabledTag_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("tag.notFound.id"), 3);
        //When
        final String response = getResponseNotFoundForGetMethod("/tags/3", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldReturnTag_On_ReadByIdEndPoint_For_ActiveTag_User() throws Exception {
        //Given
        final TagDto expectedDtoTag = new TagDto(1, "tag1", true);
        //When
        final String response = getResponseOkForGetMethod("/tags/1", mockMvc);
        final TagDto outDtoTag = objectMapper.readValue(response, TagDto.class);
        //Then
        assertEquals(expectedDtoTag, outDtoTag);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_ReadByIdEndPoint_ForNotPersistedTag_User() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("tag.notFound.id"), 10);
        //When
        final String response = getResponseNotFoundForGetMethod("/tags/10", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_ReadByIdEndPoint_ForDisabledTag_User() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("tag.notFound.id"), 3);
        //When
        final String response = getResponseNotFoundForGetMethod("/tags/3", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnTag_On_ReadByIdEndPoint_For_ActiveTag_Admin() throws Exception {
        //Given
        final TagDto expectedDtoTag = new TagDto(1, "tag1", true);
        //When
        final String response = getResponseOkForGetMethod("/tags/1", mockMvc);
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
        final String response = getResponseOkForGetMethod("/tags/3", mockMvc);
        final TagDto outDtoTag = objectMapper.readValue(response, TagDto.class);
        //Then
        assertEquals(expectedDtoTag, outDtoTag);
    }

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

    @Test
    public void shouldThrowException_On_DeleteEndPoint_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = UNAUTHORIZED_MESSAGE;
        //When
        final String response = getResponseUnauthorizedForDeleteMethod("/tags/7", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_DeleteEndPoint_User() throws Exception {
        //Given
        final String expectedExceptionMessage = ACCESS_DENIED_MESSAGE;
        //When
        final String response = getResponseForbiddenForDeleteMethod("/tags/7", mockMvc);
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
        final String response = getResponseNotFoundForDeleteMethod("/tags/3", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    public void shouldThrowException_On_CreateEndPoint_For_NewTag_Anonymous() throws Exception {
        final String expectedExceptionMessage = UNAUTHORIZED_MESSAGE;
        //When
        final String response = getResponseUnauthorizedForPostMethod("/tags", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_CreateEndPoint_For_NewTag_User() throws Exception {
        //Given
        final String expectedExceptionMessage = ACCESS_DENIED_MESSAGE;
        //When
        final String response = getResponseForbiddenForPostMethod("/tags", mockMvc);
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
                = getResponseCreatedForPostMethodForObjectAsString("/tags", inDtoTagAsString, mockMvc);
        final TagDto outDtoTag = objectMapper.readValue(outDtoTagAsString, TagDto.class);
        //Then
        assertThat(outDtoTag.getId()).isNotNull();
        assertEquals(inDtoTag.getName(), outDtoTag.getName());
        assertThat(outDtoTag.getActive()).isEqualTo(true);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnTag_On_CreateEndPoint_For_DisableTag_Admin() throws Exception {
        //Given
        final TagDto inDtoTag = new TagDto("tag8");
        final String inDtoTagAsString = objectMapper.writeValueAsString(inDtoTag);
        //When
        final String outDtoTagAsString
                = getResponseCreatedForPostMethodForObjectAsString("/tags", inDtoTagAsString, mockMvc);
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
