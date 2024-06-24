package main;

import objects.Collection;
import objects.Tag;
import visual.ObjectBrowser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SearchResult extends JDialog {
    private final JTextField searchField;
    private final JTextArea resultArea;
    private final int mode;
    // 1 - by names
    // 2 - by description
    // 3 - by tags
    public SearchResult(int mode, JTextField searchField) {
        this.mode = mode;
        this.searchField = new JTextField(searchField.getText(), 20);
        this.resultArea = new JTextArea(10, 20);
        this.resultArea.setEditable(false);
        this.resultArea.setLineWrap(true);
        this.resultArea.setWrapStyleWord(true);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(300, 400));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel searchLabel = new JLabel("Search field: ");
        add(searchLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(searchField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        JPanel buttonPanel = getButtonPanel();
        add(buttonPanel, gbc);

        pack();
    }

    private JPanel getButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton searchButton = getSearchButton();
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        buttonPanel.add(searchButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    private JButton getSearchButton() {
        JButton searchButton = new JButton("Search");
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                StringBuilder result = new StringBuilder();
                String check = searchField.getText();
                ArrayList<Collection> collections = ObjectBrowser.getInstanceForBrowser().getCollections();
                if (mode == 1) {
                    String checkLowerCase = check.toLowerCase();
                    collections.stream()
                            .flatMap(collection -> collection.getContents().stream()
                                    .filter(photo -> photo.getTitle().toLowerCase().contains(checkLowerCase))
                                    .map(photo -> collection.getName() + "." + photo.getTitle() + "\n"))
                            .forEach(result::append);
                } else if (mode == 2) {
                    String checkLowerCase = check.toLowerCase();
                    collections.stream()
                            .flatMap(collection -> collection.getContents().stream()
                                    .filter(photo -> photo.getDescription().toLowerCase().contains(checkLowerCase))
                                    .map(photo -> collection.getName() + "." + photo.getTitle() + "\n"))
                            .forEach(result::append);
                } else {
                    boolean isAndSearch = check.contains(" AND ");
                    List<String> tags = Arrays.stream(check.split(isAndSearch ? " AND " : " OR "))
                            .map(String::trim)
                            .toList();

                    collections.forEach(collection -> {
                        collection.getContents().forEach(photo -> {
                            Set<String> photoTagNames = photo.getTags().stream()
                                    .map(Tag::tag)
                                    .map(String::toLowerCase)
                                    .collect(Collectors.toSet());

                            boolean matches;
                            if (isAndSearch) {
                                matches = photoTagNames.containsAll(tags);
                            } else {
                                matches = tags.stream().anyMatch(photoTagNames::contains);
                            }

                            if (matches) {
                                result.append(collection.getName())
                                        .append(".")
                                        .append(photo.getTitle())
                                        .append("\n");
                            }
                        });
                    });
                }
                resultArea.setText(result.toString());
            }
        });
        return searchButton;
    }
}
