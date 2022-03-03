package be.nazarimilad.springbootgraphqlquerydsldemo.mapper;

import java.util.List;

import be.nazarimilad.springbootgraphqlquerydsldemo.dto.DeliveryOrderDto;
import be.nazarimilad.springbootgraphqlquerydsldemo.dto.DeliveryOrderLineDto;
import be.nazarimilad.springbootgraphqlquerydsldemo.entity.DeliveryOrder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class DeliveryOrderMapper {
  private final DeliveryOrderLineMapper deliveryOrderLineMapper;

  public DeliveryOrderMapper(@Lazy DeliveryOrderLineMapper deliveryOrderLineMapper) {
    this.deliveryOrderLineMapper = deliveryOrderLineMapper;
  }

  /**
   * Map a delivery order entity object into a delivery order DTO
   */
  public DeliveryOrderDto toDeliveryOrderDto(DeliveryOrder deliveryOrder) {
    DeliveryOrderDto deliveryOrderDto = new DeliveryOrderDto();

    deliveryOrderDto.setId(deliveryOrder.getId());
    deliveryOrderDto.setAddress(deliveryOrder.getAddress());
    deliveryOrderDto.setCustomerName(deliveryOrder.getCustomerName());

    if (deliveryOrder.getDeliveryOrderLineList() != null) {
      List<DeliveryOrderLineDto> deliveryOrderLineDtoList = deliveryOrder.getDeliveryOrderLineList()
                                                                         .stream()
                                                                         .map(orderLine -> deliveryOrderLineMapper.toDeliveryOrderLineDto(orderLine))
                                                                         .toList();
      deliveryOrderDto.setDeliveryOrderLines(deliveryOrderLineDtoList);
    }

    deliveryOrderDto.setExpectedDeliveryDate(deliveryOrder.getExpectedDeliveryDateTime());

    return deliveryOrderDto;
  }
}
