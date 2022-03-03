package be.nazarimilad.springbootgraphqlquerydsldemo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

import be.nazarimilad.springbootgraphqlquerydsldemo.dto.QItemDto;
import be.nazarimilad.springbootgraphqlquerydsldemo.entity.Item;
import be.nazarimilad.springbootgraphqlquerydsldemo.entity.QItem;
import be.nazarimilad.springbootgraphqlquerydsldemo.repository.util.QueryBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import graphql.schema.DataFetchingFieldSelectionSet;
import graphql.schema.SelectedField;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepository extends CustomJpaRepository<Item> {
  private final static QItem Q_ITEM = QItem.item;

  public ItemRepository(EntityManager entityManager) {
    super(Item.class, entityManager);
  }

  public Item createItem(String name, Optional<String> description) {
    Item item = new Item();

    item.setName(name);
    description.ifPresent(item::setDescription);

    return saveAndFlush(item);
  }

  public List<Item> findItemList(DataFetchingFieldSelectionSet selectionSet) {
    // start a new query
    QueryBuilder<Item> queryBuilder = super.createQueryBuilder(selectionSet, Q_ITEM);

    // when we query a list of items, it's possible that other entities related to the items will be queried too in the query as nested fields.
    // But the queried properties of the items will always be the immediate fields
    List<SelectedField> immediateFields = queryBuilder.getSelectionSet().getImmediateFields();
    QBean<Item> selection = buildSelection(immediateFields, queryBuilder, "");

    // build the query, fetch the data through hibernate and map/project the database results to entities
    Map<Long, Item> idToItemMap = queryBuilder.build()
                                              .transform(
                                               GroupBy.groupBy(Q_ITEM.id)
                                                      .as(selection)
                                              );
    return new ArrayList<>(idToItemMap.values());
  }

  @Override
  protected QBean<Item> buildSelection(List<SelectedField> selectionFields, QueryBuilder<?> queryBuilder, String fieldReferencePattern) {
    // get entity paths from our GraphQL field selection
    List<Expression<?>> pathList =  selectionFields.stream()
                                                   .map(selectionField -> mapDtoFieldNameToEntityPath(selectionField.getName()))
                                                   .collect(Collectors.toList());

    // now that we have all the necessary paths for our item, a projection with all these paths can be created and returned
    Expression<?>[] selectedExpressions = super.mapExpressionListToExpressions(pathList);
    return Projections.fields(Q_ITEM, selectedExpressions);
  }

  @Override
  protected Expression<?> mapDtoFieldNameToEntityPath(String dtoFieldName) {
    if (dtoFieldName.equals(QItemDto.itemDto.id.getMetadata().getName())) {
      return QItem.item.id;
    }
    else if (dtoFieldName.equals(QItemDto.itemDto.name.getMetadata().getName())) {
      return QItem.item.name;
    }
    else if (dtoFieldName.equals(QItemDto.itemDto.description.getMetadata().getName())) {
      return QItem.item.description;
    }

    return null;
  }
}
