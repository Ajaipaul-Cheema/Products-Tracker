package ca.cmpt213.a4.client;

import ca.cmpt213.a4.client.view.MainMenuUI;

import javax.swing.*;

/**
 * This class handles calling the SwingUtilities runnable
 * starting the GUI
 */
public class Main {
    /**
     * This function starts the GUI
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(MainMenuUI::new));
    }
}
