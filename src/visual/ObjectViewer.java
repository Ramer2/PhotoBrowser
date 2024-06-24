package visual;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import objects.Collection;
import objects.Photo;
import objects.Tag;
import visual.listeners.AddToCollection;

import javax.swing.*;

public class ObjectViewer extends JPanel {
    private static ObjectViewer single_instance = null;
    private ObjectViewer() {initializer();}

    private void initializer(){
        setVisible(true);
    }

    public void displayPhoto(Photo photo) {
        JPanel displayPhoto = new JPanel();
        displayPhoto.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        displayPhoto.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        displayPhoto.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        JLabel title = new JLabel("Title: ");
        displayPhoto.add(title, gbc);

        gbc.gridx = 1;
        JTextField titleField = new JTextField(photo.getTitle(), 20);
        displayPhoto.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        ImageIcon imageIcon = new ImageIcon(photo.getImage());
        Image scaledImage = getScaledImage(imageIcon.getImage());
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        displayPhoto.add(imageLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        JLabel dateLabel = new JLabel("Date: ");
        displayPhoto.add(dateLabel, gbc);

        gbc.gridx = 1;
        JTextField dateField = new JTextField(photo.getDate(), 20);
        displayPhoto.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel descriptionLabel = new JLabel("Description: ");
        displayPhoto.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        JTextArea descriptionArea = new JTextArea(photo.getDescription(), 3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        displayPhoto.add(scrollPane, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;

        JLabel tags = new JLabel("Tags:");
        displayPhoto.add(tags, gbc);

        gbc.gridy = 4;
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;

        JScrollPane tagsPanel = getTagsPanel(photo, imageLabel);
        displayPhoto.add(tagsPanel, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        JPanel buttonPanel = getButtonPanel(photo, titleField, descriptionArea, dateField, tagsPanel);
        displayPhoto.add(buttonPanel, gbc);

        this.removeAll();
        this.revalidate();
        this.repaint();
        this.add(displayPhoto, BorderLayout.CENTER);
    }

    private static JPanel getButtonPanel(Photo photo, JTextField titleField, JTextArea descriptionArea, JTextField dateField, JScrollPane tagsPanel) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton saveChanges = new JButton("Save Changes");
        saveChanges.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String photoTitle = titleField.getText();
                String photoDescription = descriptionArea.getText();
                String photoDate = dateField.getText();
                photoTitle = photoTitle.replaceAll("\\s+", " ").trim();
                photoDescription = photoDescription.replaceAll("\\s+", " ").trim();
                photoDate = photoDate.replaceAll("\\s+", "").trim();
                boolean error = false;

                if (photoTitle.isEmpty() || photoDate.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Title and Date fields should not be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    error = true;
                }

                if (!photoDate.matches("\\d{2}\\.\\d{2}\\.\\d{4}") && !error) {
                    JOptionPane.showMessageDialog(null, "Date is written in the wrong format.", "Error", JOptionPane.ERROR_MESSAGE);
                    error = true;
                }

                if (!error) {
                    photo.setTitle(photoTitle);
                    photo.setDescription(photoDescription);
                    photo.setDate(photoDate);

                    Set<Tag> selectedTags = new HashSet<>();
                    JViewport viewport = tagsPanel.getViewport();
                    Component[] components = ((JPanel) viewport.getView()).getComponents();
                    for (Component comp : components) {
                        if (comp instanceof JCheckBox checkBox) {
                            if (checkBox.isSelected()) {
                                String tagName = checkBox.getText();
                                Tag tag = new Tag(tagName);
                                selectedTags.add(tag);
                            }
                        }
                    }
                    photo.setTags(selectedTags);

                    ObjectViewer.getInstanceForViewer().refresh();
                    ObjectBrowser.getInstanceForBrowser().refreshAll();
                }
            }
        });
        JButton delete = new JButton("Delete Photo");
        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Collection current = ObjectBrowser.getInstanceForBrowser().getSelectedColl();
                current.deletePhoto(photo);
                ObjectViewer.getInstanceForViewer().refresh();
                ObjectBrowser.getInstanceForBrowser().refreshAll();
            }
        });

        JButton addTo = new JButton("Add to...");
        addTo.addMouseListener(new AddToCollection(addTo, photo));
        buttonPanel.add(saveChanges);
        buttonPanel.add(delete);
        buttonPanel.add(addTo);
        return buttonPanel;
    }

    private Image getScaledImage(Image srcImg) {
        int width = srcImg.getWidth(null);
        int height = srcImg.getHeight(null);

        if (width > height) {
            if (width > 350) {
                height = (height * 350) / width;
                width = 350;
            }
        } else {
            if (height > 350) {
                width = (width * 350) / height;
                height = 350;
            }
        }

        return srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    private JScrollPane getTagsPanel(Photo photo, JLabel imageLabel) {
        JPanel tagsPanel = new JPanel();
        tagsPanel.setLayout(new BoxLayout(tagsPanel, BoxLayout.Y_AXIS));

        ArrayList<Tag> allTags = TagsList.getInstanceOfTagsList().getTagSet();
        for (Tag tag : allTags) {
            JCheckBox checkBox = new JCheckBox(tag.tag());
            boolean found = false;
            for (Tag photoTag : photo.getTags()) {
                if (photoTag.tag().equals(tag.tag())) {
                    found = true;
                    break;
                }
            }
            if (found) {
                checkBox.setSelected(true);
            }
            tagsPanel.add(checkBox);
        }

        int imageHeight = imageLabel.getPreferredSize().height;

        int availableHeight = 1170 - imageHeight;
        int tagsPanelHeight = Math.min(availableHeight, tagsPanel.getPreferredSize().height);

        if (tagsPanelHeight < 100) {
            tagsPanelHeight = 100;
        }

        tagsPanel.setPreferredSize(new Dimension(200, tagsPanelHeight));

        JScrollPane scrollPane = new JScrollPane(tagsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(200, tagsPanelHeight));

        return scrollPane;
    }

    public void displayCollection(Collection collection) {
        JPanel displayCollection = new JPanel();
        displayCollection.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        displayCollection.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel label = new JLabel("Collection name: ");
        displayCollection.add(label, gbc);

        gbc.gridx = 1;
        JTextField collectionName = new JTextField(collection.getName(), 20);
        if (collection.getName().equals("All Photos")) {
            collectionName.setEnabled(false);
        }
        displayCollection.add(collectionName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JPanel buttonPanel = getButtonPanel2(collection, collectionName);
        displayCollection.add(buttonPanel, gbc);

        this.removeAll();
        this.revalidate();
        this.repaint();
        this.add(displayCollection);
    }

    private static JPanel getButtonPanel2(Collection collection, JTextField collectionName) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton saveChanges = getSaveChanges(collection, collectionName);
        JButton delete = new JButton("Delete");
        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!Objects.equals(collection.getName(), "All Photos")) {
                    ObjectBrowser.getInstanceForBrowser().deleteCollection(collection);
                }
                ObjectViewer.getInstanceForViewer().refresh();
            }
        });
        buttonPanel.add(saveChanges);
        buttonPanel.add(delete);
        return buttonPanel;
    }

    private static JButton getSaveChanges(Collection collection, JTextField collectionName) {
        JButton saveChanges = new JButton("Save changes");
        saveChanges.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!collection.getName().equals("All Photos")) {
                    String name = collectionName.getText();
                    name = name.replaceAll("\\s+", " ").trim();
                    collection.setName(name);
                    ObjectBrowser.getInstanceForBrowser().refreshCollections();
                    ObjectViewer.getInstanceForViewer().refresh();
                }
            }
        });
        return saveChanges;
    }

    public static ObjectViewer getInstanceForViewer() {
        if (single_instance == null) {
            single_instance = new ObjectViewer();
        }
        return single_instance;
    }

    public void refresh() {
        ObjectBrowser.getInstanceForBrowser().clearModel();
        this.removeAll();
        this.revalidate();
        this.repaint();
    }
}