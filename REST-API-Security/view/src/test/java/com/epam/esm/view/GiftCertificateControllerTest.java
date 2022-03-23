package com.epam.esm.view;

import com.epam.esm.service.dto.GiftCertificateDto;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class GiftCertificateControllerTest extends ResponseProvider {

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
        objectMapper.findAndRegisterModules();
    }

    @Test
    public void shouldReturnGiftCertificate_On_ReadByIdEndPoint_ForActiveGiftCertificate_Anonymous() throws Exception {
        //Given
        final TagDto dtoTag = new TagDto(1, "tag1", true);
        final GiftCertificateDto expectedDtoGiftCertificate = new GiftCertificateDto(
                1, "gift1", "d1", 1.0F, 1, true, Collections.singleton(dtoTag));
        //When
        final String response = getResponseOkForGetMethod("/gifts/1", mockMvc);
        final GiftCertificateDto outDtoGiftCertificate = objectMapper.readValue(response, GiftCertificateDto.class);
        //Then
        assertEquals(expectedDtoGiftCertificate, outDtoGiftCertificate);
    }

    @Test
    public void shouldThrowException_On_ReadByIdEndPoint_ForNotPersistedGiftCertificate_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("giftCertificate.notFound.id"), 10);
        //When
        final String response = getResponseNotFoundForGetMethod("/gifts/10", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    public void shouldThrowException_On_ReadByIdEndPoint_ForDisabledGiftCertificate_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("giftCertificate.notFound.id"), 3);
        //When
        final String response = getResponseNotFoundForGetMethod("/gifts/3", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldReturnGiftCertificate_On_ReadByIdEndPoint_For_ActiveGiftCertificate_User() throws Exception {
        //Given
        final TagDto dtoTag = new TagDto(1, "tag1", true);
        final GiftCertificateDto expectedDtoGiftCertificate = new GiftCertificateDto(
                1, "gift1", "d1", 1.0F, 1, true, Collections.singleton(dtoTag));
        //When
        final String response = getResponseOkForGetMethod("/gifts/1", mockMvc);
        final GiftCertificateDto outDtoGiftCertificate = objectMapper.readValue(response, GiftCertificateDto.class);
        //Then
        assertEquals(expectedDtoGiftCertificate, outDtoGiftCertificate);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_ReadByIdEndPoint_ForNotPersistedGiftCertificate_User() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("giftCertificate.notFound.id"), 10);
        //When
        final String response = getResponseNotFoundForGetMethod("/gifts/10", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_ReadByIdEndPoint_ForDisabledGiftCertificate_User() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("giftCertificate.notFound.id"), 3);
        //When
        final String response = getResponseNotFoundForGetMethod("/gifts/3", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnGiftCertificate_On_ReadByIdEndPoint_For_ActiveGiftCertificate_Admin() throws Exception {
        //Given
        final TagDto dtoTag = new TagDto(1, "tag1", true);
        final GiftCertificateDto expectedDtoGiftCertificate = new GiftCertificateDto(
                1, "gift1", "d1", 1.0F, 1, true, Collections.singleton(dtoTag));
        //When
        final String response = getResponseOkForGetMethod("/gifts/1", mockMvc);
        final GiftCertificateDto outGiftCertificateDto = objectMapper.readValue(response, GiftCertificateDto.class);
        //Then
        assertEquals(expectedDtoGiftCertificate, outGiftCertificateDto);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnGiftCertificate_On_ReadByIdEndPoint_For_DisableGiftCertificate_Admin() throws Exception {
        //Given
        final TagDto dtoTag = new TagDto(1, "tag1", true);
        final GiftCertificateDto expectedDtoGiftCertificate = new GiftCertificateDto(
                3, "gift3", "d3", 3.0F, 3, false, Collections.singleton(dtoTag));
        //When
        final String response = getResponseOkForGetMethod("/gifts/3", mockMvc);
        final GiftCertificateDto outGiftCertificate = objectMapper.readValue(response, GiftCertificateDto.class);
        //Then
        assertEquals(expectedDtoGiftCertificate, outGiftCertificate);
    }

    @Test
    public void shouldReturnActiveGiftCertificates_On_ReadEndPoint_Anonymous() throws Exception {
        //Given
        final Set<TagDto> dtoTags1 = Collections.singleton(new TagDto(1, "tag1", true));
        final Set<TagDto> dtoTags2 = Collections.singleton(new TagDto(2, "tag2", true));
        final List<GiftCertificateDto> expectedDtoGiftCertificates
                = Arrays.asList(
                new GiftCertificateDto(1, "gift1", "d1", 1.0F, 1, true, dtoTags1),
                new GiftCertificateDto(2, "gift2", "d2", 2.0F, 2, true, dtoTags1),
                new GiftCertificateDto(4, "gift4", "d4", 4.0F, 4, true, dtoTags2),
                new GiftCertificateDto(5, "gift5", "d5", 5.0F, 5, true, dtoTags2),
                new GiftCertificateDto(6, "gift6", "d6", 6.0F, 6, true, dtoTags2)
        );
        //When
        final List<GiftCertificateDto> outGiftCertificates = getOutGiftCertificatesForGetMethod("/gifts");
        //Then
        assertEquals(expectedDtoGiftCertificates, outGiftCertificates);
        assertEquals(expectedDtoGiftCertificates.size(), outGiftCertificates.size());
    }

    private List<GiftCertificateDto> getOutGiftCertificatesForGetMethod(final String url) throws Exception {
        final MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/gifts"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        final PagedModelDeserializer<GiftCertificateDto> deserializedResponse
                = objectMapper.readValue(
                response.getContentAsString(), new TypeReference<PagedModelDeserializer<GiftCertificateDto>>() {
                });
        return deserializedResponse.getContent();
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldReturnActiveDtoTags_On_ReadEndPoint_User() throws Exception {
        //Given
        final Set<TagDto> dtoTags1 = Collections.singleton(new TagDto(1, "tag1", true));
        final Set<TagDto> dtoTags2 = Collections.singleton(new TagDto(2, "tag2", true));
        final List<GiftCertificateDto> expectedDtoGiftCertificates
                = Arrays.asList(
                new GiftCertificateDto(1, "gift1", "d1", 1.0F, 1, true, dtoTags1),
                new GiftCertificateDto(2, "gift2", "d2", 2.0F, 2, true, dtoTags1),
                new GiftCertificateDto(4, "gift4", "d4", 4.0F, 4, true, dtoTags2),
                new GiftCertificateDto(5, "gift5", "d5", 5.0F, 5, true, dtoTags2),
                new GiftCertificateDto(6, "gift6", "d6", 6.0F, 6, true, dtoTags2)
        );
        //When
        final List<GiftCertificateDto> outGiftCertificates = getOutGiftCertificatesForGetMethod("/gifts");
        //Then
        assertEquals(expectedDtoGiftCertificates, outGiftCertificates);
        assertEquals(expectedDtoGiftCertificates.size(), outGiftCertificates.size());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnAllDtoTags_On_ReadEndPoint_Admin() throws Exception {
        //Given
        final Set<TagDto> dtoTags1 = Collections.singleton(new TagDto(1, "tag1", true));
        final Set<TagDto> dtoTags2 = Collections.singleton(new TagDto(2, "tag2", true));
        final List<GiftCertificateDto> expectedDtoGiftCertificates
                = Arrays.asList(
                new GiftCertificateDto(1, "gift1", "d1", 1.0F, 1, true, dtoTags1),
                new GiftCertificateDto(2, "gift2", "d2", 2.0F, 2, true, dtoTags1),
                new GiftCertificateDto(3, "gift3", "d3", 3.0F, 3, false, dtoTags1),
                new GiftCertificateDto(4, "gift4", "d4", 4.0F, 4, true, dtoTags2),
                new GiftCertificateDto(5, "gift5", "d5", 5.0F, 5, true, dtoTags2)
        );
        //When
        final List<GiftCertificateDto> outGiftCertificates = getOutGiftCertificatesForGetMethod("/gifts");
        //Then
        assertEquals(expectedDtoGiftCertificates, outGiftCertificates);
        assertEquals(expectedDtoGiftCertificates.size(), outGiftCertificates.size());
    }

    @Test
    public void shouldThrowException_On_DeleteEndPoint_Anonymous() throws Exception {
        final String expectedExceptionMessage = UNAUTHORIZED_MESSAGE;
        //When
        final String response = getResponseUnauthorizedForDeleteMethod("/gifts/7", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_DeleteEndPoint_User() throws Exception {
        //Given
        final String expectedExceptionMessage = ACCESS_DENIED_MESSAGE;
        //When
        final String response = getResponseForbiddenForDeleteMethod("/gifts/7", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnDeleted_On_DeleteEndPoint_For_ActiveGiftCertificate_Admin() throws Exception {
        //When
        final String contentAsString = mockMvc.perform(MockMvcRequestBuilders.delete("/gifts/7"))
                .andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn().getResponse().getContentAsString();
        //Then
        // todo return gift
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldThrowException_On_DeleteEndPoint_For_DisabledGiftCertificate_Admin() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("giftCertificate.notFound.id"), 3);
        //When
        final String response = getResponseNotFoundForDeleteMethod("/gifts/3", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    public void shouldThrowException_On_CreateEndPoint_For_GiftCertificate_Anonymous() throws Exception {
        final String expectedExceptionMessage = UNAUTHORIZED_MESSAGE;
        //When
        final String response = getResponseUnauthorizedForPostMethod("/gifts", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_CreateEndPoint_For_GiftCertificate_User() throws Exception {
        final String expectedExceptionMessage = ACCESS_DENIED_MESSAGE;
        //When
        final String response = getResponseForbiddenForPostMethod("/gifts", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturn_On_CreateEndPoint_For_NewGiftCertificateWithOldTag_Admin() throws Exception {
        //Given
        final Set<TagDto> inDtoTags = Collections.singleton(new TagDto("tag1"));
        final GiftCertificateDto inDtoGiftCertificate = new GiftCertificateDto(
                "gift9", "d9", 9.0F, 9, inDtoTags);
        final Set<TagDto> outDtoTags = Collections.singleton(new TagDto(1, "tag1", true));
        //When
        final String innDtoGiftCertificateAsString = objectMapper.writeValueAsString(inDtoGiftCertificate);
        final String outDtoGiftCertificateAsString
                = getResponseCreatedForPostMethodForObjectAsString("/gifts", innDtoGiftCertificateAsString, mockMvc);
        final GiftCertificateDto outDtoGiftCertificate
                = objectMapper.readValue(outDtoGiftCertificateAsString, GiftCertificateDto.class);
        //Then
        assertThat(outDtoGiftCertificate.getId()).isNotNull();
        assertEquals(inDtoGiftCertificate.getName(), outDtoGiftCertificate.getName());
        assertEquals(inDtoGiftCertificate.getDescription(), outDtoGiftCertificate.getDescription());
        assertEquals(inDtoGiftCertificate.getPrice(), outDtoGiftCertificate.getPrice());
        assertEquals(inDtoGiftCertificate.getDuration(), outDtoGiftCertificate.getDuration());
        assertThat(outDtoGiftCertificate.getCreateDate()).isNotNull();
        assertThat(outDtoGiftCertificate.getLastUpdateDate()).isNotNull();
        assertEquals(outDtoTags, outDtoGiftCertificate.getDtoTags());
        assertThat(outDtoGiftCertificate.getActive()).isEqualTo(true);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturn_On_CreateEndPoint_For_NewGiftCertificateWithNewTag_Admin() throws Exception {
        //Given
        final TagDto inTagDto = new TagDto("tagNew");
        final GiftCertificateDto inDtoGiftCertificate = new GiftCertificateDto(
                "gift10", "d10", 10.0F, 10, Collections.singleton(inTagDto));
        //When
        final String innDtoGiftCertificateAsString = objectMapper.writeValueAsString(inDtoGiftCertificate);
        final String outDtoGiftCertificateAsString
                = getResponseCreatedForPostMethodForObjectAsString("/gifts", innDtoGiftCertificateAsString, mockMvc);
        final GiftCertificateDto outDtoGiftCertificate
                = objectMapper.readValue(outDtoGiftCertificateAsString, GiftCertificateDto.class);
        final Set<TagDto> outDtoTags = outDtoGiftCertificate.getDtoTags();
        //Then
        assertThat(outDtoGiftCertificate.getId()).isNotNull();
        assertEquals(inDtoGiftCertificate.getName(), outDtoGiftCertificate.getName());
        assertEquals(inDtoGiftCertificate.getDescription(), outDtoGiftCertificate.getDescription());
        assertEquals(inDtoGiftCertificate.getPrice(), outDtoGiftCertificate.getPrice());
        assertEquals(inDtoGiftCertificate.getDuration(), outDtoGiftCertificate.getDuration());
        assertThat(outDtoGiftCertificate.getCreateDate()).isNotNull();
        assertThat(outDtoGiftCertificate.getLastUpdateDate()).isNotNull();
        assertThat(outDtoGiftCertificate.getActive()).isTrue();

        outDtoTags.forEach(outDtoTag -> {
                    assertThat(outDtoTag.getId()).isNotNull();
                    assertEquals(outDtoTag.getName(), inTagDto.getName());
                    assertThat(outDtoTag.getActive()).isTrue();
                }
        );
    }
//
//    @Test
//    @WithMockUser(authorities = "ROLE_ADMIN")
//    public void shouldReturn_On_CreateEndPoint_For_DisableTag_Admin() throws Exception {
//        //When
//        final TagDto dtoTag = new TagDto();
//        dtoTag.setName("tag8");
//        final String jsonDtoGiftCertificate = objectMapper.writeValueAsString(dtoTag);
//        final String contentAsString = mockMvc.perform(MockMvcRequestBuilders.post("/tags")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonDtoGiftCertificate))
//                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();
//        final TagDto createdDtoTag = objectMapper.readValue(contentAsString, TagDto.class);
//        //Then
//        assertThat(createdDtoTag.getId()).isNotNull();
//        assertEquals(dtoTag.getName(), createdDtoTag.getName());
//        assertThat(createdDtoTag.getActive()).isEqualTo(true);
//    }
}
