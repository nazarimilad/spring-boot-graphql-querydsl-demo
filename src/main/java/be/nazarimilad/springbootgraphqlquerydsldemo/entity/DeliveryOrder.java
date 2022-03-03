package be.nazarimilad.springbootgraphqlquerydsldemo.entity;

import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "DeliveryOrder")
public class DeliveryOrder {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false, unique = true)
  private Long id;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "customerName", nullable = false)
  private String customerName;

  @OneToMany(mappedBy = "deliveryOrder", fetch = FetchType.LAZY)
  // one delivery order always has one or more delivery order lines
  private List<DeliveryOrderLine> deliveryOrderLineList;

  @Column(name = "expectedDeliveryDateTime")
  private ZonedDateTime expectedDeliveryDateTime;

  public Long getId() {
    return id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public List<DeliveryOrderLine> getDeliveryOrderLineList() {
    return deliveryOrderLineList;
  }

  public void setDeliveryOrderLineList(List<DeliveryOrderLine> deliveryOrderLineList) {
    this.deliveryOrderLineList = deliveryOrderLineList;
  }

  public ZonedDateTime getExpectedDeliveryDateTime() {
    return expectedDeliveryDateTime;
  }

  public void setExpectedDeliveryDateTime(ZonedDateTime expectedDeliveryDateTime) {
    this.expectedDeliveryDateTime = expectedDeliveryDateTime;
  }
}
