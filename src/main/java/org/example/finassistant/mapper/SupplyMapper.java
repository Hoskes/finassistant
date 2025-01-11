package org.example.finassistant.mapper;

import org.example.finassistant.dto.SupplyDTO;
import org.example.finassistant.dto.UserDTO;
import org.example.finassistant.model.Supply;
import org.example.finassistant.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SupplyMapper {
    SupplyMapper INSTANCE = Mappers.getMapper(SupplyMapper.class);
    SupplyDTO supplyToDTO(Supply supply);
}
