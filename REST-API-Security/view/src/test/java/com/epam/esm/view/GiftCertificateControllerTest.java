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

    //Read one method tests
    @Test
    public void shouldReturnGiftCertificate_On_ReadByIdEndPoint_ForActiveGiftCertificate_Anonymous() throws Exception {
        //Given
        final TagDto dtoTag = new TagDto(1, "tag1", true);
        final GiftCertificateDto expectedDtoGiftCertificate = new GiftCertificateDto(
                1, "gift1", "d1", 1.0F, 1,
                null, null, true, Collections.singleton(dtoTag));
        //When
        final String response = getOkForGetMethod("/gifts/1", mockMvc);
        final GiftCertificateDto outDtoGiftCertificate = objectMapper.readValue(response, GiftCertificateDto.class);
        //Then
        assertEquals(expectedDtoGiftCertificate, outDtoGiftCertificate);
    }

    @Test
    public void shouldThrowException_On_ReadByIdEndPoint_For_DisabledGiftCertificate_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("giftCertificate.notFound.id"), 3);
        //When
        final String response = getNotFoundForGetMethod("/gifts/3", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    public void shouldThrowException_On_ReadByIdEndPoint_ForNotPersistedGiftCertificate_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("giftCertificate.notFound.id"), 100);
        //When
        final String response = getNotFoundForGetMethod("/gifts/100", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    public void shouldThrowExceptionInvalid_On_ReadByIdEndPoint_Anonymous() throws Exception {
        //Given
        final String expectedExceptionMessage = rb.getString("invalid.input");
        //When
        final String response = getBadRequestForGetMethod("/gifts/a", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldReturnGiftCertificate_On_ReadByIdEndPoint_For_ActiveGiftCertificate_User() throws Exception {
        //Given
        final TagDto dtoTag = new TagDto(1, "tag1", true);
        final GiftCertificateDto expectedDtoGiftCertificate = new GiftCertificateDto(
                1, "gift1", "d1", 1.0F, 1,
                null, null, true, Collections.singleton(dtoTag));
        //When
        final String response = getOkForGetMethod("/gifts/1", mockMvc);
        final GiftCertificateDto outDtoGiftCertificate = objectMapper.readValue(response, GiftCertificateDto.class);
        //Then
        assertEquals(expectedDtoGiftCertificate, outDtoGiftCertificate);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_ReadByIdEndPoint_ForNotPersistedGiftCertificate_User() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("giftCertificate.notFound.id"), 100);
        //When
        final String response = getNotFoundForGetMethod("/gifts/100", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_ReadByIdEndPoint_ForDisabledGiftCertificate_User() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("giftCertificate.notFound.id"), 3);
        //When
        final String response = getNotFoundForGetMethod("/gifts/3", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturnGiftCertificate_On_ReadByIdEndPoint_For_ActiveGiftCertificate_Admin() throws Exception {
        //Given
        final TagDto dtoTag = new TagDto(1, "tag1", true);
        final GiftCertificateDto expectedDtoGiftCertificate = new GiftCertificateDto(
                1, "gift1", "d1", 1.0F, 1,
                null, null, true, Collections.singleton(dtoTag));
        //When
        final String response = getOkForGetMethod("/gifts/1", mockMvc);
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
                3, "gift3", "d3", 3.0F, 3,
                null, null, false, Collections.singleton(dtoTag));
        //When
        final String response = getOkForGetMethod("/gifts/3", mockMvc);
        final GiftCertificateDto outGiftCertificate = objectMapper.readValue(response, GiftCertificateDto.class);
        //Then
        assertEquals(expectedDtoGiftCertificate, outGiftCertificate);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldThrowException_On_ReadByIdEndPoint_For_NotPersistedGiftCertificate_Admin() throws Exception {
        //Given
        final String expectedExceptionMessage = String.format(rb.getString("giftCertificate.notFound.id"), 100);
        //When
        final String response = getNotFoundForGetMethod("/gifts/100", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    //Read all method test
    @Test
    public void shouldReturnActiveGiftCertificates_On_ReadEndPoint_Anonymous() throws Exception {
        //Given
        final Set<TagDto> dtoTags1 = Collections.singleton(new TagDto(1, "tag1", true));
        final Set<TagDto> dtoTags2 = Collections.singleton(new TagDto(2, "tag2", true));
        final List<GiftCertificateDto> expectedDtoGiftCertificates
                = Arrays.asList(
                new GiftCertificateDto(1, "gift1", "d1", 1.0F, 1,
                        null, null, true, dtoTags1),
                new GiftCertificateDto(2, "gift2", "d2", 2.0F, 2,
                        null, null, true, dtoTags1),
                new GiftCertificateDto(4, "gift4", "d4", 4.0F, 4,
                        null, null, true, dtoTags2),
                new GiftCertificateDto(5, "gift5", "d5", 5.0F, 5,
                        null, null, true, dtoTags2),
                new GiftCertificateDto(6, "gift6", "d6", 6.0F, 6,
                        null, null, true, dtoTags2)
        );
        //When
        final List<GiftCertificateDto> outGiftCertificates = getOutGiftCertificatesForGetMethod("/gifts?page=0&size=5");
        //Then
        assertEquals(expectedDtoGiftCertificates, outGiftCertificates);
        assertEquals(expectedDtoGiftCertificates.size(), outGiftCertificates.size());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldReturnActiveDtoGiftCertificate_On_ReadEndPoint_User() throws Exception {
        //Given
        final Set<TagDto> dtoTags1 = Collections.singleton(new TagDto(1, "tag1", true));
        final Set<TagDto> dtoTags2 = Collections.singleton(new TagDto(2, "tag2", true));
        final List<GiftCertificateDto> expectedDtoGiftCertificates
                = Arrays.asList(
                new GiftCertificateDto(1, "gift1", "d1", 1.0F, 1,
                        null, null, true, dtoTags1),
                new GiftCertificateDto(2, "gift2", "d2", 2.0F, 2,
                        null, null, true, dtoTags1),
                new GiftCertificateDto(4, "gift4", "d4", 4.0F, 4,
                        null, null, true, dtoTags2),
                new GiftCertificateDto(5, "gift5", "d5", 5.0F, 5,
                        null, null, true, dtoTags2),
                new GiftCertificateDto(6, "gift6", "d6", 6.0F, 6,
                        null, null, true, dtoTags2)
        );
        //When
        final List<GiftCertificateDto> outGiftCertificates = getOutGiftCertificatesForGetMethod("/gifts?page=0&size=5");
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
                new GiftCertificateDto(1, "gift1", "d1", 1.0F, 1,
                        null, null, true, dtoTags1),
                new GiftCertificateDto(2, "gift2", "d2", 2.0F, 2,
                        null, null, true, dtoTags1),
                new GiftCertificateDto(3, "gift3", "d3", 3.0F, 3,
                        null, null, false, dtoTags1),
                new GiftCertificateDto(4, "gift4", "d4", 4.0F, 4,
                        null, null, true, dtoTags2),
                new GiftCertificateDto(5, "gift5", "d5", 5.0F, 5,
                        null, null, true, dtoTags2)
        );
        //When
        final List<GiftCertificateDto> outGiftCertificates = getOutGiftCertificatesForGetMethod("/gifts?page=0&size=5");
        //Then
        assertEquals(expectedDtoGiftCertificates, outGiftCertificates);
        assertEquals(expectedDtoGiftCertificates.size(), outGiftCertificates.size());
    }

    //Delete method tests
    @Test
    public void shouldThrowException_On_DeleteEndPoint_Anonymous() throws Exception {
        final String expectedExceptionMessage = UNAUTHORIZED_MESSAGE;
        //When
        final String response = getUnauthorizedForDeleteMethod("/gifts/7", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_DeleteEndPoint_User() throws Exception {
        //Given
        final String expectedExceptionMessage = ACCESS_DENIED_MESSAGE;
        //When
        final String response = getForbiddenForDeleteMethod("/gifts/7", mockMvc);
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
        final String response = getNotFoundForDeleteMethod("/gifts/3", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    public void shouldThrowException_On_CreateEndPoint_For_GiftCertificate_Anonymous() throws Exception {
        final String expectedExceptionMessage = UNAUTHORIZED_MESSAGE;
        //When
        final String response = getUnauthorizedForPostMethod("/gifts", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void shouldThrowException_On_CreateEndPoint_For_GiftCertificate_User() throws Exception {
        final String expectedExceptionMessage = ACCESS_DENIED_MESSAGE;
        //When
        final String response = getForbiddenForPostMethod("/gifts", mockMvc);
        //Then
        assertThat(response).contains(expectedExceptionMessage);
    }

    //Create method tests
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturn_On_CreateEndPoint_For_NewGiftCertificate_With_NewTag_Admin() throws Exception {
        //Given
        final TagDto inTagDto = new TagDto();
        inTagDto.setName("tagNew");
        final GiftCertificateDto inDtoGiftCertificate = new GiftCertificateDto(
                null, "gift9", "d9", 9.0F, 9,
                null, null, null, Collections.singleton(inTagDto));
        //When
        final String innDtoGiftCertificateAsString = objectMapper.writeValueAsString(inDtoGiftCertificate);
        final String outDtoGiftCertificateAsString
                = getCreatedForPostMethodForObjectAsString("/gifts", innDtoGiftCertificateAsString, mockMvc);
        final GiftCertificateDto outDtoGiftCertificate
                = objectMapper.readValue(outDtoGiftCertificateAsString, GiftCertificateDto.class);
        //Then
        assertGiftCertificates(inDtoGiftCertificate, outDtoGiftCertificate);
        assertTags(inDtoGiftCertificate.getDtoTags(), outDtoGiftCertificate.getDtoTags());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturn_On_CreateEndPoint_For_NewGiftCertificate_With_OldActiveTag_Admin() throws Exception {
        //Given
        final TagDto inTagDto = new TagDto();
        inTagDto.setName("tag1");
        final Set<TagDto> inDtoTags = Collections.singleton(inTagDto);
        final GiftCertificateDto inDtoGiftCertificate = new GiftCertificateDto(
                null, "gift10", "d10", 10.0F, 10,
                null, null, null, inDtoTags);
        //When
        final String innDtoGiftCertificateAsString = objectMapper.writeValueAsString(inDtoGiftCertificate);
        final String outDtoGiftCertificateAsString
                = getCreatedForPostMethodForObjectAsString("/gifts", innDtoGiftCertificateAsString, mockMvc);
        final GiftCertificateDto outDtoGiftCertificate
                = objectMapper.readValue(outDtoGiftCertificateAsString, GiftCertificateDto.class);
        //Then
        assertGiftCertificates(inDtoGiftCertificate, outDtoGiftCertificate);
        assertTags(inDtoGiftCertificate.getDtoTags(), outDtoGiftCertificate.getDtoTags());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldReturn_On_CreateEndPoint_For_NewGiftCertificate_With_OldDisabledTag_Admin() throws Exception {
        //Given
        final TagDto inTagDto = new TagDto();
        inTagDto.setName("tag10");
        final GiftCertificateDto inDtoGiftCertificate = new GiftCertificateDto(
                null, "gift11", "d11", 11.0F, 11,
                null, null, null, Collections.singleton(inTagDto));
        //When
        final String innDtoGiftCertificateAsString = objectMapper.writeValueAsString(inDtoGiftCertificate);
        final String outDtoGiftCertificateAsString
                = getCreatedForPostMethodForObjectAsString("/gifts", innDtoGiftCertificateAsString, mockMvc);
        final GiftCertificateDto outDtoGiftCertificate
                = objectMapper.readValue(outDtoGiftCertificateAsString, GiftCertificateDto.class);
        //Then
        assertGiftCertificates(inDtoGiftCertificate, outDtoGiftCertificate);
        assertTags(inDtoGiftCertificate.getDtoTags(), outDtoGiftCertificate.getDtoTags());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void shouldThrowException_On_CreateEndPoint_With_ExistsGiftCertificateName_Admin() throws Exception {
        //Given
        final TagDto inTagDto = new TagDto();
        inTagDto.setName("tag1");
        final GiftCertificateDto inDtoGiftCertificate = new GiftCertificateDto(
                null, "gift1", "d10", 10.0F, 10,
                null, null, null, Collections.singleton(inTagDto));
        final String expectedMessage = String.format(
                rb.getString("giftCertificate.alreadyExists.name"), inDtoGiftCertificate.getName());
        //When
        final String innDtoGiftCertificateAsString = objectMapper.writeValueAsString(inDtoGiftCertificate);
        final String response
                = getConflictForPostMethod("/gifts", innDtoGiftCertificateAsString, mockMvc);
        //Then
        assertThat(response).contains(expectedMessage);
    }

    private List<GiftCertificateDto> getOutGiftCertificatesForGetMethod(final String url) throws Exception {
        final MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        final PagedModelDeserializer<GiftCertificateDto> deserializedResponse
                = objectMapper.readValue(
                response.getContentAsString(), new TypeReference<PagedModelDeserializer<GiftCertificateDto>>() {
                });
        return deserializedResponse.getContent();
    }

    private void assertGiftCertificates(final GiftCertificateDto inDtoGiftCertificate,
                                        final GiftCertificateDto outDtoGiftCertificate) {
        assertThat(outDtoGiftCertificate.getId()).isNotNull();
        assertEquals(inDtoGiftCertificate.getName(), outDtoGiftCertificate.getName());
        assertEquals(inDtoGiftCertificate.getDescription(), outDtoGiftCertificate.getDescription());
        assertEquals(inDtoGiftCertificate.getPrice(), outDtoGiftCertificate.getPrice());
        assertEquals(inDtoGiftCertificate.getDuration(), outDtoGiftCertificate.getDuration());
        assertThat(outDtoGiftCertificate.getCreateDate()).isNotNull();
        assertThat(outDtoGiftCertificate.getLastUpdateDate()).isNotNull();
        assertThat(outDtoGiftCertificate.getActive()).isTrue();
    }

    private void assertTags(final Set<TagDto> inDtoTags, final Set<TagDto> outDtoTags) {
        assertEquals(inDtoTags.size(), outDtoTags.size());
        outDtoTags.forEach(tagDto -> {
            assertThat(tagDto.getId()).isNotNull();
            assertThat(tagDto.getName()).isNotNull();
            assertThat(tagDto.getActive()).isTrue();
        });
    }
}
