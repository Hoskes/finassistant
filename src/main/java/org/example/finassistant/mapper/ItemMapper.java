package org.example.finassistant.mapper;

import org.example.finassistant.dto.ItemDTO;
import org.example.finassistant.model.Item;
import org.mapstruct.factory.Mappers;

public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);
    ItemDTO itemToDTO(Item item);
}
