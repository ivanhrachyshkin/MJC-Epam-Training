package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dao.GiftCertificateSpecification;
import com.epam.esm.dao.TagRepository;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.config.ExceptionStatusPostfixProperties;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateRequestParamsContainer;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.validator.AuthorityValidator;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.PageValidator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    @Setter
    private ResourceBundle rb;
    private final ExceptionStatusPostfixProperties properties;
    private final DtoMapper<GiftCertificate, GiftCertificateDto> mapper;
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final GiftCertificateValidator giftCertificateValidator;
    private final PageValidator paginationValidator;
    private final AuthorityValidator authorityValidator;
    private final GiftCertificateSpecification specification;

    @Override
    @Transactional
    public GiftCertificateDto create(final GiftCertificateDto giftCertificateDto) {
        giftCertificateValidator.isValidateCreateElseUpdate(giftCertificateDto, true);
        final GiftCertificate rawGiftCertificate = mapper.dtoToModel(giftCertificateDto);
        rawGiftCertificate.setActive(true);
        setTagId(rawGiftCertificate.getTags());
        final GiftCertificate newGiftCertificate = createOrUpdateOld(rawGiftCertificate);
        return mapper.modelToDto(newGiftCertificate);
    }

    @Override
    @Transactional
    public GiftCertificateDto update(final GiftCertificateDto giftCertificateDto) {
        giftCertificateValidator.isValidateCreateElseUpdate(giftCertificateDto, false);
        checkExist(giftCertificateDto.getId());
        checkExistByName(giftCertificateDto.getName());// todo active
        final GiftCertificate rawGiftCertificate = mapper.dtoToModel(giftCertificateDto);
        setTagId(rawGiftCertificate.getTags());
        final GiftCertificate updatedGiftCertificate = giftCertificateRepository.save(rawGiftCertificate);
        return mapper.modelToDto(updatedGiftCertificate);
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
        return mapper.modelsToDto(giftCertificates);
    }

    @Override
    @Transactional
    public GiftCertificateDto readOne(final int id) {
        giftCertificateValidator.validateId(id);
        final GiftCertificate giftCertificate = getGiftCertificateByUserRole(id);
        return mapper.modelToDto(giftCertificate);
    }

    @Override
    @Transactional
    public void deleteById(final int id) {
        giftCertificateValidator.validateId(id);
        final GiftCertificate giftCertificate = checkExistActive(id);
        giftCertificate.setActive(false);
        giftCertificateRepository.save(giftCertificate);
    }

    private void setTagId(final Set<Tag> tags) {
        tags.forEach(tag -> {
            tag.setActive(true);
            final Optional<Tag> oldTag = tagRepository.findByName(tag.getName());
            if (oldTag.isPresent()) {
                tag.setId(oldTag.get().getId());
            } else {
                tagRepository.save(tag);
            }
        });
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

    private GiftCertificate checkExistActive(final int id) {
        return giftCertificateRepository
                .findByIdAndActive(id, true)
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

    private GiftCertificate createOrUpdateOld(final GiftCertificate rawGiftCertificate) {
        final Optional<GiftCertificate> optionalGiftCertificate
                = giftCertificateRepository.findByName(rawGiftCertificate.getName());
        if (optionalGiftCertificate.isPresent() && optionalGiftCertificate.get().getActive()) {
            throw new ServiceException(
                    rb.getString("giftCertificate.alreadyExists.name"),
                    HttpStatus.CONFLICT, properties.getGift(), optionalGiftCertificate.get().getName());
        }
        optionalGiftCertificate.ifPresent(oldGiftCertificate -> rawGiftCertificate.setId(oldGiftCertificate.getId()));
        return giftCertificateRepository.save(rawGiftCertificate);
    }
}

