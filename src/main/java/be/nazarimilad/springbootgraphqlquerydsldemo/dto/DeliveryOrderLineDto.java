package be.nazarimilad.springbootgraphqlquerydsldemo.dto;

import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
public class DeliveryOrderLineDto {
  private Long id;
  private DeliveryOrderDto deliveryOrder;
  private ItemDto item;
  private Integer quantity;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public DeliveryOrderDto getDeliveryOrder() {
    return deliveryOrder;
  }

  public void setDeliveryOrder(DeliveryOrderDto deliveryOrder) {
    this.deliveryOrder = deliveryOrder;
  }

  public ItemDto getItem() {
    return item;
  }

  public void setItem(ItemDto item) {
    this.item = item;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }
}
