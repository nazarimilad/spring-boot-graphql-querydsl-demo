package be.nazarimilad.springbootgraphqlquerydsldemo.controller;

import java.util.List;

import be.nazarimilad.springbootgraphqlquerydsldemo.dto.DeliveryOrderDto;
import be.nazarimilad.springbootgraphqlquerydsldemo.entity.DeliveryOrder;
import be.nazarimilad.springbootgraphqlquerydsldemo.mapper.DeliveryOrderMapper;
import be.nazarimilad.springbootgraphqlquerydsldemo.repository.DeliveryOrderRepository;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

@Controller
public class DeliveryOrderController {
  private final DeliveryOrderRepository deliveryOrderRepository;
  private final DeliveryOrderMapper deliveryOrderMapper;

  public DeliveryOrderController(DeliveryOrderRepository deliveryOrderRepository, DeliveryOrderMapper deliveryOrderMapper) {
    this.deliveryOrderRepository = deliveryOrderRepository;
    this.deliveryOrderMapper = deliveryOrderMapper;
  }

  /**
   * Fetch one specific delivery order with the provided id
   */
  @QueryMapping
  @Nullable
  public DeliveryOrderDto deliveryOrder(DataFetchingEnvironment dataFetchingEnvironment, @Argument long id) {
    // get the selected fields
    DataFetchingFieldSelectionSet selectionSet = dataFetchingEnvironment.getSelectionSet();

    // fetch the necessary data based on the selected fields
    DeliveryOrder deliveryOrder = deliveryOrderRepository.findDeliveryOrder(selectionSet, id);
    if (deliveryOrder == null) return null;

    // map the result entity to dto and return it
    return deliveryOrderMapper.toDeliveryOrderDto(deliveryOrder);
  }

  /**
   * Fetch all available delivery orders
   */
  @QueryMapping
  public List<DeliveryOrderDto> deliveryOrders(DataFetchingEnvironment dataFetchingEnvironment) {
    // get the selected fields
    DataFetchingFieldSelectionSet selectionSet = dataFetchingEnvironment.getSelectionSet();

    // fetch the necessary data based on the selected fields
    List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findDeliveryOrderList(selectionSet);

    // map the result entities to dto's and return them
    return deliveryOrderList.stream()
                            .map(deliveryOrder -> deliveryOrderMapper.toDeliveryOrderDto(deliveryOrder))
                            .toList();
  }
}
