package Assignment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Restaurant extends JFrame {
    private List<Dish> dishes;
    private JTextField tableNumberField, dishField;
    private JButton addButton;

    public Restaurant() {
        super("Restaurant Ordering Table");
        this.dishes = new ArrayList<>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setResizable(false);

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create title label
        JLabel titleLabel = new JLabel("Restaurant Ordering Table");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create center panel for input fields
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        centerPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Add table number label and field
        JLabel tableNumberLabel = new JLabel("Table Number:");
        tableNumberField = new JTextField();
        centerPanel.add(tableNumberLabel);
        centerPanel.add(tableNumberField);

        // Add dish label and field
        JLabel dishLabel = new JLabel("Dish:");
        dishField = new JTextField();
        centerPanel.add(dishLabel);
        centerPanel.add(dishField);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Create bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // Add add button
        addButton = new JButton("Add Dish");
        addButton.setPreferredSize(new Dimension(120, 30));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tableNumberText = tableNumberField.getText();
                String dishName = dishField.getText();
                if (!tableNumberText.isEmpty() && !dishName.isEmpty()) {
                    try {
                        int tableNumber = Integer.parseInt(tableNumberText);
                        Dish dish = new Dish(tableNumber, dishName);
                        addDish(dish);
                        JOptionPane.showMessageDialog(Restaurant.this, "Dish added successfully!");
                        tableNumberField.setText("");
                        dishField.setText("");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(Restaurant.this, "Invalid table number format!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(Restaurant.this, "Please fill in both table number and dish fields!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        bottomPanel.add(addButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
        setVisible(true);
    }

    public synchronized void addDish(Dish dish) {
        dishes.add(dish);
        notify(); // Notify waiter that there's a new dish
    }

    public synchronized Dish getDish() throws InterruptedException {
        while (dishes.isEmpty()) {
            wait(); // Wait for a dish to be added
        }
        return dishes.remove(0);
    }

    public boolean hasDishes() {
        return !dishes.isEmpty();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Restaurant();
            }
        });
    }
}
