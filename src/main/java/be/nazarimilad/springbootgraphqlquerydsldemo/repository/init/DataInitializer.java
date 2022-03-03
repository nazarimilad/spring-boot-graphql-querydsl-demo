package be.nazarimilad.springbootgraphqlquerydsldemo.repository.init;

import java.time.ZonedDateTime;

import be.nazarimilad.springbootgraphqlquerydsldemo.entity.DeliveryOrder;
import be.nazarimilad.springbootgraphqlquerydsldemo.entity.DeliveryOrderLine;
import be.nazarimilad.springbootgraphqlquerydsldemo.entity.Item;
import be.nazarimilad.springbootgraphqlquerydsldemo.repository.DeliveryOrderLineRepository;
import be.nazarimilad.springbootgraphqlquerydsldemo.repository.DeliveryOrderRepository;
import be.nazarimilad.springbootgraphqlquerydsldemo.repository.ItemRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * This class is reponsible to populate the in-memory database
 * so that we already can have some data to retrieve and test on.
 * Note that, when restarting the application, all data is reset;
 * so no changes are persisted anywhere.
 */
@Component
public class DataInitializer implements InitializingBean {
  private final ItemRepository itemRepository;
  private final DeliveryOrderLineRepository deliveryOrderLineRepository;
  private final DeliveryOrderRepository deliveryOrderRepository;

  public DataInitializer(ItemRepository itemRepository, DeliveryOrderLineRepository deliveryOrderLineRepository, DeliveryOrderRepository deliveryOrderRepository) {
    this.itemRepository = itemRepository;
    this.deliveryOrderLineRepository = deliveryOrderLineRepository;
    this.deliveryOrderRepository = deliveryOrderRepository;
  }

  /**
   * Populate the database after all the (Spring) bean properties have been set
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    // persist new items
    Item phoneChargerItem = new Item();
    phoneChargerItem.setName("Phone charger");
    phoneChargerItem.setDescription("Fast charging phone charger");

    Item backpackItem = new Item();
    backpackItem.setName("Backpack");
    backpackItem.setDescription("Vaude waterproof backpack");

    Item shoeItem = new Item();
    shoeItem.setName("Shoes");
    shoeItem.setDescription("Sneaker XL");

    Item tShirtItem = new Item();
    tShirtItem.setName("T-shirt");
    tShirtItem.setDescription("S green with dots");

    itemRepository.save(phoneChargerItem);
    itemRepository.save(backpackItem);
    itemRepository.save(shoeItem);
    itemRepository.save(tShirtItem);

    // persist new delivery orders
    DeliveryOrder firstDeliveryOrder = new DeliveryOrder();
    firstDeliveryOrder.setAddress("Brusselsesteenweg 32, 1000 Brussels ");
    firstDeliveryOrder.setExpectedDeliveryDateTime(ZonedDateTime.now().plusMonths(3));
    firstDeliveryOrder.setCustomerName("Bob");

    DeliveryOrder secondDeliveryOrder = new DeliveryOrder();
    secondDeliveryOrder.setAddress("New Orlean street 3, 34019 New York");
    secondDeliveryOrder.setExpectedDeliveryDateTime(ZonedDateTime.now().plusMonths(8));
    secondDeliveryOrder.setCustomerName("Lisa");

    deliveryOrderRepository.save(firstDeliveryOrder);
    deliveryOrderRepository.save(secondDeliveryOrder);

    // persist new delivery order lines using the newly created items and delivery orders
    DeliveryOrderLine twoPhoneChargersDeliveryOrderLine = new DeliveryOrderLine();
    twoPhoneChargersDeliveryOrderLine.setItem(phoneChargerItem);
    twoPhoneChargersDeliveryOrderLine.setQuantity(2);
    twoPhoneChargersDeliveryOrderLine.setDeliveryOrder(firstDeliveryOrder);

    DeliveryOrderLine oneBackpackDeliveryOrderLine = new DeliveryOrderLine();
    oneBackpackDeliveryOrderLine.setItem(backpackItem);
    oneBackpackDeliveryOrderLine.setQuantity(1);
    oneBackpackDeliveryOrderLine.setDeliveryOrder(firstDeliveryOrder);

    DeliveryOrderLine threeShoesOrderLine = new DeliveryOrderLine();
    threeShoesOrderLine.setItem(shoeItem);
    threeShoesOrderLine.setQuantity(3);
    threeShoesOrderLine.setDeliveryOrder(firstDeliveryOrder);

    DeliveryOrderLine twoBackpacksDeliveryOrderLine = new DeliveryOrderLine();
    twoBackpacksDeliveryOrderLine.setItem(backpackItem);
    twoBackpacksDeliveryOrderLine.setQuantity(2);
    twoBackpacksDeliveryOrderLine.setDeliveryOrder(secondDeliveryOrder);

    DeliveryOrderLine tenTshirtsDeliveryOrderLine = new DeliveryOrderLine();
    tenTshirtsDeliveryOrderLine.setItem(tShirtItem);
    tenTshirtsDeliveryOrderLine.setQuantity(10);
    tenTshirtsDeliveryOrderLine.setDeliveryOrder(secondDeliveryOrder);

    deliveryOrderLineRepository.save(twoPhoneChargersDeliveryOrderLine);
    deliveryOrderLineRepository.save(oneBackpackDeliveryOrderLine);
    deliveryOrderLineRepository.save(threeShoesOrderLine);
    deliveryOrderLineRepository.save(twoBackpacksDeliveryOrderLine);
    deliveryOrderLineRepository.save(tenTshirtsDeliveryOrderLine);
  }
}
