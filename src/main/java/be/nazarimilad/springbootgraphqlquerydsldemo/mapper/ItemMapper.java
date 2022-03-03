package be.nazarimilad.springbootgraphqlquerydsldemo.mapper;

import be.nazarimilad.springbootgraphqlquerydsldemo.dto.ItemDto;
import be.nazarimilad.springbootgraphqlquerydsldemo.entity.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

  /**
   * Map an item entity object into an item DTO
   */
  public ItemDto toItemDto(Item item) {
    ItemDto itemDto = new ItemDto();

    itemDto.setId(item.getId());
    itemDto.setName(item.getName());
    itemDto.setDescription(item.getDescription());

    return itemDto;
  }
}
