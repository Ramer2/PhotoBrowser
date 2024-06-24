package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import visual.listeners.AddButtonListener;
import visual.listeners.CreateCollectionButtonListener;
import main.SearchResult;

public class TopBar extends JPanel {
    public TopBar() {
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        JButton add = new JButton("Add");
        add.addMouseListener(new AddButtonListener(add));
        JButton createCollection = new JButton("Create Collection");
        createCollection.addMouseListener(new CreateCollectionButtonListener(createCollection));
        JButton tags = new JButton("Tags");
        tags.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JDialog tagWindow = TagsList.getInstanceOfTagsList();
                tagWindow.setVisible(true);
            }
        });
        JButton refresh = new JButton("Refresh");
        refresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ObjectBrowser.getInstanceForBrowser().refreshAll();
                ObjectViewer.getInstanceForViewer().refresh();
            }
        });
        buttonPanel.add(tags);
        buttonPanel.add(refresh);
        buttonPanel.add(add);
        buttonPanel.add(createCollection);

        JPanel searchPanel = getSearchPanel();

        this.add(buttonPanel, BorderLayout.EAST);
        this.add(searchPanel, BorderLayout.WEST);
    }

    private static JPanel getSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        JTextField searchBar = new JTextField(20);
        JButton search = new JButton("Search");
        JLabel searchBy = new JLabel("Search by:");
        String[] options = {"Names", "Description", "Tags"};
        JComboBox<String> dropDown = new JComboBox<>(options);
        search.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String modeName = (String) dropDown.getSelectedItem();
                int mode;
                if (modeName.equals("Names")) mode = 1;
                else if (modeName.equals("Description")) mode = 2;
                else mode = 3;
                SearchResult searchResultDialog = new SearchResult(mode, searchBar);
                searchResultDialog.setLocationRelativeTo(null);
                searchResultDialog.setVisible(true);
            }
        });
        searchPanel.add(searchBar);
        searchPanel.add(search);
        searchPanel.add(searchBy);
        searchPanel.add(dropDown);
        return searchPanel;
    }
}
