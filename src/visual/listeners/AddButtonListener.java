package visual.listeners;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import objects.Photo;
import visual.ObjectBrowser;

public class AddButtonListener extends MouseAdapter {
    private final JButton thisButton;
    static String[] imagePath = new String[1];

    public AddButtonListener(JButton thisButton) {
        this.thisButton = thisButton;
    }
    private JDialog getDialog() {
        JDialog addingPhoto = new JDialog((JFrame)null, "Adding Photo");
        addingPhoto.setPreferredSize(new Dimension(600, 400));
        addingPhoto.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel addTitle = new JLabel("*Title:");
        JTextField title = new JTextField(25);
        JLabel addDescription = new JLabel("Description:");
        JTextArea description = new JTextArea(5, 25);
        JScrollPane descriptionScrollPane = new JScrollPane(description);
        JLabel addDate = new JLabel("*Date (DD.MM.YYYY):");
        JTextField date = new JTextField(25);
        JLabel selectedFile = new JLabel("Selected file: ");
        JLabel file = new JLabel();
        JLabel addPhoto = new JLabel("*Photo:");
        JButton photo = getPhoto(imagePath, file);

        {
            gbc.gridx = 0;
            gbc.gridy = 0;
            formPanel.add(addTitle, gbc);

            gbc.gridx = 1;
            formPanel.add(title, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            formPanel.add(addDescription, gbc);

            gbc.gridx = 1;
            formPanel.add(descriptionScrollPane, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            formPanel.add(addDate, gbc);

            gbc.gridx = 1;
            formPanel.add(date, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            formPanel.add(addPhoto, gbc);

            gbc.gridx = 1;
            formPanel.add(photo, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            formPanel.add(selectedFile, gbc);
            gbc.gridx = 1;
            formPanel.add(file, gbc);
        }

        mainPanel.add(formPanel, BorderLayout.CENTER);
        JPanel buttonPanel = getButtonPanel(addingPhoto, title, description, date);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        addingPhoto.add(mainPanel);
        addingPhoto.pack();
        addingPhoto.setLocationRelativeTo(null);
        return addingPhoto;
    }

    private JButton getPhoto(String[] imagePath, JLabel file) {
        JButton photo = new JButton("*Choose photo...");
        photo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Photos only", "png", "jpg"));
                int id = fileChooser.showOpenDialog(null);
                if (id == JFileChooser.APPROVE_OPTION) {
                    imagePath[0] = fileChooser.getSelectedFile().getAbsolutePath();
                    file.setText(fileChooser.getSelectedFile().getName());
                }
                fileChooser.setVisible(true);
            }
        });
        return photo;
    }

    private JPanel getButtonPanel(JDialog addingPhoto, JTextField title, JTextArea description, JTextField date) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton addButton = getAddPhotoButton(title, description, date, addingPhoto);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addingPhoto.dispose();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    private JButton getAddPhotoButton(JTextField title, JTextArea description, JTextField date, JDialog addingPhoto) {
        JButton addButton = new JButton("Add");
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String photoTitle = title.getText();
                String photoDescription = description.getText();
                String photoDate = date.getText();
                photoTitle = photoTitle.replaceAll("\\s+", " ").trim();
                photoDescription = photoDescription.replaceAll("\\s+", " ").trim();
                photoDate = photoDate.replaceAll("\\s+", "").trim();
                boolean error = false;

                if (photoTitle.isEmpty() || photoDate.isEmpty()) {
                    JOptionPane.showMessageDialog(addingPhoto, "Title and Date fields should not be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    error = true;
                }

                if (!error && imagePath[0] == null) {
                    JOptionPane.showMessageDialog(addingPhoto, "You didn't select the image.", "Error", JOptionPane.ERROR_MESSAGE);
                    error = true;
                }

                if (!photoDate.matches("\\d{2}\\.\\d{2}\\.\\d{4}") && !error) {
                    JOptionPane.showMessageDialog(addingPhoto, "Date is written in the wrong format.", "Error", JOptionPane.ERROR_MESSAGE);
                    error = true;
                }

                if (!error) {
                    Photo newPhoto = new Photo(photoTitle, photoDescription, photoDate, imagePath[0]);
                    ObjectBrowser.getInstanceForBrowser().addPhoto(newPhoto);
                }
                addingPhoto.dispose();
            }
        });
        return addButton;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (thisButton.isEnabled()) {
            thisButton.setEnabled(false);
            JDialog addingPhoto = getDialog();
            addingPhoto.setVisible(true);
            addingPhoto.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    thisButton.setEnabled(true);
                }
            });
        }
    }
}