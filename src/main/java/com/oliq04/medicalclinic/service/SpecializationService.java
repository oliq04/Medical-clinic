package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.exceptions.SpecializationNotFoundException;
import com.oliq04.medicalclinic.model.specialization.Specialization;
import com.oliq04.medicalclinic.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecializationService {
    private final SpecializationRepository specializationRepository;

    public Specialization findSpecialization(String name) {
        return specializationRepository.findBySpecializationName(name)
                .orElseThrow(() -> new SpecializationNotFoundException("Specialization with given name not found", HttpStatus.NOT_FOUND));
    }
}
