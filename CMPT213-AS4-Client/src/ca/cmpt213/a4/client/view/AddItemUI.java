package ca.cmpt213.a4.client.view;

import ca.cmpt213.a4.client.control.Consumable;
import ca.cmpt213.a4.client.control.ConsumableFactory;
import ca.cmpt213.a4.client.control.ConsumableManager;
import com.github.lgooddatepicker.components.DatePicker;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * This class handles accepting inputs of all attributes
 * of a food or drink item via JTextFields
 * It also handles invalid input for those attributes
 * If valid inputs are typed in, it then adds that item
 */
public class AddItemUI extends JDialog implements ActionListener {
    private final ConsumableFactory consumableFactory = ConsumableFactory.getInstance();
    private final JPanel panel;
    private Consumable consumable;
    private JLabel weightOrVolumeLabel;
    private JLabel typeLabel;
    private JLabel nameLabel;
    private JLabel notesLabel;
    private JLabel priceLabel;
    private JLabel expiryDateLabel;
    private JTextField nameField;
    private JTextField notesField;
    private JTextField priceField;
    private JTextField weightOrVolumeField;
    private JButton cancel;
    private JButton addingItem;
    private JComboBox<String> typeItem;
    private DatePicker expiryDateItem;
    private LocalDateTime localDateTime;
    private int typeConsumable = 1;

    public AddItemUI(JFrame jFrame) {
        super(jFrame, true);
        Box box = Box.createHorizontalBox();
        setSize(600, 800);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        populatingUI();
        addContentToPanels();
        setContentPane(panel);
        super.add(box);
    }

    /**
     * This method handles creating all the labels and text fields for
     * the attributes of the item
     * Also creates the combo box for the type of item and creates the
     * date picker
     */
    private void populatingUI() {
        Font typeItemFont = new Font("MS Sans Serif", Font.BOLD, 12);

        typeLabel = new JLabel("Type: ");
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel = new JLabel("Name: ");
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        notesLabel = new JLabel("Notes: ");
        notesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceLabel = new JLabel("Price: ");
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        weightOrVolumeLabel = new JLabel("Weight: ");
        weightOrVolumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        expiryDateLabel = new JLabel("Expiry Date: ");
        expiryDateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        typeItem = new JComboBox<>();
        typeItem.setMaximumSize(new Dimension(100, 50));
        typeItem.setSelectedItem("Food");
        typeItem.setFont(typeItemFont);
        typeItem.addItem("Food");
        typeItem.addItem("Drink");

        nameField = new JTextField(10);
        nameField.setMaximumSize(new Dimension(100, 50));
        notesField = new JTextField(10);
        notesField.setMaximumSize(new Dimension(100, 50));

        priceField = new JTextField(10);
        priceField.setMaximumSize(new Dimension(100, 50));
        priceField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent charClicked) {
                priceField.setEditable((charClicked.getKeyChar() >= '0' && charClicked.getKeyChar() <= '9') || charClicked.getKeyChar() == '.' || charClicked.getKeyChar() == 8);
            }
        });

        weightOrVolumeField = new JTextField(10);
        weightOrVolumeField.setMaximumSize(new Dimension(100, 51));
        weightOrVolumeField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent charClicked) {
                weightOrVolumeField.setEditable((charClicked.getKeyChar() >= '0' && charClicked.getKeyChar() <= '9') || charClicked.getKeyChar() == '.' || charClicked.getKeyChar() == 8);
            }
        });

        // set date time picker
        expiryDateItem = new DatePicker();
        expiryDateItem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (expiryDateItem.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(expiryDateItem, "Choose an expiry date.");
                }
            }
        });

        typeItem.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (typeItem.getSelectedItem().toString().equals("Food")) {
                    weightOrVolumeLabel.setText("Weight: ");
                    typeConsumable = 1;
                } else {
                    weightOrVolumeLabel.setText("Volume: ");
                    typeConsumable = 2;
                }
            }
        });

        addingItem = new JButton("Add consumable");
        addingItem.setAlignmentX(Component.CENTER_ALIGNMENT);
        addingItem.addActionListener(this);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
    }

    /**
     * This method handles adding everything to the panel
     * and also creates space between content for visual
     * purposes
     */
    private void addContentToPanels() {
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(typeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(typeItem);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(nameLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(nameField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(notesLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(notesField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(priceLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(priceField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(weightOrVolumeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(weightOrVolumeField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(expiryDateLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(expiryDateItem);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(addingItem);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(cancel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    /**
     * This method handles saving the consumable that is
     * added from the add dialog
     */
    public Consumable saveConsumable() {
        setTitle("Add Consumable");
        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);
        if (consumable == null) {
            return null;
        }
        return consumable;
    }

    /**
     * This method handles adding an item or cancelling to add
     * an item via the use of buttons
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (!expiryDateItem.getText().isEmpty()) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
            LocalDate date = LocalDate.parse(expiryDateItem.getText(), format);
            localDateTime = date.atStartOfDay();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            localDateTime.format(formatter);
        }

        if (e.getSource() == cancel) {
            dispose();
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setVisible(false);
        } else if (e.getSource() == addingItem) {
            if (!nameField.getText().isEmpty() && !priceField.getText().isEmpty() &&
                    !weightOrVolumeField.getText().isEmpty() && !expiryDateItem.getText().isEmpty()) {

                if (weightOrVolumeField.getText().equals("Weight: ")) {
                    typeConsumable = 1;
                } else if (weightOrVolumeField.getText().equals("Volume: ")) {
                    typeConsumable = 2;
                }

                consumable = consumableFactory.createConsumable(typeConsumable, nameField.getText(),
                        notesField.getText(), Double.parseDouble(priceField.getText()),
                        Double.parseDouble(weightOrVolumeField.getText()), localDateTime);

                String item = new JSONObject()
                        .put("choiceOfItem", consumable.getChoiceOfItem())
                        .put("name", consumable.getName())
                        .put("notes", consumable.getNotes())
                        .put("price", consumable.getPrice())
                        .put("info", consumable.getSize())
                        .put("expiryDate", consumable.getExpiryDate())
                        .toString();

                try {
                    ConsumableManager.addConsumableToServer(consumable.getChoiceOfItem(), item);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "All attributes except notes must be non-empty.");
            }
        }
    }

}