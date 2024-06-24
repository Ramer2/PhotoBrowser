package visual;

import objects.Collection;
import objects.Photo;
import objects.Tag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TagsList extends JDialog {
    private static TagsList single_instance = null;
    private ArrayList<Tag> tagSet = new ArrayList<>();
    private Tag selected;
    private DefaultListModel<Tag> tagDefaultListModel;

    private TagsList() {
        super((Frame)null, "Manage Tags", true);
        initializer();
    }

    private void initializer() {
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        tagDefaultListModel = new DefaultListModel<>();

        JLabel label = new JLabel("Tags", JLabel.CENTER);
        JList<Tag> tagList = new JList<>(tagDefaultListModel);
        tagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tagList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selected = tagList.getSelectedValue();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tagList);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton addTag = new JButton("Add Tag");
        addTag.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JDialog addingTag = getDialog();
                addingTag.setVisible(true);
            }
        });

        JButton removeTag = getRemoveTag();

        buttonPanel.add(addTag);
        buttonPanel.add(removeTag);

        add(label, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private JDialog getDialog() {
        JDialog addingTag = new JDialog(this, "Adding Tag", true);
        addingTag.setLayout(new BorderLayout(10, 10));
        addingTag.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Enter Tag Name");

        JPanel textPanel = new JPanel();
        JTextField tagName = new JTextField(20);
        textPanel.add(tagName);

        JPanel buttonPanel = getButtonPanel(tagName, addingTag);

        addingTag.add(label, BorderLayout.NORTH);
        addingTag.add(textPanel, BorderLayout.CENTER);
        addingTag.add(buttonPanel, BorderLayout.SOUTH);

        addingTag.pack();
        addingTag.setLocationRelativeTo(this);

        return addingTag;
    }

    private JPanel getButtonPanel(JTextField tagName, JDialog addingTag) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton add = getAdd(tagName, addingTag);

        JButton cancel = new JButton("Cancel");
        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addingTag.dispose();
            }
        });

        buttonPanel.add(add);
        buttonPanel.add(cancel);

        return buttonPanel;
    }

    private JButton getAdd(JTextField tagName, JDialog addingTag) {
        JButton add = new JButton("Add");
        add.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String name = tagName.getText().trim();
                if (!name.isEmpty()) {
                    Tag newTag = new Tag(name);
                    tagSet.add(newTag);
                    refresh();
                    addingTag.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a tag name.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return add;
    }

    private JButton getRemoveTag() {
        JButton removeTag = new JButton("Remove Tag");
        removeTag.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selected == null) {
                    JOptionPane.showMessageDialog(null, "Please select a tag to remove.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    ArrayList<Collection> collections = ObjectBrowser.getInstanceForBrowser().getCollections();
                    tagSet.remove(selected);
                    for (Collection collection : collections) {
                        for (Photo photo : collection.getContents()) {
                            photo.removeTag(selected);
                        }
                    }
                    refresh();
                }
            }
        });
        return removeTag;
    }

    public void setTagSet(ArrayList<Tag> tagSet) {
        this.tagSet = tagSet;
    }

    public ArrayList<Tag> getTagSet() {
        return tagSet;
    }

    public void refresh() {
        tagDefaultListModel.clear();
        for (Tag tag : tagSet) {
            tagDefaultListModel.addElement(tag);
        }
    }

    public static TagsList getInstanceOfTagsList() {
        if (single_instance == null) {
            single_instance = new TagsList();
        }
        return single_instance;
    }
}
