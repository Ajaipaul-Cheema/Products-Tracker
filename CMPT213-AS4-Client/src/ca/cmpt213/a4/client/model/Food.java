package ca.cmpt213.a4.client.model;

import ca.cmpt213.a4.client.control.Consumable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * This class creates all attributes of a food item
 * Updates the attributes of the food item per user input
 * and implements toString() to show all attributes of food item
 * such as name, notes, price and expiry date
 * it also has a compareTo method to compare expiry dates
 * of consumables
 */
public class Food implements Consumable {
    private final String foodName;
    private final String foodNotes;
    private final double foodPrice;
    private final double foodWeight;
    private final LocalDateTime foodExpiryDate;
    private int choiceOfItem;
    private String expired;

    public Food(int choiceOfItem, String name, String notes, double price, double weight,
                LocalDateTime expiryDate) {
        this.choiceOfItem = choiceOfItem;
        this.foodName = name;
        this.foodNotes = notes;
        this.foodPrice = price;
        this.foodWeight = weight;
        this.foodExpiryDate = expiryDate;
    }

    /**
     * This toString method will allow us to display info
     * about all attributes of a certain food item
     */
    @Override
    public String toString() {
        daysTillItemExpires();
        DateTimeFormatter formatted = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        expired = foodExpiryDate.format(formatted);

        if (daysTillItemExpires() == 0) {
            return "This is a food item." + "\n" +
                    "Name: " + foodName + "\n" +
                    "Notes: " + foodNotes + "\n" +
                    "Price: " + String.format("%.2f", foodPrice) + "\n" +
                    "Weight: " + String.format("%.2f", foodWeight) + "\n" +
                    "Expiry date: " + expired + "\n" +
                    "This food item will expire today.";
        } else if (daysTillItemExpires() < 0) {
            return "This is a food item." + "\n" +
                    "Name: " + foodName + "\n" +
                    "Notes: " + foodNotes + "\n" +
                    "Price: " + String.format("%.2f", foodPrice) + "\n" +
                    "Weight: " + String.format("%.2f", foodWeight) + "\n" +
                    "Expiry date: " + expired + "\n" +
                    "This food item is expired for " + Math.abs(daysTillItemExpires()) + " day(s).";
        }

        return "This is a food item." + "\n" +
                "Name: " + foodName + "\n" +
                "Notes: " + foodNotes + "\n" +
                "Price: " + String.format("%.2f", foodPrice) + "\n" +
                "Weight: " + String.format("%.2f", foodWeight) + "\n" +
                "Expiry date: " + expired + "\n" +
                "This food item will be expired in " + daysTillItemExpires() + " day(s).";
    }

    /**
     * This method returns how many days are left or have passed
     * since food item has expired or will expire
     */
    public long daysTillItemExpires() {
        return DAYS.between(LocalDateTime.now().toLocalDate(), foodExpiryDate.toLocalDate());
    }

    /**
     * Every method below this is a getter method
     * for all attributes that a food item has
     */
    @Override
    public int getChoiceOfItem() {
        return choiceOfItem;
    }

    @Override
    public String getName() {
        return foodName;
    }

    @Override
    public String getNotes() {
        return foodNotes;
    }

    @Override
    public double getPrice() {
        return foodPrice;
    }

    @Override
    public double getSize() {
        return foodWeight;
    }

    @Override
    public LocalDateTime getExpiryDate() {
        return foodExpiryDate;
    }

    /**
     * This method helps us to compare expiry dates of consumables
     */
    @Override
    public int compareTo(Consumable o) {
        LocalDateTime currentTime = LocalDateTime.now();
        long daysTillExpired = DAYS.between(currentTime.toLocalDate(), foodExpiryDate.toLocalDate());

        if (daysTillExpired < o.daysTillItemExpires()) {
            return -1;
        }
        return 1;

    }

}
