package ke.co.marvelsoft.masharubu.service.mapper;

import ke.co.marvelsoft.masharubu.domain.*;
import ke.co.marvelsoft.masharubu.service.dto.TourOperatorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TourOperator} and its DTO {@link TourOperatorDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TourOperatorMapper extends EntityMapper<TourOperatorDTO, TourOperator> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "validatedBy.id", target = "validatedById")
    @Mapping(source = "validatedBy.login", target = "validatedByLogin")
    TourOperatorDTO toDto(TourOperator tourOperator);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "validatedById", target = "validatedBy")
    TourOperator toEntity(TourOperatorDTO tourOperatorDTO);

    default TourOperator fromId(Long id) {
        if (id == null) {
            return null;
        }
        TourOperator tourOperator = new TourOperator();
        tourOperator.setId(id);
        return tourOperator;
    }
}
