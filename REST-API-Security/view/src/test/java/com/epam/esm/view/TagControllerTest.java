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
public class TagControllerTest {

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
    public void shouldReturnDtoTag_On_ReadByIdEndPoint_ForActiveTag_Anonymous() throws Exception {
        //Given
        final TagDto expectedDtoTag = new TagDto(1, "tag1", true);
        //When
        final MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/tags/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        final String responseContent = response.getContentAsString();
        System.out.println(responseContent);
        final TagDto actualTagDto = objectMapper.readValue(responseContent, TagDto.class);
        //Then
        assertEquals(expectedDtoTag, actualTagDto);
    }

    @Test
    public void shouldThrowException_On_ReadByIdEndPoint_ForNotPersistedTag_Anonymous() throws Exception {
        //When
        final String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/tags/10"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn().getResponse().getContentAsString();
        //Then
        assertThat(contentAsString).contains(String.format(rb.getString("tag.notFound.id"), 10));
    }

    @Test
    public void shouldThrowException_On_ReadByIdEndPoint_ForDisabledTag_Anonymous() throws Exception {
        //When
        final String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/tags/3"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn().getResponse().getContentAsString();
        //Then
        assertThat(contentAsString).contains(String.format(rb.getString("tag.notFound.id"), 3));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldReturnDtoTag_On_ReadByIdEndPoint_For_ActiveTag_User() throws Exception {
        //Given
        final TagDto expectedDtoTag = new TagDto(1, "tag1", true);
        //When
        final MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/tags/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        final String responseContent = response.getContentAsString();
        final TagDto actualTagDto = objectMapper.readValue(responseContent, TagDto.class);
        //Then
        assertEquals(expectedDtoTag, actualTagDto);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_ReadByIdEndPoint_ForNotPersistedTag_User() throws Exception {
        //When
        final String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/tags/10"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn().getResponse().getContentAsString();
        //Then
        assertThat(contentAsString).contains(String.format(rb.getString("tag.notFound.id"), 10));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_ReadByIdEndPoint_ForDisabledTag_User() throws Exception {
        //When
        final String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/tags/3"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn().getResponse().getContentAsString();
        //Then
        assertThat(contentAsString).contains(String.format(rb.getString("tag.notFound.id"), 3));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnDtoTag_On_ReadByIdEndPoint_For_ActiveTag_Admin() throws Exception {
        //Given
        final TagDto expectedDtoTag = new TagDto(1, "tag1", true);
        //When
        final MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/tags/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        final String responseContent = response.getContentAsString();
        final TagDto actualTagDto = objectMapper.readValue(responseContent, TagDto.class);
        //Then
        assertEquals(expectedDtoTag, actualTagDto);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnDtoTag_On_ReadByIdEndPoint_For_DisableTag_Admin() throws Exception {
        //Given
        final TagDto expectedDtoTag = new TagDto(3, "tag3", false);
        //When
        final MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/tags/3"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        final String responseContent = response.getContentAsString();
        final TagDto actualTagDto = objectMapper.readValue(responseContent, TagDto.class);
        //Then
        assertEquals(expectedDtoTag, actualTagDto);
    }

    @Test
    public void shouldReturnActiveDtoTags_On_ReadEndPoint_Anonymous() throws Exception {
        //Given
        final List<TagDto> expectedDtoTags = Arrays.asList(
                new TagDto(1, "tag1", true),
                new TagDto(2, "tag2", true));
        //When
        final MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/tags"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        final PagedModelDeserializer<TagDto> deserializedResponse
                = objectMapper.readValue(
                response.getContentAsString(), new TypeReference<PagedModelDeserializer<TagDto>>() {
                });
        final List<TagDto> actualDtoTags = deserializedResponse.getContent();
        //Then
        assertEquals(expectedDtoTags, actualDtoTags);
        assertEquals(expectedDtoTags.size(), actualDtoTags.size());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldReturnActiveDtoTags_On_ReadEndPoint_User() throws Exception {
        //Given
        final List<TagDto> expectedDtoTags = Arrays.asList(
                new TagDto(1, "tag1", true),
                new TagDto(2, "tag2", true));
        //When
        final MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/tags"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        final PagedModelDeserializer<TagDto> deserializedResponse
                = objectMapper.readValue(
                response.getContentAsString(), new TypeReference<PagedModelDeserializer<TagDto>>() {
                });
        final List<TagDto> actualDtoTags = deserializedResponse.getContent();
        //Then
        assertEquals(expectedDtoTags, actualDtoTags);
        assertEquals(expectedDtoTags.size(), actualDtoTags.size());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnAllDtoTags_On_ReadEndPoint_Admin() throws Exception {
        //Given
        final List<TagDto> expectedDtoTags = Arrays.asList(
                new TagDto(1, "tag1", true),
                new TagDto(2, "tag2", true),
                new TagDto(3, "tag3", false));
        //When
        final MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/tags"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        final PagedModelDeserializer<TagDto> deserializedResponse
                = objectMapper.readValue(
                response.getContentAsString(), new TypeReference<PagedModelDeserializer<TagDto>>() {
                });
        final List<TagDto> actualDtoTags = deserializedResponse.getContent();
        //Then
        assertEquals(expectedDtoTags, actualDtoTags);
        assertEquals(expectedDtoTags.size(), actualDtoTags.size());
    }

    @Test
    public void shouldThrowException_On_DeleteEndPoint_Anonymous() throws Exception {
        //When
        final String contentAsString = mockMvc.perform(MockMvcRequestBuilders.delete("/tags/1"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()).andReturn().getResponse().getContentAsString();
        //Then
        //assertThat(contentAsString).contains(String.format(rb.getString("tag.notFound.id"), 3)); // todo exception message
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_DeleteEndPoint_User() throws Exception {
        //When
        final String contentAsString = mockMvc.perform(MockMvcRequestBuilders.delete("/tags/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn().getResponse().getContentAsString();
        //Then
        //assertThat(contentAsString).contains(String.format(rb.getString("tag.notFound.id"), 3)); // todo exception message
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnDeleted_On_DeleteEndPoint_ForActiveTag_Admin() throws Exception {
        //When
        final String contentAsString = mockMvc.perform(MockMvcRequestBuilders.delete("/tags/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn().getResponse().getContentAsString();
        //Then
        //assertThat(contentAsString).contains(String.format(rb.getString("tag.notFound.id"), 3)); // todo return tag
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldThrowException_On_DeleteEndPoint_For_DisabledTag_Admin() throws Exception {
        //When
        final String contentAsString = mockMvc.perform(MockMvcRequestBuilders.delete("/tags/3"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn().getResponse().getContentAsString();
        //Then
        assertThat(contentAsString).contains(String.format(rb.getString("tag.notFound.id"), 3));
    }
}
