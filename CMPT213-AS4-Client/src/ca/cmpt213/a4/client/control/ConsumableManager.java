package ca.cmpt213.a4.client.control;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * This class handles removing, adding
 * loading and saving items
 */
public class ConsumableManager {
    private static final ConsumableFactory consumableFactory = ConsumableFactory.getInstance();
    private static final ArrayList<Consumable> itemsList = new ArrayList<>();

    // adding, removing, loading and saving items inspired by https://zetcode.com/java/getpostrequest/
    // and https://www.baeldung.com/httpurlconnection-post

    /**
     * This method handles adding a consumable item to the server via a POST request
     *
     * @param choiceOfItem
     * @param consumable
     * @throws IOException
     */
    public static void addConsumableToServer(int choiceOfItem, String consumable) throws IOException {
        URL url;

        if (choiceOfItem == 1) {
            url = new URL("http://localhost:8080/addFood");
        } else {
            url = new URL("http://localhost:8080/addDrink");
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.valueOf(url)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(consumable))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        Collections.sort(itemsList);
    }

    /**
     * This method handles removing a consumable item by its indexOfItemRemoved via a POST request
     *
     * @param indexOfItemRemoved
     * @throws IOException
     */
    public static void removeConsumableFromServer(int indexOfItemRemoved) throws IOException {
        Consumable consumable = itemsList.get(indexOfItemRemoved - 1);
        URL url = new URL("http://localhost:8080/removeItem/" + (indexOfItemRemoved - 1));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.valueOf(url)))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(consumable)))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        Collections.sort(itemsList);
    }

    /**
     * This method returns the arraylist of consumables
     *
     * @return
     */
    public ArrayList<Consumable> getListOfConsumables() {
        return itemsList;
    }

    /**
     * This method handles loading consumable items into the server
     *
     * @throws IOException
     */
    public void loadItemsToServer() throws IOException {
        URL url = new URL("http://localhost:8080/listAll");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();

        int responseCode = con.getResponseCode();

        if (responseCode != 200) {
            throw new RuntimeException("Http response code is: " + responseCode);
        }

        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());

        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }

        scanner.close();
        JsonElement jsonElement = JsonParser.parseString(stringBuilder.toString());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject itemObject = jsonArray.get(i).getAsJsonObject();
            if (itemObject.get("choiceOfItem") != null) {
                String foodName = itemObject.get("name").getAsString();
                String foodNotes = itemObject.get("notes").getAsString();
                double foodPrice = itemObject.get("price").getAsDouble();
                double foodWeight = itemObject.get("info").getAsDouble();
                String foodExpiryDate = itemObject.get("expiryDate").getAsString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime dateOfExpiry = LocalDateTime.parse(foodExpiryDate, formatter);
                Consumable consumable = consumableFactory.createConsumable(itemObject.get("choiceOfItem").getAsInt(), foodName, foodNotes, foodPrice, foodWeight, dateOfExpiry);
                itemsList.add(consumable);
            }
        }
        Collections.sort(itemsList);
    }

    /**
     * This method removes an item at a certain index
     *
     * @param index
     */
    public void remove(int index) {
        if (itemsList.size() == 0) {
            return;
        }

        if (index <= itemsList.size() && index >= 1) {
            index--;
            itemsList.remove(index);
        }
    }

    /**
     * This method adds a given consumable
     *
     * @param consumable
     */
    public void add(Consumable consumable) {
        if (consumable == null) {
            return;
        } else {
            itemsList.add(consumable);
        }
    }

    /**
     * This method saves the consumable items from the server triggered by the exit of the GUI
     */
    public void saveFromServer() throws IOException {
        URL url = new URL("http://localhost:8080/exit");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();

        int responseCode = httpURLConnection.getResponseCode();

        if (responseCode != 200) {
            throw new RuntimeException("Http response code is: " + responseCode);
        }
        StringBuilder builder = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());

        while (scanner.hasNext()) {
            builder.append(scanner.nextLine());
        }
        scanner.close();
        Collections.sort(itemsList);
    }

}
