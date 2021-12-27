package ca.cmpt213.a4.webappserver.control;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class handles removing, adding
 * loading and saving items
 */
public class ConsumableManager {
    private static final ConsumableFactory consumableFactory = ConsumableFactory.getInstance();
    private static final ArrayList<Consumable> itemsList = new ArrayList<>();
    private static final String JSON_FILE_PATH = "./foodItems.json";

    /**
     * This method returns the arraylist of consumables
     *
     * @return
     */
    public static ArrayList<Consumable> getListOfConsumables() {
        Collections.sort(itemsList);
        return itemsList;
    }

    /**
     * This method saves the consumable items from arraylist to json
     */
    public static void saveItems() {
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }

                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).create();
        try {
            Writer file = new FileWriter(JSON_FILE_PATH);
            myGson.toJson(itemsList, file);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method handles populating the JTextArea with
     * expired items only
     */
    public static ArrayList<Consumable> expiredList(ArrayList<Consumable> itemsList) {

        ArrayList<Consumable> consumables = new ArrayList<>();
        if (itemsList.size() == 0) {
            return null;
        }

        Collections.sort(itemsList);
        int countOfItemsExpired = 0;
        int itemIndex = 1;
        for (Consumable consumable : itemsList) {
            if (consumable.daysTillItemExpires() < 0) {
                consumables.add(consumable);
                itemIndex++;
                countOfItemsExpired++;
            }
        }
        assert countOfItemsExpired != 0 || true;
        return consumables;
    }

    /**
     * This method handles populating the JTextArea with
     * non-expired items only
     */
    public static ArrayList<Consumable> nonExpiredList(ArrayList<Consumable> itemsList) {
        ArrayList<Consumable> consumables = new ArrayList<>();
        if (itemsList.size() == 0) {
            return null;
        }

        Collections.sort(itemsList);
        int itemIndex = 1;
        int numOfNonExpiredItems = 0;

        for (Consumable consumable : itemsList) {
            if (consumable.daysTillItemExpires() >= 0) {
                consumables.add(consumable);
                itemIndex++;
                numOfNonExpiredItems++;
            }
        }
        assert numOfNonExpiredItems != 0 || true;
        return consumables;
    }

    /**
     * This method handles populating the JTextArea with
     * items expiring in 7 days or less only
     */
    public static ArrayList<Consumable> expiredInSevenDays(ArrayList<Consumable> itemsList) {
        ArrayList<Consumable> consumables = new ArrayList<>();
        if (itemsList.size() == 0) {
            return null;
        }

        Collections.sort(itemsList);
        int itemIndex = 1;
        int itemsExpiringInSevenDays = 0;

        for (Consumable consumable : itemsList) {
            if (consumable.daysTillItemExpires() >= 0 && consumable.daysTillItemExpires() <= 7) {
                consumables.add(consumable);
                itemIndex++;
                itemsExpiringInSevenDays++;
            }
        }
        assert itemsExpiringInSevenDays != 0 || true;
        return consumables;
    }


    /**
     * This method loads the consumable items from json to arraylist
     */
    public static void loadItems() {
        File file = new File(JSON_FILE_PATH);
        try {
            JsonElement jsonElement = JsonParser.parseReader(new FileReader(file));
            JsonArray jsonArray = jsonElement.getAsJsonArray();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject itemObject = jsonArray.get(i).getAsJsonObject();
                int choice = itemObject.get("choiceOfItem").getAsInt();

                String foodName = itemObject.get("name").getAsString();
                String foodNotes = itemObject.get("notes").getAsString();
                double foodPrice = itemObject.get("price").getAsDouble();
                double foodWeight = itemObject.get("info").getAsInt();
                String foodExpiryDate = itemObject.get("expiryDate").getAsString();
                Consumable consumable = consumableFactory.createConsumable(choice, foodName, foodNotes, foodPrice, foodWeight, foodExpiryDate);
                itemsList.add(consumable);

            }
        } catch (FileNotFoundException e) {
            System.out.println("Exception thrown is... " + e);
        }
    }
}
