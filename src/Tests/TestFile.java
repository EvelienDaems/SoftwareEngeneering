package Tests;

import Core.*;
import Core.Clients.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.midi.Soundbank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.ac.ua.ansymo.adbc.exceptions.*;


public class TestFile {   // core tests all the happy day scenario' s

    //    ADDRESS    //
    @Test
    public void testCorrectAddress(){
        Address address = new Address("straat", "369", "2222", "lazytown");
        assertEquals("straat 369\n 2222 lazytown", address.getFullAddress());
    }

    //    CLIENT    //
    @Test
    public void testCorrectClient(){
        Address address = new Address("straat", "369", "2222", "lazytown");
        Client client = new Client("Evelien", address);
        assertEquals("straat 369\n 2222 lazytown", client.address().getFullAddress());
        assertEquals("Evelien", client.name());
    }

    //    MAILING_PREFERENCES    //
    @Test
    public void testCorrectMailing_Preferences(){
        Mailing_Preferences preferences = new Mailing_Preferences(false, false, false);
        assertFalse(preferences.newsletters());
        assertFalse(preferences.promotions());
        assertFalse(preferences.third_party());

        preferences = new Mailing_Preferences(true, true, false);
        assertTrue(preferences.newsletters());
        assertTrue(preferences.promotions());
        assertFalse(preferences.third_party());
    }

    //    REGISTEREDCLIENT    //
    @Test
    public void testCorrectRegisteredClient(){
        Address address = new Address("straat", "369", "2222", "lazytown");
        Mailing_Preferences preferences = new Mailing_Preferences(false, false, false);
        RegisteredClient registeredclient = new RegisteredClient("Evelien", address, "blub", preferences);

        assertEquals("Evelien", registeredclient.name());
        assertEquals(address,registeredclient.address());

        // gegeven de documentatie zou dit true moeten returnen
        assertTrue(registeredclient.checkPassword("blub"));
        assertEquals(preferences, registeredclient.getMailPreferences());

        registeredclient.updatePassword("blubikbeneenhaai");
        assertTrue(registeredclient.checkPassword("blubikbeneenhaai"));

        preferences = new Mailing_Preferences(true, true, true);
        registeredclient.updateMailPreferences(preferences);
        assertEquals(preferences, registeredclient.getMailPreferences());
    }

    //    UNREGISTEREDCLIENT    //
    @Test
    public void testCorrectUnRegisteredClient(){
        Address address = new Address("straat", "369", "2222", "lazytown");
        UnregisteredClient unregisteredClient = new UnregisteredClient("Evelien", address);

        assertEquals("Evelien", unregisteredClient.name());
        assertEquals(address, unregisteredClient.address());
    }

    //    CLIENTS    //
    @Test
    public void testCorrectClients(){
        Clients clients = new Clients();

        Address address = new Address("straat", "369", "2222", "lazytown");
        Mailing_Preferences preferences = new Mailing_Preferences(false, false, false);
        RegisteredClient registeredclient = new RegisteredClient("Evelien", address, "blub", preferences);

        Address address2 = new Address("straat", "963", "2222", "lazytown");
        UnregisteredClient unregisteredClient = new UnregisteredClient("Lars", address2);

        clients.AddRegisteredClient(registeredclient, "Evelien");
        clients.AddUnregisteredClient(unregisteredClient);

        assertEquals(unregisteredClient, clients.getClient(unregisteredClient.ID()));
        assertEquals(registeredclient, clients.getRegisteredClient("Evelien", "blub"));

        // gegeven de documentatie zou dit wel degelijk moeten werken
        assertEquals(registeredclient, clients.getClient(registeredclient.ID()));
    }

