package be.nazarimilad.springbootgraphqlquerydsldemo.entity;

import javax.persistence.*;

@Entity
@Table(name = "DeliveryOrderLine")
public class DeliveryOrderLine {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false, unique = true)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "deliveryOrderId", nullable = false)
  // a delivery order line always belong to one specific delivery order
  private DeliveryOrder deliveryOrder;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "itemId", nullable = false)
  // a delivery order line always has one specific item
  private Item item;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  public Long getId() {
    return id;
  }

  public DeliveryOrder getDeliveryOrder() {
    return deliveryOrder;
  }

  public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
    this.deliveryOrder = deliveryOrder;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }
}
