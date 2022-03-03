package be.nazarimilad.springbootgraphqlquerydsldemo.repository;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

import be.nazarimilad.springbootgraphqlquerydsldemo.dto.QDeliveryOrderLineDto;
import be.nazarimilad.springbootgraphqlquerydsldemo.entity.*;
import be.nazarimilad.springbootgraphqlquerydsldemo.repository.util.QueryBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import graphql.schema.SelectedField;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryOrderLineRepository extends CustomJpaRepository<DeliveryOrderLine> {
  private final static QDeliveryOrderLine Q_DELIVERY_ORDER_LINE = QDeliveryOrderLine.deliveryOrderLine;
  private final DeliveryOrderRepository deliveryOrderRepository;
  private final ItemRepository itemRepository;

  public DeliveryOrderLineRepository(EntityManager entityManager, @Lazy DeliveryOrderRepository deliveryOrderRepository, @Lazy ItemRepository itemRepository) {
    super(DeliveryOrderLine.class, entityManager);
    this.deliveryOrderRepository = deliveryOrderRepository;
    this.itemRepository = itemRepository;
  }

  @Override
  protected QBean<DeliveryOrderLine> buildSelection(List<SelectedField> selectionFields, QueryBuilder<?> queryBuilder, String fieldReferencePattern) {
    // get entity paths from our GraphQL field selection
    List<Expression<?>> pathList =  selectionFields.stream()
                                                   .map(selectionField -> mapDtoFieldNameToEntityPath(selectionField.getName()))
                                                   .collect(Collectors.toList());

    // if delivery order field is in the selection, add the necessary delivery order selection data (join, selection, etc)
    int indexDeliveryOrder = pathList.indexOf(Q_DELIVERY_ORDER_LINE.deliveryOrder);
    if (indexDeliveryOrder >= 0) {
      // join delivery order table
      queryBuilder.innerJoin(Q_DELIVERY_ORDER_LINE.deliveryOrder, QDeliveryOrder.deliveryOrder);

      // fetch the delivery order fields residing inside our delivery order line fields and
      // create a delivery order selection with them
      String deliveryOrderString = QDeliveryOrderLineDto.deliveryOrderLineDto.deliveryOrder.getMetadata().getName();
      String deliveryOrderLineFieldReferencePattern = fieldReferencePattern + deliveryOrderString + "/*";
      List<SelectedField> deliveryOrderFields = queryBuilder.getSelectionSet().getFields(deliveryOrderLineFieldReferencePattern);
      Expression<DeliveryOrder> selection = deliveryOrderRepository.buildSelection(deliveryOrderFields, queryBuilder, deliveryOrderLineFieldReferencePattern)
                                                                   .as(Q_DELIVERY_ORDER_LINE.deliveryOrder);
      // update the basic empty pathList deliveryOrder path with the extended selection we just have build
      pathList.set(indexDeliveryOrder, selection);
    }

    // if item field is in the selection, add the necessary item selection data (join, selection, etc)
    int indexItem = pathList.indexOf(Q_DELIVERY_ORDER_LINE.item);
    if (indexItem >= 0) {
      // join item table
      queryBuilder.innerJoin(Q_DELIVERY_ORDER_LINE.item, QItem.item);

      // fetch the item fields residing inside our delivery order line fields and
      // create an item selection with them
      String itemString = QDeliveryOrderLineDto.deliveryOrderLineDto.item.getMetadata().getName();
      String itemFieldReferencePattern = fieldReferencePattern + itemString + "/*";
      List<SelectedField> itemFields = queryBuilder.getSelectionSet().getFields(itemFieldReferencePattern);
      Expression<Item> selection = itemRepository.buildSelection(itemFields, queryBuilder, itemFieldReferencePattern)
                                                 .as(Q_DELIVERY_ORDER_LINE.item);
      // update the basic empty pathList item path with the extended selection we just have build
      pathList.set(indexItem, selection);
    }

    // now that we have all the necessary paths for our delivery order line, a projection with all these paths can be created and returned
    Expression<?>[] selectedExpressions = super.mapExpressionListToExpressions(pathList);
    return Projections.fields(Q_DELIVERY_ORDER_LINE, selectedExpressions);
  }

  @Override
  protected Expression<?> mapDtoFieldNameToEntityPath(String dtoFieldName) {
    if (dtoFieldName.equals(QDeliveryOrderLineDto.deliveryOrderLineDto.id.getMetadata().getName())) {
      return Q_DELIVERY_ORDER_LINE.id;
    }
    else if (dtoFieldName.equals(QDeliveryOrderLineDto.deliveryOrderLineDto.deliveryOrder.getMetadata().getName())) {
      return Q_DELIVERY_ORDER_LINE.deliveryOrder;
    }
    else if (dtoFieldName.equals(QDeliveryOrderLineDto.deliveryOrderLineDto.item.getMetadata().getName())) {
      return Q_DELIVERY_ORDER_LINE.item;
    }
    else if (dtoFieldName.equals(QDeliveryOrderLineDto.deliveryOrderLineDto.quantity.getMetadata().getName())) {
      return Q_DELIVERY_ORDER_LINE.quantity;
    }

    return null;
  }
}
