package be.nazarimilad.springbootgraphqlquerydsldemo.controller;

import java.util.List;
import java.util.Optional;

import be.nazarimilad.springbootgraphqlquerydsldemo.dto.ItemDto;
import be.nazarimilad.springbootgraphqlquerydsldemo.entity.Item;
import be.nazarimilad.springbootgraphqlquerydsldemo.mapper.ItemMapper;
import be.nazarimilad.springbootgraphqlquerydsldemo.repository.ItemRepository;
import graphql.GraphqlErrorException;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

@Controller
public class ItemController {
  private final ItemRepository itemRepository;
  private final ItemMapper itemMapper;

  public ItemController(ItemRepository itemRepository, ItemMapper itemMapper) {
    this.itemRepository = itemRepository;
    this.itemMapper = itemMapper;
  }

  /**
   * Fetch all available items
   */
  @QueryMapping
  public List<ItemDto> items(DataFetchingEnvironment dataFetchingEnvironment) {
    // get the selected fields
    DataFetchingFieldSelectionSet selectionSet = dataFetchingEnvironment.getSelectionSet();

    // fetch the necessary data based on the selected fields
    List<Item> itemList = itemRepository.findItemList(selectionSet);

    // map the result entities to dto's and return them
    return itemList.stream()
                   .map(item -> itemMapper.toItemDto(item))
                   .toList();
  }

  @MutationMapping
  public ItemDto createItem(@Argument String name, @Argument Optional<String> description) throws GraphqlErrorException {
    // validation
    if (name.isBlank()) {
      throw GraphqlErrorException.newErrorException()
                                 .errorClassification(ErrorType.BAD_REQUEST)
                                 .message("The required name for a new item cannot be blank")
                                 .build();
    }

    // create a new item
    Item item = itemRepository.createItem(name, description);

    // map the new item entity to a dto and return it
    return itemMapper.toItemDto(item);
  }
}
