package ca.cmpt213.a4.client.view;

import ca.cmpt213.a4.client.control.Consumable;
import ca.cmpt213.a4.client.control.ConsumableManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class handles showing all items, expired & non-expired
 * items and items expiring in 7 days with the help of buttons
 * It also handles updating the view of items after an item has
 * been added or removed
 */
public class MainMenuUI extends JFrame implements ActionListener {
    private final JFrame mainFrame;
    private final JTextArea allItemsTextArea;
    private final ConsumableManager consumableManager = new ConsumableManager();
    private JButton allItems, expiredItems, nonExpiredItems, expiringIn7DaysItems, removeItemButton;
    private JButton addButton;

    public MainMenuUI() {
        mainFrame = new JFrame("My Consumable Tracker");
        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            consumableManager.loadItemsToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // inspired from https://www.javatpoint.com/java-windowlistener
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(mainFrame,
                        "Confirming that you want to close?", "Shutting Window Off",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    try {
                        consumableManager.saveFromServer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                } else {
                    mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());

        addingButtons();

        allItemsTextArea = new JTextArea(25, 36);
        allItemsTextArea.setLineWrap(true);
        allItemsTextArea.setVisible(true);
        allItemsTextArea.setEditable(false);

        JScrollPane itemsScrollPane = new JScrollPane(allItemsTextArea);
        itemsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        itemsScrollPane.getVerticalScrollBar().setUnitIncrement(20);

        JPanel addAndRemovePanel = new JPanel();
        addAndRemovePanel.setBounds(0, 700, 1000, 30);
        addAndRemovePanel.add(addButton);
        addAndRemovePanel.add(removeItemButton);
        addAndRemovePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        addAndRemovePanel.setVisible(true);

        mainPanel.setBounds(0, 50, 1000, 50);
        mainPanel.add(allItems);
        mainPanel.add(expiredItems);
        mainPanel.add(nonExpiredItems);
        mainPanel.add(expiringIn7DaysItems);
        mainPanel.add(itemsScrollPane);
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        mainPanel.setVisible(true);

        mainFrame.getContentPane().add(mainPanel, BoxLayout.X_AXIS);
        mainFrame.getContentPane().add(addAndRemovePanel, BorderLayout.SOUTH);
        mainFrame.setSize(400, 500);
        mainFrame.setVisible(true);

        listEveryItem(consumableManager.getListOfConsumables());
        allItemsTextArea.append("\n");
    }

    /**
     * This method handles adding the buttons for all items,
     * expired & non-expired items and items expiring in 7 days
     * With these buttons, the view of the appropriate items
     * are shown
     */
    private void addingButtons() {
        allItems = new JButton();
        allItems.setText("All Items");
        allItems.setFocusable(false);
        allItems.setFont(new Font("Serif", Font.ITALIC, 13));
        allItems.addActionListener(this);
        allItems.setVisible(true);

        expiredItems = new JButton();
        expiredItems.setText("Expired Items");
        expiredItems.setFocusable(false);
        expiredItems.setFont(new Font("Serif", Font.ITALIC, 13));
        expiredItems.addActionListener(this);
        expiredItems.setVisible(true);

        nonExpiredItems = new JButton();
        nonExpiredItems.setText("Non Expired Items");
        nonExpiredItems.setFocusable(false);
        nonExpiredItems.setFont(new Font("Serif", Font.ITALIC, 13));
        nonExpiredItems.addActionListener(this);
        nonExpiredItems.setVisible(true);

        expiringIn7DaysItems = new JButton();
        expiringIn7DaysItems.setText("Items Expiring in 7 Days");
        expiringIn7DaysItems.setFocusable(false);
        expiringIn7DaysItems.setFont(new Font("Serif", Font.ITALIC, 13));
        expiringIn7DaysItems.addActionListener(this);
        expiringIn7DaysItems.setVisible(true);

        addButton = new JButton("Add Item");
        addButton.setFont(new Font("Serif", Font.ITALIC, 13));
        addButton.addActionListener(this);

        removeItemButton = new JButton("Remove Item");
        removeItemButton.setFont(new Font("Serif", Font.ITALIC, 13));
        removeItemButton.addActionListener(this);
    }

    /**
     * This lists the updated items in all items, expired or non expired
     * items and items in expiring in 7 days
     */
    private void listEveryItem(ArrayList<Consumable> consumables) {
        RemoveItemUI.listingItems(consumables, allItemsTextArea);
    }

    /**
     * This method handles populating the JTextArea with
     * expired items only
     */
    private void expiredList(ArrayList<Consumable> itemsList) {
        if (itemsList.size() == 0) {
            allItemsTextArea.append("No expired items to show.");
            allItemsTextArea.append("\n");
            return;
        }

        Collections.sort(itemsList);
        int countOfItemsExpired = 0;
        int itemIndex = 1;
        for (Consumable consumable : itemsList) {
            if (consumable.daysTillItemExpires() < 0) {
                allItemsTextArea.append("Item #" + itemIndex + "\n");
                allItemsTextArea.append(consumable + "\n");
                allItemsTextArea.append("\n");
                itemIndex++;
                countOfItemsExpired++;
            }
        }
        if (countOfItemsExpired == 0) {
            allItemsTextArea.append("No expired items to show.");
            allItemsTextArea.append("\n");
        }
    }

    /**
     * This method handles populating the JTextArea with
     * non-expired items only
     */
    private void nonExpiredList(ArrayList<Consumable> itemsList) {
        if (itemsList.size() == 0) {
            allItemsTextArea.append("No non-expired items to show.");
            allItemsTextArea.append("\n");
            return;
        }

        Collections.sort(itemsList);
        int itemIndex = 1;
        int numOfNonExpiredItems = 0;

        for (Consumable consumable : itemsList) {
            if (consumable.daysTillItemExpires() >= 0) {
                allItemsTextArea.append("Item #" + itemIndex + "\n");
                allItemsTextArea.append(consumable + "\n");
                allItemsTextArea.append("\n");
                itemIndex++;
                numOfNonExpiredItems++;
            }
        }
        if (numOfNonExpiredItems == 0) {
            allItemsTextArea.append("No non-expired items to show.");
            allItemsTextArea.append("\n");
        }
    }

    /**
     * This method handles populating the JTextArea with
     * items expiring in 7 days or less only
     */
    private void expiredInSevenDays(ArrayList<Consumable> itemsList) {
        if (itemsList.size() == 0) {
            allItemsTextArea.append("No items expiring in 7 days to show.");
            allItemsTextArea.append("\n");
            return;
        }

        Collections.sort(itemsList);
        int itemIndex = 1;
        int itemsExpiringInSevenDays = 0;

        for (Consumable consumable : itemsList) {
            if (consumable.daysTillItemExpires() >= 0 && consumable.daysTillItemExpires() <= 7) {
                allItemsTextArea.append("Item #" + itemIndex + "\n");
                allItemsTextArea.append(consumable + "\n");
                allItemsTextArea.append("\n");
                itemIndex++;
                itemsExpiringInSevenDays++;
            }
        }

        if (itemsExpiringInSevenDays == 0) {
            allItemsTextArea.append("No items expiring in 7 days to show.");
            allItemsTextArea.append("\n");
        }
    }

    /**
     * This method handles clicking on the buttons that show
     * you all items, expired/non-expired items and items expiring
     * in 7 days
     * It also handles going to the add and remove dialogs
     * via clicks of those buttons
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            AddItemUI dialog = new AddItemUI(mainFrame);
            Consumable consumable = dialog.saveConsumable();
            consumableManager.add(consumable);
            allItemsTextArea.selectAll();
            allItemsTextArea.replaceSelection("");
            listEveryItem(consumableManager.getListOfConsumables());
            allItemsTextArea.append("\n");
        } else if (e.getSource() == removeItemButton) {
            RemoveItemUI dialog = new RemoveItemUI(mainFrame);
            int posOfItemRemoved = dialog.saveIndexRemoved();
            consumableManager.remove(posOfItemRemoved);
            allItemsTextArea.selectAll();
            allItemsTextArea.replaceSelection("");
            listEveryItem(consumableManager.getListOfConsumables());
            allItemsTextArea.append("\n");
        } else if (e.getSource() == allItems) {
            allItemsTextArea.selectAll();
            allItemsTextArea.replaceSelection("");
            listEveryItem(consumableManager.getListOfConsumables());
            allItemsTextArea.append("\n");
        } else if (e.getSource() == expiredItems) {
            allItemsTextArea.selectAll();
            allItemsTextArea.replaceSelection("");
            expiredList(consumableManager.getListOfConsumables());
            allItemsTextArea.append("\n");
        } else if (e.getSource() == nonExpiredItems) {
            allItemsTextArea.selectAll();
            allItemsTextArea.replaceSelection("");
            nonExpiredList(consumableManager.getListOfConsumables());
            allItemsTextArea.append("\n");
        } else if (e.getSource() == expiringIn7DaysItems) {
            allItemsTextArea.selectAll();
            allItemsTextArea.replaceSelection("");
            expiredInSevenDays(consumableManager.getListOfConsumables());
            allItemsTextArea.append("\n");
        }
    }
}

