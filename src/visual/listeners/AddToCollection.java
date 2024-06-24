package visual.listeners;

import objects.Collection;
import objects.Photo;
import visual.ObjectBrowser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class AddToCollection extends MouseAdapter {
    private final JButton thisButton;
    private final Photo thisPhoto;

    public AddToCollection(JButton thisButton, Photo thisPhoto) {
        this.thisButton = thisButton;
        this.thisPhoto = thisPhoto;
    }

    private JDialog getDialog() {
        JDialog addingToCollection = new JDialog((JFrame)null, "Adding to Collection");
        addingToCollection.setPreferredSize(new Dimension(300, 200));
        addingToCollection.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel("Choose what collection to move this picture to: ");
        mainPanel.add(label, BorderLayout.NORTH);

        JPanel comboPanel = new JPanel();
        Vector<Collection> choices = new Vector<>(ObjectBrowser.getInstanceForBrowser().getCollections());
        JComboBox<Collection> chooseFrom = new JComboBox<>(choices);
        comboPanel.add(chooseFrom);
        JPanel buttonPanel = getButtonPanel(thisPhoto, chooseFrom, addingToCollection);

        mainPanel.add(comboPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        addingToCollection.add(mainPanel);
        return addingToCollection;
    }

    private JPanel getButtonPanel(Photo thisPhoto, JComboBox<Collection> chooseFrom, JDialog addingToCollection) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton agree = getAgree(thisPhoto, chooseFrom, addingToCollection);
        JButton cancel = new JButton("Cancel");
        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addingToCollection.dispose();
            }
        });
        buttonPanel.add(agree);
        buttonPanel.add(cancel);
        return buttonPanel;
    }

    private static JButton getAgree(Photo thisPhoto, JComboBox<Collection> chooseFrom, JDialog addingToCollection) {
        JButton agree = new JButton("OK");
        agree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Collection choice = (Collection) chooseFrom.getSelectedItem();
                if (choice != null) {
                    Collection current = ObjectBrowser.getInstanceForBrowser().getSelectedColl();
                    current.deletePhoto(thisPhoto);
                    choice.addPhoto(thisPhoto);
                    ObjectBrowser.getInstanceForBrowser().refreshAll();
                }
                addingToCollection.dispose();
            }
        });
        return agree;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (thisButton.isEnabled()) {
            thisButton.setEnabled(false);
            JDialog addingToCollection = getDialog();
            addingToCollection.setVisible(true);
            addingToCollection.pack();
            addingToCollection.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    thisButton.setEnabled(true);
                }
            });
        }
    }
}
