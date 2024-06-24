package visual.listeners;

import objects.Collection;
import visual.ObjectBrowser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateCollectionButtonListener extends MouseAdapter {
    JButton thisButton;

    public CreateCollectionButtonListener(JButton thisButton) {
        this.thisButton = thisButton;
    }

    private JDialog getDialog() {
        JDialog addingCollection = new JDialog((JFrame) null, "Adding Collection");
        addingCollection.setSize(400, 200);
        addingCollection.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel collectionName = new JLabel("*Collection name: ");
        JTextField name = new JTextField(20);

        {
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.LINE_END;
            inputPanel.add(collectionName, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            inputPanel.add(name, gbc);
        }

        JPanel buttonPanel = getButtonPanel(addingCollection, name);

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        addingCollection.add(mainPanel);
        return addingCollection;
    }

    private static JPanel getButtonPanel(JDialog addingCollection, JTextField name) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton create = new JButton("Create");
        create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean error = false;
                String collectionName = name.getText();
                collectionName = collectionName.replaceAll("\\s+", " ").trim();
                if (collectionName.isEmpty()) {
                    JOptionPane.showMessageDialog(addingCollection, "You didn't input the name.", "Error", JOptionPane.ERROR_MESSAGE);
                    error = true;
                }

                if (!error) {
                    Collection newColl = new Collection(collectionName);
                    ObjectBrowser.getInstanceForBrowser().addCollection(newColl);
                }
                addingCollection.dispose();
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addingCollection.dispose();
            }
        });

        buttonPanel.add(create);
        buttonPanel.add(cancel);
        return buttonPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (thisButton.isEnabled()) {
            thisButton.setEnabled(false);
            JDialog addingCollection = getDialog();
            addingCollection.setVisible(true);
            addingCollection.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    thisButton.setEnabled(true);
                }
            });
        }
    }
}
