import com.oliq04.medicalclinic.model.Clinic;
import com.oliq04.medicalclinic.model.DomainPage;
import com.oliq04.medicalclinic.model.DomainPageRequest;
import com.oliq04.medicalclinic.model.Pageable;
import com.oliq04.medicalclinic.service.ClinicRepositoryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClinicRepositoryProviderImpl implements ClinicRepositoryProvider {

    private final ClinicRepository clinicRepository;
    private final ClinicMapper clinicMapper;

    @Override
    public Pageable<Clinic> findAll(DomainPageRequest pageRequest) {
        PageRequest pageRequestToJpa = PageRequest.of(pageRequest.getPage(), pageRequest.getSize());
        Page<ClinicEntity> result = clinicRepository.findAll(pageRequestToJpa);
        List<Clinic> clinicList = result.stream()
                .map(clinicMapper::toEntityFromPojo)
                .toList();
        DomainPage domainPage = new DomainPage(result.getSize(),result.getNumber(), result.getNumberOfElements(), result.getTotalPages());
        return Pageable.toPageable(clinicList, domainPage);
    }

    @Override
    public Optional<Clinic> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Clinic save(Clinic clinic) {
        return null;
    }

    @Override
    public void delete(Clinic byName) {

    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }
}
