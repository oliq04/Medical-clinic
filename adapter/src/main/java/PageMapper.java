import com.oliq04.medicalclinic.model.Clinic;
import com.oliq04.medicalclinic.model.DomainPage;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PageMapper {

    Clinic toEntityFromPojo(ClinicEntity clinic);
}
