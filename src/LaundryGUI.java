import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class LaundryGUI {

    private final LaundryResources resources;

    private JLabel washerLabel;
    private JLabel dryerLabel;
    private JLabel kioskLabel;
    private JLabel servedLabel;
    private JLabel queueLabel;
    private JLabel ownerLabel;

    private JTextArea eventLog;

    public LaundryGUI(LaundryResources resources) {
        this.resources = resources;
    }

    public void show() {
        SwingUtilities.invokeLater(this::buildAndShow);
    }

    private void buildAndShow() {

        JFrame frame = new JFrame("Smart Laundry");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 520);
        frame.setLocationRelativeTo((Component) null);
        frame.setLayout(new BorderLayout(10, 10));

        // Title
        JLabel title = new JLabel(
                "SMART LAUNDRY — CONGESTED SCENARIO",
                JLabel.CENTER
        );

        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(
                BorderFactory.createEmptyBorder(10, 0, 5, 0)
        );

        frame.add(title, BorderLayout.NORTH);

        // Status panel
        JPanel statusPanel = new JPanel(
                new GridLayout(6, 1, 5, 5)
        );

        statusPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Status"),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                )
        );

        washerLabel = new JLabel("Washers:  0 / 6");
        dryerLabel = new JLabel("Dryers:   0 / 4");
        kioskLabel = new JLabel("Kiosks:   0 / 2");
        servedLabel = new JLabel("Customers Served:  0 / 50");
        queueLabel = new JLabel("Payment Queue:     0");
        ownerLabel = new JLabel("Owner Called:      NO");

        Font statusFont = new Font("Monospaced", Font.PLAIN, 14);

        washerLabel.setFont(statusFont);
        dryerLabel.setFont(statusFont);
        kioskLabel.setFont(statusFont);
        servedLabel.setFont(statusFont);
        queueLabel.setFont(statusFont);

        ownerLabel.setFont(
                new Font("Monospaced", Font.BOLD, 14)
        );

        statusPanel.add(washerLabel);
        statusPanel.add(dryerLabel);
        statusPanel.add(kioskLabel);
        statusPanel.add(servedLabel);
        statusPanel.add(queueLabel);
        statusPanel.add(ownerLabel);

        frame.add(statusPanel, BorderLayout.CENTER);

        // Event log
        eventLog = new JTextArea(10, 50);
        eventLog.setEditable(false);
        eventLog.setFont(
                new Font("Monospaced", Font.PLAIN, 12)
        );

        JScrollPane logScrollPane =
                new JScrollPane(eventLog);

        logScrollPane.setBorder(
                BorderFactory.createTitledBorder("Event Log")
        );

        frame.add(logScrollPane, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Refresh GUI every 500 ms
        Timer refreshTimer = new Timer(
                500,
                event -> refreshDisplay()
        );

        refreshTimer.start();
    }

    private void refreshDisplay() {

        washerLabel.setText(
                "Washers:  "
                        + resources.getCurrentWashersInUse()
                        + " / 6"
        );

        dryerLabel.setText(
                "Dryers:   "
                        + resources.getCurrentDryersInUse()
                        + " / 4"
        );

        kioskLabel.setText(
                "Kiosks:   "
                        + resources.getCurrentKiosksInUse()
                        + " / 2"
        );

        servedLabel.setText(
                "Customers Served:  "
                        + resources.getCustomersServed()
                        + " / 50"
        );

        queueLabel.setText(
                "Payment Queue:     "
                        + resources.getPaymentQueueLength()
        );

        if (resources.isOwnerCalled()) {

            ownerLabel.setText("Owner Called:      YES");
            ownerLabel.setForeground(Color.RED);

        } else {

            ownerLabel.setText("Owner Called:      NO");
            ownerLabel.setForeground(Color.BLACK);
        }
    }

    public void appendLog(String message) {

        SwingUtilities.invokeLater(() -> {

            eventLog.append(message + "\n");

            eventLog.setCaretPosition(
                    eventLog.getDocument().getLength()
            );
        });
    }
}

