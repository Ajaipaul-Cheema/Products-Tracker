package ca.cmpt213.a4.webappserver.control;

import ca.cmpt213.a4.webappserver.model.Drink;
import ca.cmpt213.a4.webappserver.model.Food;

/**
 * This class handles generating new consumable items via
 * a singleton design of a class
 */
public class ConsumableFactory {
    private static final ConsumableFactory INSTANCE = new ConsumableFactory();
    private static Food food;
    private static Drink drink;

    private ConsumableFactory() {
    }

    public static ConsumableFactory getInstance() {
        return INSTANCE;
    }

    /**
     * This method helps create new instances of a consumable item
     * and returns that newly made consumable
     */
    public Consumable createConsumable(int inputItem, String name, String notes, double price, double quantityItem, String expiryDate) {
        if (inputItem == 1) {
            food = new Food(inputItem, name, notes, price, quantityItem, expiryDate);
            return food;
        } else if (inputItem == 2) {
            drink = new Drink(inputItem, name, notes, price, quantityItem, expiryDate);
            return drink;
        }
        return null;
    }


}
