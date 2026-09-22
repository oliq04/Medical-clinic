package com.oliq04.medicalclinic.service;

import com.oliq04.medicalclinic.model.Clinic;
import com.oliq04.medicalclinic.model.exceptions.ClinicAlreadyExistsException;
import com.oliq04.medicalclinic.model.exceptions.ClinicNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ClinicService {

    private final ClinicRepositoryProvider clinicRepository;

    public List<Clinic> getClinics(int pageNumber, int clinicsCount) {
        return clinicRepository.findAll();
    }

    public Clinic getClinic(String name) {
        return findByName(name);
    }

    public Clinic findByName(String name) {
        return clinicRepository.findByName(name)
                .orElseThrow(() -> new ClinicNotFoundException("Clinic with given name not found", 404));
    }

    public Clinic addClinic(Clinic clinicCommand) {
        log.info("Saving clinic to database.");
        return clinicRepository.save(clinicCommand);
    }

    public void deleteClinic(String name) {
        clinicRepository.delete(findByName(name));
    }

    public Clinic editClinic(String name, Clinic clinicCommand) {
        if (clinicRepository.existsByName(clinicCommand.getName()) && !clinicCommand.getName().equals(name)) {
            throw new ClinicAlreadyExistsException("Clinic with given name already exists", 409);
        }
        Clinic clinic = findByName(name);
        clinic.update(clinicCommand);
        clinicRepository.save(clinic);
        return clinic;
    }
}

