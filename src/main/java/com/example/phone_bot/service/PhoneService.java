package com.example.phone_bot.service;

import com.example.phone_bot.model.dto.PhoneDto;
import com.example.phone_bot.model.entity.PhoneEntity;
import com.example.phone_bot.repository.PhoneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Service
public class PhoneService {
    @Autowired private PhoneRepository phoneRepository;

    public List<PhoneDto> getPhonesByBrand(String brand) {
        List<PhoneEntity> byBrand = phoneRepository.findByBrand(brand);
        return byBrand.stream().map(PhoneDto::toDto).toList();
    }

    public List<PhoneDto> getPhonesByModel(String model) {
        List<PhoneEntity> byModel = phoneRepository.findByModel(model);
        return byModel.stream().map(PhoneDto::toDto).toList();
    }

    @Transactional
    public PhoneDto addPhone(PhoneDto phoneDto, String imageUrl) throws IOException {
        // URL에서 이미지를 다운로드하여 저장할 필요가 없다면, 아래 코드는 생략할 수 있습니다.
        // 만약 필요하다면, URL에서 이미지를 다운로드하고 저장하는 로직을 추가해야 합니다.

        // PhoneEntity를 생성하고 이미지 URL 설정
        PhoneEntity entity = PhoneEntity.of(
                phoneDto.brand(),
                phoneDto.model(),
                phoneDto.price(),
                phoneDto.imagePath(),
                phoneDto.condition());
        entity.setImage(imageUrl); // 이미지 URL 설정 추가
        PhoneEntity savedEntity = phoneRepository.save(entity);
        return PhoneDto.toDto(savedEntity);
    }




    @Transactional
    public PhoneDto updatePhone(Long id, PhoneDto phoneDto, String imageFile) throws IOException {

        PhoneEntity entity = phoneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Phone not found"));
        entity.setBrand(phoneDto.brand());
        entity.setModel(phoneDto.model());
        entity.setPrice(phoneDto.price());
        entity.setImage(imageFile);
        entity.setCondition(phoneDto.condition());
        phoneRepository.save(entity);
        return PhoneDto.toDto(entity);
    }

    @Transactional
    public void deletePhone(Long id) {
        PhoneEntity phoneEntity = phoneRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Phone not found"));
        phoneRepository.delete(phoneEntity);
    }

    // PhoneService.java
    public Optional<PhoneEntity> getLatestPhone() {
        return phoneRepository.findTopByOrderByIdDesc();  // ID 기준으로 최신 휴대폰 조회
    }

    public Optional<PhoneDto> getPhoneById(Long id) {
        PhoneEntity phoneEntity = phoneRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("phone is not found!"));
        return Optional.of(PhoneDto.toDto(phoneEntity));
    }
}
