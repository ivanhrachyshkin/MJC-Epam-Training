package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dao.GiftCertificateSpecification;
import com.epam.esm.model.Category;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.CategoryDto;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateRequestParamsContainer;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.AuthorityValidator;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.PageableValidator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {


    @Setter
    private ResourceBundle rb;
    private final TagServiceImpl tagService;
    private final ExceptionStatusPostfixProperties properties;
    private final DtoMapper<GiftCertificate, GiftCertificateDto> dtoMapper;
    private final DtoMapper<Category, CategoryDto> categoryMapper;
    private final GiftCertificateMapper giftCertificateMapper;
    private final GiftCertificateRepository giftCertificateRepository;
    private final GiftCertificateValidator giftCertificateValidator;
    private final CategoryService categoryService;
    private final PageableValidator paginationValidator;
    private final AuthorityValidator authorityValidator;
    private final GiftCertificateSpecification specification;
    private final HttpServletRequest request;


    @Override
    @Transactional
    public GiftCertificateDto create(final GiftCertificateDto rawGiftCertificateDto) {
        rawGiftCertificateDto.trim();
        giftCertificateValidator.validateCreateOrUpdate(rawGiftCertificateDto, request.getMethod());
        final GiftCertificate rawGiftCertificate = dtoMapper.dtoToModel(rawGiftCertificateDto);
        rawGiftCertificate.setActive(true);
        final Set<Tag> rawTags = rawGiftCertificate.getTags();
        tagService.prepareTagsForGiftCertificate(rawTags);
        final GiftCertificate preparedGiftCertificate = prepareGiftCertificateForCreate(rawGiftCertificate);
        final GiftCertificate createdGiftCertificate = giftCertificateRepository.save(preparedGiftCertificate);
        return dtoMapper.modelToDto(createdGiftCertificate);
    }

    @Override
    @Transactional
    public GiftCertificateDto update(final GiftCertificateDto rawGiftCertificateDto) {
        rawGiftCertificateDto.trim();
        giftCertificateValidator.validateCreateOrUpdate(rawGiftCertificateDto, request.getMethod());
        checkExistByName(rawGiftCertificateDto.getName());
        final GiftCertificate oldGiftCertificate = checkExist(rawGiftCertificateDto.getId());
        final GiftCertificate rawGiftCertificate = dtoMapper.dtoToModel(rawGiftCertificateDto);
        final Set<Tag> rawTags = rawGiftCertificate.getTags();
        tagService.prepareTagsForGiftCertificate(rawTags);
        giftCertificateMapper.updateGiftCertificateFromRaw(rawGiftCertificate, oldGiftCertificate);
        final GiftCertificate updatedGiftCertificate = giftCertificateRepository.save(oldGiftCertificate);
        return dtoMapper.modelToDto(updatedGiftCertificate);
    }

    @Override
    @Transactional
    public Page<GiftCertificateDto> readAll(final List<String> tags,
                                            final GiftCertificateRequestParamsContainer container,
                                            final Pageable pageable) {
        giftCertificateValidator.readAllValidate(tags, container);
        paginationValidator.paginationValidate(pageable);
        final String giftCertificateName = container.getName();
        final String giftCertificateDescription = container.getDescription();
        final Page<GiftCertificate> giftCertificates
                = getGiftCertificatesByUserRole(tags, giftCertificateName, giftCertificateDescription, pageable);
        return dtoMapper.modelsToDto(giftCertificates);
    }

    @Override
    public Page<GiftCertificateDto> readAllByCategory(final CategoryDto categoryDto, final Pageable pageable) {
        paginationValidator.paginationValidate(pageable);
        final CategoryDto oldCategoryDto = categoryService.readOneByName(categoryDto.getName());
        final Category category = categoryMapper.dtoToModel(oldCategoryDto);
        final Page<GiftCertificate> giftCertificates
                = giftCertificateRepository.findAllByCategory(category, pageable);
        return dtoMapper.modelsToDto(giftCertificates);
    }

    @Override
    @Transactional
    public GiftCertificateDto readOne(final int id) {
        giftCertificateValidator.validateId(id);
        final GiftCertificate giftCertificate = getGiftCertificateByUserRole(id);
        return dtoMapper.modelToDto(giftCertificate);
    }

    @Override
    @Transactional
    public GiftCertificateDto deleteById(final int id) {
        giftCertificateValidator.validateId(id);
        final GiftCertificate oldGiftCertificate = checkExistActive(id);
        oldGiftCertificate.setActive(false);
        final GiftCertificate deletedGiftCertificate = giftCertificateRepository.save(oldGiftCertificate);
        return dtoMapper.modelToDto(deletedGiftCertificate);
    }

    public GiftCertificate checkExistActive(final int id) {
        return giftCertificateRepository
                .findByIdAndActive(id, true)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("giftCertificate.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getGift(), id));
    }


    private Page<GiftCertificate> getGiftCertificatesByUserRole(final List<String> tags,
                                                                final String giftCertificateName,
                                                                final String giftCertificateDescription,
                                                                final Pageable pageable) {
        final Boolean isActive = authorityValidator.isNotAdmin();
        return giftCertificateRepository
                .findAll(specification.giftCertificateFiltered(
                        tags, giftCertificateName, giftCertificateDescription, isActive), pageable);
    }

    private GiftCertificate getGiftCertificateByUserRole(final int id) {
        return authorityValidator.isAdmin() ? checkExist(id) : checkExistActive(id);
    }

    private GiftCertificate checkExist(final int id) {
        return giftCertificateRepository
                .findById(id)
                .orElseThrow(() -> new ServiceException(
                        rb.getString("giftCertificate.notFound.id"),
                        HttpStatus.NOT_FOUND, properties.getGift(), id));
    }

    private void checkExistByName(final String name) {
        giftCertificateRepository
                .findByName(name)
                .ifPresent(giftCertificate -> {
                    throw new ServiceException(
                            rb.getString("giftCertificate.alreadyExists.name"),
                            HttpStatus.CONFLICT, properties.getGift(), name);
                });
    }

    private GiftCertificate prepareGiftCertificateForCreate(GiftCertificate rawGiftCertificate) {
        final Optional<GiftCertificate> optionalGiftCertificate
                = giftCertificateRepository.findByName(rawGiftCertificate.getName());

        if (optionalGiftCertificate.isPresent()) {
            if (optionalGiftCertificate.get().getActive()) {
                throw new ServiceException(
                        rb.getString("giftCertificate.alreadyExists.name"),
                        HttpStatus.CONFLICT, properties.getGift(), optionalGiftCertificate.get().getName());
            }
            giftCertificateMapper.updateGiftCertificateFromRaw(rawGiftCertificate, optionalGiftCertificate.get());
            rawGiftCertificate = optionalGiftCertificate.get();
        }

        return rawGiftCertificate;
    }
}

