package be.nazarimilad.springbootgraphqlquerydsldemo.mapper;

import be.nazarimilad.springbootgraphqlquerydsldemo.dto.DeliveryOrderDto;
import be.nazarimilad.springbootgraphqlquerydsldemo.dto.DeliveryOrderLineDto;
import be.nazarimilad.springbootgraphqlquerydsldemo.dto.ItemDto;
import be.nazarimilad.springbootgraphqlquerydsldemo.entity.DeliveryOrderLine;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class DeliveryOrderLineMapper {
  private final DeliveryOrderMapper deliveryOrderMapper;
  private final ItemMapper itemMapper;

  // Lazy annotation is necessary since there is a circular dependecy between DeliveryOrder and DeliveryOrderLine
  public DeliveryOrderLineMapper(@Lazy DeliveryOrderMapper deliveryOrderMapper, @Lazy ItemMapper itemMapper) {
    this.deliveryOrderMapper = deliveryOrderMapper;
    this.itemMapper = itemMapper;
  }

  /**
   * Map a delivery order line entity object into a delivery order line DTO
   */
  public DeliveryOrderLineDto toDeliveryOrderLineDto(DeliveryOrderLine deliveryOrderLine) {
    DeliveryOrderLineDto deliveryOrderLineDto = new DeliveryOrderLineDto();

    deliveryOrderLineDto.setId(deliveryOrderLine.getId());

    if (deliveryOrderLine.getDeliveryOrder() != null) {
      DeliveryOrderDto deliveryOrderDto = deliveryOrderMapper.toDeliveryOrderDto(deliveryOrderLine.getDeliveryOrder());
      deliveryOrderLineDto.setDeliveryOrder(deliveryOrderDto);
    }

    if (deliveryOrderLine.getItem() != null) {
      ItemDto itemDto = itemMapper.toItemDto(deliveryOrderLine.getItem());
      deliveryOrderLineDto.setItem(itemDto);
    }

    deliveryOrderLineDto.setQuantity(deliveryOrderLine.getQuantity());

    return deliveryOrderLineDto;
  }
}
