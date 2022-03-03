package be.nazarimilad.springbootgraphqlquerydsldemo.dto;

import java.time.ZonedDateTime;
import java.util.List;

import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
public class DeliveryOrderDto {
  private Long id;
  private String address;
  private String customerName;
  private List<DeliveryOrderLineDto> deliveryOrderLines;
  private ZonedDateTime expectedDeliveryDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public List<DeliveryOrderLineDto> getDeliveryOrderLines() {
    return deliveryOrderLines;
  }

  public void setDeliveryOrderLines(List<DeliveryOrderLineDto> deliveryOrderLines) {
    this.deliveryOrderLines = deliveryOrderLines;
  }

  public ZonedDateTime getExpectedDeliveryDate() {
    return expectedDeliveryDate;
  }

  public void setExpectedDeliveryDate(ZonedDateTime expectedDeliveryDateTime) {
    this.expectedDeliveryDate = expectedDeliveryDateTime;
  }
}
