import com.oliq04.medicalclinic.model.Clinic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClinicMapper {

    //@Mapping(target = "doctors", ignore = true)
    //@Mapping(target = "id", ignore = true)
    ClinicEntity toEntityFromPojo(Clinic clinic);
    Clinic toEntityFromPojo(ClinicEntity clinic);
    List<ClinicDto> clinicsDto(List<Clinic> clinics);
    //ClinicDto toDtoFromEntity(Clinic clinic);
}
