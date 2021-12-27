package ca.cmpt213.a4.client.view;

import ca.cmpt213.a4.client.control.Consumable;
import ca.cmpt213.a4.client.control.ConsumableManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class handles allowing user to choose which
 * item to remove and thus removes that item and updates
 * the view of list of items
 */
public class RemoveItemUI extends JDialog implements ActionListener {
    private final JTextField removeItemIndexField;
    private final JButton removeItem, cancelRemovingItem;
    private final JTextArea itemsTextArea;
    private final ConsumableManager consumableManager = new ConsumableManager();
    private int indexOfItemRemoved = 0;

    public RemoveItemUI(JFrame jFrame) {
        super(jFrame, true);
        Box box = Box.createHorizontalBox();

        itemsTextArea = new JTextArea(25, 36);
        itemsTextArea.setLineWrap(true);
        itemsTextArea.setVisible(true);
        itemsTextArea.setEditable(false);
        JScrollPane itemsScrollPane = new JScrollPane(itemsTextArea);
        itemsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        itemsScrollPane.getVerticalScrollBar().setUnitIncrement(20);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBounds(0, 300, 500, 30);

        removeItemIndexField = new JTextField(7);
        removeItemIndexField.setMaximumSize(new Dimension(100, 50));
        removeItemIndexField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent charClicked) {
                removeItemIndexField.setEditable((charClicked.getKeyChar() >= '0' && charClicked.getKeyChar() <= '9') || charClicked.getKeyChar() == '.' || charClicked.getKeyChar() == 8);
            }
        });

        JLabel removingItemLabel = new JLabel();
        removingItemLabel.setText("Index of consumable to remove: ");
        removingItemLabel.setBounds(0, 11, 60, 90);
        removingItemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        removeItem = new JButton("Remove consumable");
        removeItem.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeItem.addActionListener(this);

        cancelRemovingItem = new JButton("Cancel");
        cancelRemovingItem.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelRemovingItem.addActionListener(this);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(removingItemLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(removeItemIndexField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(removeItem);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(cancelRemovingItem);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.setVisible(true);

        panel.add(itemsScrollPane);

        setContentPane(panel);

        listEveryItem(consumableManager.getListOfConsumables());
        super.add(box);
    }

    /**
     * This method lists all the items in a JTextArea for the user
     * to choose the deleted item from
     */
    static void listingItems(ArrayList<Consumable> consumables, JTextArea itemsTextArea) {
        if (consumables.size() == 0) {
            itemsTextArea.append("No items to show. \n");
        } else {
            int displayIndex = 1;

            Collections.sort(consumables);
            for (Consumable items : consumables) {
                itemsTextArea.append("Item #" + displayIndex + "\n");
                itemsTextArea.append(items.toString() + "\n");
                itemsTextArea.append("\n");
                displayIndex++;
            }
        }
    }

    /**
     * This method handles saving the index of the item
     * the user decided to remove
     *
     * @return int
     */
    public int saveIndexRemoved() {
        setTitle("Remove Consumable");
        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);
        return indexOfItemRemoved;
    }

    /**
     * This method handles listing all items
     */
    public void listEveryItem(ArrayList<Consumable> consumables) {
        listingItems(consumables, itemsTextArea);
    }

    /**
     * This method handles cancelling or removing button actions
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == removeItem) {
            if (!removeItemIndexField.getText().isEmpty()) {
                indexOfItemRemoved = Integer.parseInt(removeItemIndexField.getText());
                if (indexOfItemRemoved <= 0 || indexOfItemRemoved > consumableManager.getListOfConsumables().size()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Index of item to remove must be between 1 and number of items in list of items below");
                } else {
                    itemsTextArea.selectAll();
                    itemsTextArea.replaceSelection("");

                    try {
                        ConsumableManager.removeConsumableFromServer(indexOfItemRemoved);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    listEveryItem(consumableManager.getListOfConsumables());
                    dispose();
                    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    setVisible(false);
                }
            } else if (consumableManager.getListOfConsumables().size() == 0) {
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "There are no items to remove. Click cancel at top right of screen or the cancel button to go back and add an item first.");
            } else {
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Type a number to remove between 1 and number of items in list of items below");
            }

        } else if (e.getSource() == cancelRemovingItem) {
            itemsTextArea.selectAll();
            itemsTextArea.replaceSelection("");
            dispose();
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setVisible(false);
        }
    }
}