package com.trendyol.shoppingcart.mapper;

import com.trendyol.shoppingcart.models.dto.VasItemDTO;
import com.trendyol.shoppingcart.models.entity.VasItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface VasItemMapper {

    @Mapping(target = "itemId", source = "defaultItem.itemId")
    @Mapping(target = "vasItemId", source = "itemId")
    @Mapping(target = "quantity", source = "quantity")
    VasItemDTO vasItemToItemDTO(VasItem vasItem);

    @Mapping(target = "defaultItem.itemId", source = "itemId")
    @Mapping(target = "itemId", source = "vasItemId")
    @Mapping(target = "quantity", source = "quantity")
    VasItem vasItemDTOToVasItem(VasItemDTO vasItemDTO);

    List<VasItemDTO> vasItemsToVasItemDTOs(List<VasItem> vasItems);
}

