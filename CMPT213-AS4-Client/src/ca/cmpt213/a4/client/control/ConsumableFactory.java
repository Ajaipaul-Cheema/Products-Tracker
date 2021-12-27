package ca.cmpt213.a4.client.control;


import ca.cmpt213.a4.client.model.Drink;
import ca.cmpt213.a4.client.model.Food;

import java.time.LocalDateTime;

/**
 * This class handles generating new consumable items via
 * a singleton design of a class
 */
public class ConsumableFactory {
    private static final ConsumableFactory INSTANCE = new ConsumableFactory();
    private static Food food;
    private static Drink drink;

    public ConsumableFactory() {
    }

    public static ConsumableFactory getInstance() {
        return INSTANCE;
    }

    /**
     * This method helps create new instances of a consumable item
     * and returns that newly made consumable
     */
    public Consumable createConsumable(int choiceOfItem, String name, String notes, double price, double quantityItem, LocalDateTime expiryDate) {
        if (choiceOfItem == 1) {
            food = new Food(choiceOfItem, name, notes, price, quantityItem, expiryDate);
            return food;
        } else if (choiceOfItem == 2) {
            drink = new Drink(choiceOfItem, name, notes, price, quantityItem, expiryDate);
            return drink;
        }
        return null;
    }


}