    //    INTEGRATION TEST    //
    @Test
    public void testSystemIntegration(){
        // create all objects needed for testing
        Address address = new Address("straat", "nummer", "9999", "lazytown");
        Client client = new Client("Evelien", address);
        Cart cart = new Cart();
        Item item = new Item("spiekblaadjes", "handig voor tijdens de examens", 2);
        Catalog catalog = new Catalog();
        Order order = new Order(cart, client);
        Order order2 = new Order(cart, client);
        Category category = new Category();
        Orders orders = new Orders();

        //  CATALOG //
        catalog.addItem(item);

        assertEquals(1, catalog.getNumberOfItems());
        assertEquals(item, catalog.getItemByID(catalog.getAllIDs().iterator().next()));

        //  CART  //
        cart.addItem(item, 1000);
        cart.removeItem(item, 900);

        HashMap<Item, Integer> expected_cart_contents = new HashMap<Item, Integer>();
        expected_cart_contents.put(item, 100);
        assertEquals(expected_cart_contents, cart.contents());

        //  CATEGORY //
        category.addCategory("Visjes");

        ArrayList<String> expected_categories = new ArrayList<String>();
        String[] basic_categories = {"Phone", "Tablet", "Computer", "Image", "Sound", "Home & Living", "Kitchen", "Travel", "Fashion", "Sport", "Visjes"};
        for (String cat : basic_categories) {
            expected_categories.add(cat);
        }
        assertEquals(expected_categories, category.getCategories());

        //  ITEM  //
        assertEquals(1, item.ID());
        assertEquals("spiekblaadjes", item.name());
        assertEquals("handig voor tijdens de examens", item.desc());
        assertEquals(2, item.price(), 0);

        //  ORDER  //
        assertEquals(cart.contents(), order.getItems());
        assertEquals(1, order.ID());
        assertEquals(client, order.getClient());
        assertEquals(Order_Status.STARTED, order.getStatus());

        //  ORDERS //
        orders.addOrder(order);
        List<Order> expected_orders = new ArrayList<Order>();
        expected_orders.add(order);
        assertEquals(expected_orders, orders.getAllOrders());
        assertEquals(order, orders.getOrder(order.ID()));
        assertEquals(Order_Status.STARTED, orders.getOrderStatus(order.ID()));
        orders.requestOrderCancel(order.ID());
        assertEquals(Order_Status.CANCELED, orders.getOrderStatus(order.ID()));
    }

    //    TEST IF REQUIREMENTS GET TRIGGERED    //
    
    //    CART    //
    @Test
    public void testCartRequirements(){
        Cart cart = new Cart();
        Item item = new Item("spiekblaadjes", "handig voor tijdens de examens", 2);
        Item item2 = new Item("brood", "om op te eten", 100);

        PreConditionException e = assertThrows(PreConditionException.class, ()->cart.addItem(null, 5));
        assertTrue(e.getMessage().contains("Precondition broken!"));
        
        e = assertThrows(PreConditionException.class, ()->cart.addItem(item, -5));
        assertTrue(e.getMessage().contains("Precondition broken!"));
        
        cart.addItem(item, 5);
        
        e = assertThrows(PreConditionException.class, ()->cart.removeItem(null, 5));
        assertTrue(e.getMessage().contains("Precondition broken!"));
        
        e = assertThrows(PreConditionException.class, ()->cart.removeItem(item2, 5));
        assertTrue(e.getMessage().contains("Precondition broken!"));
       
        e = assertThrows(PreConditionException.class, ()->cart.removeItem(item, -5));
        assertTrue(e.getMessage().contains("Precondition broken!"));
    }
    
    //    CATALOG    //
    @Test
    public void testCatalogRequirements() {
    	Catalog catalog = new Catalog();
    	Item item = new Item("brood", "voedsel", 5);
    	
        PreConditionException e = assertThrows(PreConditionException.class, ()->catalog.addItem(null));
        assertTrue(e.getMessage().contains("Precondition broken!"));
        
        catalog.addItem(item);
        e = assertThrows(PreConditionException.class, ()->catalog.addItem(item));
        assertTrue(e.getMessage().contains("Precondition broken!"));

    }
    
    //    ORDERS    //
    @Test
    public void testOrdersRequirements() {
    	Orders orders = new Orders();

        PreConditionException e = assertThrows(PreConditionException.class, ()->orders.addOrder(null));
        assertTrue(e.getMessage().contains("Precondition broken!"));
        
        e = assertThrows(PreConditionException.class, ()->orders.getOrder(-5));
        assertTrue(e.getMessage().contains("Precondition broken!"));
        
        e = assertThrows(PreConditionException.class, ()->orders.getOrderStatus(-5));
        assertTrue(e.getMessage().contains("Precondition broken!"));
        
        e = assertThrows(PreConditionException.class, ()->orders.requestOrderCancel(-5));
        assertTrue(e.getMessage().contains("Precondition broken!"));
    }
    
    //    CATEGORY    //
    @Test
    public void testCategoryRequirements() {
    	Category category = new Category();
    	
    	PreConditionException e = assertThrows(PreConditionException.class, ()->category.addCategory(""));
        assertTrue(e.getMessage().contains("Precondition broken!"));
        
        e = assertThrows(PreConditionException.class, ()->category.addCategory("Phone"));
        assertTrue(e.getMessage().contains("Precondition broken!"));
    }
}