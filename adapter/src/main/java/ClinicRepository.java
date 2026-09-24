import com.oliq04.medicalclinic.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicRepository extends JpaRepository<ClinicEntity, Long> {
    Optional<ClinicEntity> findByName(String name);

    boolean existsByName(String name);
}
