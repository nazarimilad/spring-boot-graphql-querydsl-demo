package be.nazarimilad.springbootgraphqlquerydsldemo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

import be.nazarimilad.springbootgraphqlquerydsldemo.dto.QDeliveryOrderDto;
import be.nazarimilad.springbootgraphqlquerydsldemo.entity.DeliveryOrder;
import be.nazarimilad.springbootgraphqlquerydsldemo.entity.DeliveryOrderLine;
import be.nazarimilad.springbootgraphqlquerydsldemo.entity.QDeliveryOrder;
import be.nazarimilad.springbootgraphqlquerydsldemo.entity.QDeliveryOrderLine;
import be.nazarimilad.springbootgraphqlquerydsldemo.repository.util.QueryBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.DslExpression;
import graphql.schema.DataFetchingFieldSelectionSet;
import graphql.schema.SelectedField;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryOrderRepository extends CustomJpaRepository<DeliveryOrder> {
  private final static QDeliveryOrder Q_DELIVERY_ORDER = QDeliveryOrder.deliveryOrder;
  private final DeliveryOrderLineRepository deliveryOrderLineRepository;

  public DeliveryOrderRepository(EntityManager entityManager, @Lazy DeliveryOrderLineRepository deliveryOrderLineRepository) {
    super(DeliveryOrder.class, entityManager);
    this.deliveryOrderLineRepository = deliveryOrderLineRepository;
  }

  @Nullable
  public DeliveryOrder findDeliveryOrder(DataFetchingFieldSelectionSet selectionSet, long id) {
    // start a new query
    QueryBuilder<DeliveryOrder> queryBuilder = super.createQueryBuilder(selectionSet, Q_DELIVERY_ORDER);

    // when we query one delivery order, it's possible that other entities related to the delivery order will be queried too in the query as nested fields.
    // But the queried properties of the delivery order will always be the immediate fields
    List<SelectedField> immediateFields = queryBuilder.getSelectionSet().getImmediateFields();
    QBean<DeliveryOrder> selection = buildSelection(immediateFields, queryBuilder, "");

    // build the query, fetch the data through hibernate and map/project the database results to entities
    Map<Long, DeliveryOrder> idToDeliveryOrderMap = queryBuilder.build()
                                                                .where(Q_DELIVERY_ORDER.id.eq(id))
                                                                .transform(
                                                                 GroupBy.groupBy(Q_DELIVERY_ORDER.id)
                                                                        .as(selection)
                                                                );
    List<DeliveryOrder> deliveryOrderListSingle = idToDeliveryOrderMap.values().stream().toList();
    return deliveryOrderListSingle.isEmpty() ? null : deliveryOrderListSingle.get(0);
  }

  public List<DeliveryOrder> findDeliveryOrderList(DataFetchingFieldSelectionSet selectionSet) {
    // start a new query
    QueryBuilder<DeliveryOrder> queryBuilder = super.createQueryBuilder(selectionSet, Q_DELIVERY_ORDER);

    // when we query a list of delivery orders, it's possible that other entities related to the delivery orders will be queried too in the query as nested fields.
    // But the queried properties of the delivery orders will always be the immediate fields
    List<SelectedField> immediateFields = queryBuilder.getSelectionSet().getImmediateFields();
    QBean<DeliveryOrder> selection = buildSelection(immediateFields, queryBuilder, "");

    // build the query, fetch the data through hibernate and map/project the database results to entities
    Map<Long, DeliveryOrder> idToDeliveryOrderMap = queryBuilder.build()
                                                                .transform(
                                                                 GroupBy.groupBy(Q_DELIVERY_ORDER.id)
                                                                        .as(selection)
                                                                );
    return new ArrayList<>(idToDeliveryOrderMap.values());
  }

  @Override
  protected QBean<DeliveryOrder> buildSelection(List<SelectedField> selectionFields, QueryBuilder<?> queryBuilder, String fieldReferencePattern) {
    // get entity paths from our GraphQL field selection
    List<Expression<?>> pathList =  selectionFields.stream()
                                                   .map(selectionField -> mapDtoFieldNameToEntityPath(selectionField.getName()))
                                                   .collect(Collectors.toList());

    // if delivery order line list field is in the selection, add the necessary delivery order line list selection data (join, selection, etc)
    int indexDeliveryOrderLineList = pathList.indexOf(Q_DELIVERY_ORDER.deliveryOrderLineList);
    if (indexDeliveryOrderLineList >= 0) {
      // join delivery order line table
      queryBuilder.innerJoin(Q_DELIVERY_ORDER.deliveryOrderLineList, QDeliveryOrderLine.deliveryOrderLine);

      // fetch the delivery order line fields residing inside our delivery order fields and
      // create a delivery order line selection with them
      String deliveryOrderLinesString = QDeliveryOrderDto.deliveryOrderDto.deliveryOrderLines.getMetadata().getName();
      String deliveryOrderLinesReferencePattern = fieldReferencePattern + deliveryOrderLinesString + "/*";
      List<SelectedField> deliveryOrderLineFields = queryBuilder.getSelectionSet().getFields(deliveryOrderLinesReferencePattern);
      Expression<DeliveryOrderLine> selection = deliveryOrderLineRepository.buildSelection(deliveryOrderLineFields, queryBuilder, deliveryOrderLinesReferencePattern);

      // a delivery order can have multiple delivery order lines, so a delivery order has a list of delivery order lines.
      // That's why we need an aggregating list expression (for the list of delivery order lines)
      DslExpression<List<DeliveryOrderLine>> deliveryOrderLinesSelection = GroupBy.list(selection)
                                                                                  .as(Q_DELIVERY_ORDER.deliveryOrderLineList);
      // update the basic empty pathList deliveryOrderLineList path with the extended selection we just have build
      pathList.set(indexDeliveryOrderLineList, deliveryOrderLinesSelection);
    }

    // now that we have all the necessary paths for our delivery order, a projection with all these paths can be created and returned
    Expression<?>[] selectedExpressions = super.mapExpressionListToExpressions(pathList);
    return Projections.fields(Q_DELIVERY_ORDER, selectedExpressions);
  }

  @Override
  protected Expression<?> mapDtoFieldNameToEntityPath(String dtoFieldName) {
    if (dtoFieldName.equals(QDeliveryOrderDto.deliveryOrderDto.id.getMetadata().getName())) {
      return Q_DELIVERY_ORDER.id;
    }
    else if (dtoFieldName.equals(QDeliveryOrderDto.deliveryOrderDto.address.getMetadata().getName())) {
      return Q_DELIVERY_ORDER.address;
    }
    else if (dtoFieldName.equals(QDeliveryOrderDto.deliveryOrderDto.customerName.getMetadata().getName())) {
      return Q_DELIVERY_ORDER.customerName;
    }
    else if (dtoFieldName.equals(QDeliveryOrderDto.deliveryOrderDto.deliveryOrderLines.getMetadata().getName())) {
      return Q_DELIVERY_ORDER.deliveryOrderLineList;
    }
    else if (dtoFieldName.equals(QDeliveryOrderDto.deliveryOrderDto.expectedDeliveryDate.getMetadata().getName())) {
      return Q_DELIVERY_ORDER.expectedDeliveryDateTime;
    }

    return null;
  }
}
