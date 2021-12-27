package ca.cmpt213.a4.webappserver.model;


import ca.cmpt213.a4.webappserver.control.Consumable;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private final int choiceOfItem;
    private final String name;
    private final String notes;
    private final double price;
    private final double info;
    private final String expiryDate;
    private String drinkExpired;

    public Drink(@JsonProperty("choiceOfItem") int choiceOfItem,
                 @JsonProperty("name") String name,
                 @JsonProperty("notes") String notes,
                 @JsonProperty("price") double price,
                 @JsonProperty("volume") double volume,
                 @JsonProperty("expiryDate") String expiryDate) {
        this.choiceOfItem = choiceOfItem;
        this.name = name;
        this.notes = notes;
        this.price = price;
        this.info = volume;
        this.expiryDate = expiryDate;
    }

    /**
     * This toString method will allow us to display info
     * about all attributes of a certain food item
     */
    @Override
    public String toString() {
        daysTillItemExpires();
        DateTimeFormatter formatted = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        drinkExpired = expiryDate.format(String.valueOf(formatted));
        getName();
        getNotes();
        getPrice();
        getExpiryDate();

        if (daysTillItemExpires() == 0) {
            return "This is a drink item." + "\n" +
                    "Name: " + name + "\n" +
                    "Notes: " + notes + "\n" +
                    "Price: " + String.format("%.2f", price) + "\n" +
                    "Volume: " + String.format("%.2f", info) + "\n" +
                    "Expiry date: " + drinkExpired + "\n" +
                    "This drink item will expire today.";
        } else if (daysTillItemExpires() < 0) {
            return "This is a drink item." + "\n" +
                    "Name: " + name + "\n" +
                    "Notes: " + notes + "\n" +
                    "Price: " + String.format("%.2f", price) + "\n" +
                    "Volume: " + String.format("%.2f", info) + "\n" +
                    "Expiry date: " + drinkExpired + "\n" +
                    "This drink item is expired for " + Math.abs(daysTillItemExpires()) + " day(s).";
        }

        return "This is a drink item." + "\n" +
                "Name: " + name + "\n" +
                "Notes: " + notes + "\n" +
                "Price: " + String.format("%.2f", price) + "\n" +
                "Volume: " + String.format("%.2f", info) + "\n" +
                "Expiry date: " + drinkExpired + "\n" +
                "This drink item will be expired in " + daysTillItemExpires() + " day(s).";
    }

    /**
     * This method returns how many days are left or have passed
     * since food item has expired or will expire
     */
    public long daysTillItemExpires() {
        DateTimeFormatter timePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime date = LocalDateTime.parse(expiryDate, timePattern);
        return DAYS.between(LocalDateTime.now().toLocalDate(), date.toLocalDate());
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
        return name;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public double getInfo() {
        return info;
    }

    @Override
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * This method helps us to compare expiry dates of consumables
     */
    @Override
    public int compareTo(Consumable o) {
        DateTimeFormatter timePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime date = LocalDateTime.parse(expiryDate, timePattern);
        long daysTillExpired = DAYS.between(LocalDateTime.now().toLocalDate(), date.toLocalDate());

        if (daysTillExpired < o.daysTillItemExpires()) {
            return -1;
        }
        return 1;
    }

}
