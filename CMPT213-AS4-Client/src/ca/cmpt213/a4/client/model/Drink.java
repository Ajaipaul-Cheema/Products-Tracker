package ca.cmpt213.a4.client.model;

import ca.cmpt213.a4.client.control.Consumable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * This class creates all attributes of a drink item
 * Updates the attributes of the drink item per user input
 * and implements toString() to show all attributes of drink item
 * such as name, notes, price and expiry date
 * it also has a compareTo method to compare expiry dates
 * of consumables
 */
public class Drink implements Consumable {
    private final String drinkName;
    private final String drinkNotes;
    private final double drinkPrice;
    private final double drinkVolume;
    private final LocalDateTime drinkExpiryDate;
    private int choiceOfItem;
    private String drinkExpired;

    public Drink(int choiceOfItem, String name, String notes, double price, double volume,
                 LocalDateTime expiryDate) {
        this.choiceOfItem = choiceOfItem;
        this.drinkName = name;
        this.drinkNotes = notes;
        this.drinkPrice = price;
        this.drinkVolume = volume;
        this.drinkExpiryDate = expiryDate;
    }

    /**
     * This toString method will allow us to display info
     * about all attributes of a certain food item
     */
    @Override
    public String toString() {
        daysTillItemExpires();
        DateTimeFormatter formatted = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        drinkExpired = drinkExpiryDate.format(formatted);

        if (daysTillItemExpires() == 0) {
            return "This is a drink item." + "\n" +
                    "Name: " + drinkName + "\n" +
                    "Notes: " + drinkNotes + "\n" +
                    "Price: " + String.format("%.2f", drinkPrice) + "\n" +
                    "Volume: " + String.format("%.2f", drinkVolume) + "\n" +
                    "Expiry date: " + drinkExpired + "\n" +
                    "This drink item will expire today.";
        } else if (daysTillItemExpires() < 0) {
            return "This is a drink item." + "\n" +
                    "Name: " + drinkName + "\n" +
                    "Notes: " + drinkNotes + "\n" +
                    "Price: " + String.format("%.2f", drinkPrice) + "\n" +
                    "Volume: " + String.format("%.2f", drinkVolume) + "\n" +
                    "Expiry date: " + drinkExpired + "\n" +
                    "This drink item is expired for " + Math.abs(daysTillItemExpires()) + " day(s).";
        }

        return "This is a drink item." + "\n" +
                "Name: " + drinkName + "\n" +
                "Notes: " + drinkNotes + "\n" +
                "Price: " + String.format("%.2f", drinkPrice) + "\n" +
                "Volume: " + String.format("%.2f", drinkVolume) + "\n" +
                "Expiry date: " + drinkExpired + "\n" +
                "This drink item will be expired in " + daysTillItemExpires() + " day(s).";
    }

    /**
     * This method returns how many days are left or have passed
     * since food item has expired or will expire
     */
    public long daysTillItemExpires() {
        return DAYS.between(LocalDateTime.now().toLocalDate(), drinkExpiryDate.toLocalDate());
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
        return drinkName;
    }

    @Override
    public String getNotes() {
        return drinkNotes;
    }

    @Override
    public double getPrice() {
        return drinkPrice;
    }

    @Override
    public double getSize() {
        return drinkVolume;
    }

    @Override
    public LocalDateTime getExpiryDate() {
        return drinkExpiryDate;
    }

    /**
     * This method helps us to compare expiry dates of consumables
     */
    @Override
    public int compareTo(Consumable o) {
        LocalDateTime currentTime = LocalDateTime.now();
        long daysTillExpired = DAYS.between(currentTime.toLocalDate(), drinkExpiryDate.toLocalDate());

        if (daysTillExpired < o.daysTillItemExpires()) {
            return -1;
        }
        return 1;
    }

}
