package be.nazarimilad.springbootgraphqlquerydsldemo.entity;

import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "Item")
public class Item {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false, unique = true)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
  // an item can be used by several delivery order lines
  private List<DeliveryOrderLine> deliveryOrderLines;

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<DeliveryOrderLine> getDeliveryOrderLines() {
    return deliveryOrderLines;
  }
}
