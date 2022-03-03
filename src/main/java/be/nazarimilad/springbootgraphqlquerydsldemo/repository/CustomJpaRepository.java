package be.nazarimilad.springbootgraphqlquerydsldemo.repository;

import java.util.List;
import javax.persistence.EntityManager;

import be.nazarimilad.springbootgraphqlquerydsldemo.repository.util.QueryBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.QBean;
import graphql.schema.DataFetchingFieldSelectionSet;
import graphql.schema.SelectedField;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;

/**
 * Custom JPA repository that contains extra utility functions. It meant to be extended from,
 * by all repository classes inside this repository package
 */
@NoRepositoryBean
public abstract class CustomJpaRepository<T> extends SimpleJpaRepository<T, Long> {
  private final EntityManager entityManager;

  public CustomJpaRepository(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
    this.entityManager = entityManager;
  }

  /**
   * Provide a selection for class T, that contains the necessary data corresponding to the GraphQL selection,
   * so that a queryBuilder can be further extended by this selection
   */
  protected abstract QBean<T> buildSelection(List<SelectedField> deliveryOrderSelectionFields, QueryBuilder<?> queryBuilder, String fieldReferencePattern);

  protected EntityManager getEntityManager() {
    return entityManager;
  }

  protected QueryBuilder<T> createQueryBuilder(DataFetchingFieldSelectionSet selectionSet, EntityPath<T> entityPathFrom) {
    return new QueryBuilder<>(getEntityManager(), selectionSet, entityPathFrom);
  }

  /**
   * Map a specific dtoFieldName to its corresponding entity field name. For example this function
   * is necessary to map DTO field DeliveryOrderDTO.expectedDeliveryDate into entity field DeliveryOrder.expectedDeliveryDateTime
   */
  @Nullable
  protected abstract Expression<?> mapDtoFieldNameToEntityPath(String dtoFieldName);

  /**
   * Transform a set of expressions to an array of expressions,
   * used because QueryDsl's projection function only accept argument expressions as varargs, not as list
   */
  public Expression<?>[] mapExpressionListToExpressions(List<Expression<?>> expressionSet) {
    return expressionSet.toArray(new Expression[0]);
  }
}
