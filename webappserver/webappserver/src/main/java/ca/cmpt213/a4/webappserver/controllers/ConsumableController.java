package ca.cmpt213.a4.webappserver.controllers;

import ca.cmpt213.a4.webappserver.control.Consumable;
import ca.cmpt213.a4.webappserver.control.ConsumableManager;
import ca.cmpt213.a4.webappserver.model.Drink;
import ca.cmpt213.a4.webappserver.model.Food;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * This class handles all requests to the server such as listing
 * all items, listing items that are expired or non-expired or are
 * expiring in 7 days
 * It also handles requests for adding a food/drink item and removing an item
 * by its index
 * It also loads in and saves items via requests to the server
 * Also shows a "System is up!" message
 */
@RestController
public class ConsumableController {

    /**
     * This method shows a welcome message via get mapping
     *
     * @return
     */
    @GetMapping("/ping")
    public String getMessage() {
        return "System is up!";
    }

    /**
     * This method lists all items on the server via get mapping
     *
     * @return
     */
    @GetMapping("/listAll")
    public ArrayList<Consumable> getAllConsumables() {
        return ConsumableManager.getListOfConsumables();
    }

    /**
     * This method adds a food item to the server where the request body is a food object
     * via post mapping
     *
     * @param consumable
     * @return
     */
    @PostMapping("/addFood")
    @ResponseStatus(HttpStatus.CREATED)
    public ArrayList<Consumable> addFood(@RequestBody Food consumable) {
        ConsumableManager.getListOfConsumables().add(consumable);
        return ConsumableManager.getListOfConsumables();
    }

    /**
     * This method adds a drink item to the server where the request body is a drink object
     * via post mapping
     *
     * @param consumable
     * @return
     */
    @PostMapping("/addDrink")
    @ResponseStatus(HttpStatus.CREATED)
    public ArrayList<Consumable> addDrink(@RequestBody Drink consumable) {
        ConsumableManager.getListOfConsumables().add(consumable);
        return ConsumableManager.getListOfConsumables();
    }

    /**
     * This method removes an item from the server by its index where the path variable is the index
     * to be removed via post mapping
     *
     * @param index
     * @return
     */
    @PostMapping("/removeItem/{index}")
    public ArrayList<Consumable> removeItem(@PathVariable("index") int index) {
        ConsumableManager.getListOfConsumables().remove(index);
        return ConsumableManager.getListOfConsumables();
    }

    /**
     * This method lists all expired items on the server via get mapping
     *
     * @return
     */
    @GetMapping("/listExpired")
    public ArrayList<Consumable> listExpired() {
        return ConsumableManager.expiredList(ConsumableManager.getListOfConsumables());
    }

    /**
     * This method lists all non-expired items on the server via get mapping
     *
     * @return
     */
    @GetMapping("/listNonExpired")
    public ArrayList<Consumable> listNonExpired() {
        return ConsumableManager.nonExpiredList(ConsumableManager.getListOfConsumables());
    }

    /**
     * This method lists all items expiring in 7 days on the server via get mapping
     *
     * @return
     */
    @GetMapping("/listExpiringIn7days")
    public ArrayList<Consumable> listExpiringIn7Days() {
        return ConsumableManager.expiredInSevenDays(ConsumableManager.getListOfConsumables());
    }

    /**
     * This method saves all items from the server via get mapping triggered by the exit
     * of client's GUI
     *
     * @return
     */
    @GetMapping("/exit")
    public void saveItems() {
        ConsumableManager.saveItems();
    }

}
