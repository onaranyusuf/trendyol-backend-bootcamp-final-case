package com.trendyol.shoppingcart.mapper;

import com.trendyol.shoppingcart.models.dto.ItemDTO;
import com.trendyol.shoppingcart.models.entity.Item;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = VasItemMapper.class)
public interface ItemMapper {

    ItemDTO itemToItemDTO(Item defaultItem);

    Item itemDTOToItem(ItemDTO itemDTO);

    List<ItemDTO> itemsToItemDTOs(List<Item> defaultItems);
}

