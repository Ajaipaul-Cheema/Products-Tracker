package ca.cmpt213.a4.webappserver.control;


/**
 * This interface handles structural properties of
 * a food or drink object
 */
public interface Consumable extends Comparable<Consumable> {

    /**
     * These abstract methods help us define a food or drink
     */
    int getChoiceOfItem();

    String getName();

    String getNotes();

    double getPrice();

    double getInfo();

    String getExpiryDate();

    long daysTillItemExpires();
}
