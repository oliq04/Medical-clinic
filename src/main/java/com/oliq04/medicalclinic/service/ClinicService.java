package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.exceptions.ClinicAlreadyExistsException;
import com.oliq04.medicalclinic.exceptions.ClinicNotFoundException;
import com.oliq04.medicalclinic.mapper.ClinicMapper;
import com.oliq04.medicalclinic.model.clinic.Clinic;
import com.oliq04.medicalclinic.model.clinic.ClinicCommand;
import com.oliq04.medicalclinic.model.clinic.ClinicDto;
import com.oliq04.medicalclinic.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicService {

    private final ClinicRepository clinicRepository;
    private final ClinicMapper clinicMapper;

    public List<ClinicDto> getClinics() {
        return clinicRepository.findAll().stream()
                .map(clinicMapper::toDtoFromEntity)
                .toList();
    }

    public ClinicDto getClinic(String name) {
        return clinicMapper.toDtoFromEntity(findByName(name));
    }

    public Clinic findByName(String name) {
        return clinicRepository.findByName(name)
                .orElseThrow(() -> new ClinicNotFoundException("Clinic with given name not found", HttpStatus.NOT_FOUND));
    }

    public ClinicDto addClinic(ClinicCommand clinicCommand) {
        Clinic clinic = clinicMapper.toEntityFromCommand(clinicCommand);
        return clinicMapper.toDtoFromEntity(clinicRepository.save(clinic));
    }

    public void deleteClinic(String name) {
        clinicRepository.delete(findByName(name));
    }

    public ClinicDto editClinic(String name, ClinicCommand clinicCommand) {
        if (clinicRepository.existsByName(clinicCommand.getName()) && !clinicCommand.getName().equals(name)) {
            throw new ClinicAlreadyExistsException("Clinic with given name already exists", HttpStatus.CONFLICT);
        }
        Clinic clinic = findByName(name);
        clinic.update(clinicCommand);
        clinicRepository.save(clinic);
        return clinicMapper.toDtoFromEntity(clinic);
    }
}
