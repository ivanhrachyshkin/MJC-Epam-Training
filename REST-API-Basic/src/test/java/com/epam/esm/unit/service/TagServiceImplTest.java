//package com.epam.esm.unit.service;
//import com.epam.esm.dao.GiftCertificateTagDao;
//import com.epam.esm.dao.GiftCertificateTagDaoImpl;
//import com.epam.esm.dao.TagDao;
//import com.epam.esm.dao.TagDaoImpl;
//import com.epam.esm.model.Tag;
//import com.epam.esm.service.AlreadyExistException;
//import com.epam.esm.service.NotFoundException;
//import com.epam.esm.service.TagServiceImpl;
//import com.epam.esm.validator.CreateTagValidator;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TagServiceImplTest {
//
//    @Mock
//    private final TagDao tagDao = Mockito.mock(TagDaoImpl.class);
//    private final GiftCertificateTagDao giftCertificateTagDao = Mockito.mock(GiftCertificateTagDaoImpl.class);
//    private final CreateTagValidator createTagValidator = Mockito.mock(CreateTagValidator.class);
//
//    @InjectMocks
//    private TagServiceImpl tagService;
//
//    private final Tag tag = new Tag(1, "tag1");
//    private final Tag tag2 = new Tag(2, "tag2");
//    private final Tag tag3 = new Tag(3, "tag3");
//    private final Tag tag4 = new Tag(4, "tag4");
//
//    @Test
//    void shouldCreateTag_On_Create() {
//        when(tagDao.readOneByName(tag.getName())).thenReturn(null);
//        when(tagDao.create(tag)).thenReturn(tag.getId());
//        final Tag actual = tagService.create(tag);
//        final Tag expected = tag;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void shouldThrowAlreadyExistException_On_Create() {
//        when(tagDao.readOneByName(tag.getName())).thenReturn(tag);
//        final AlreadyExistException alreadyExistException =
//                assertThrows(AlreadyExistException.class, () -> tagService.create(tag));
//        assertEquals("Tag with name = " + tag.getName() + " is already exist", alreadyExistException.getMessage());
//    }
//
//    @Test
//    void shouldReturnTag_On_ReadOne() {
//        when(tagDao.readOne(tag.getId())).thenReturn(tag);
//        final Tag actual = tagService.readOne(tag.getId());
//        final Tag expected = tag;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void shouldReturnTags_On_ReadAll() {
//        final List<Tag> tags = Arrays.asList(tag, tag2, tag3, tag4);
//        when(tagDao.readAll()).thenReturn(tags);
//        final List<Tag> actual = tagService.readAll();
//        final List<Tag> expected = tags;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void shouldDeleteTag_On_DeleteById() {
//        Mockito.doReturn(tag).when(tagDao).readOne(tag.getId());
//        doNothing().when(giftCertificateTagDao).deleteGiftCertificateTagByTagId(tag.getId());
//        tagService.deleteById(tag.getId());
//        verify(tagDao, times(1)).deleteById(tag.getId());
//    }
//
//    @Test
//    void shouldThrowNotFoundException_On_DeleteById() {
//        final NotFoundException notFoundException =
//                assertThrows(NotFoundException.class, () -> tagService.deleteById(tag.getId()));
//        assertEquals("Tag with id = " + tag.getId() + " not found", notFoundException.getMessage());
//    }
//}