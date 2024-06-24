package visual;

import objects.Collection;
import objects.Tag;
import static visual.ObjectBrowser.getInstanceForBrowser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class PhotoBrowserGUI {
    public PhotoBrowserGUI() {
        JFrame mainFrame = new JFrame("PhotoBrowser");
        mainFrame.setPreferredSize(new Dimension(1380, 720));
        mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveDataOnClose();  //method to save collections and tags
                mainFrame.dispose();
                System.exit(0);
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel leftPart = new JPanel(new BorderLayout());
        leftPart.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        JPanel rightPart = new JPanel();
        rightPart.setLayout(new BoxLayout(rightPart, BoxLayout.Y_AXIS));
        ObjectViewer viewer = ObjectViewer.getInstanceForViewer();
        viewer.setAlignmentX(Component.CENTER_ALIGNMENT); //vertical alignment
        rightPart.add(Box.createVerticalGlue());
        rightPart.add(viewer);
        rightPart.setPreferredSize(new Dimension(440, mainPanel.getHeight()));

        TopBar topBar = new TopBar();
        topBar.setPreferredSize(new Dimension(leftPart.getWidth(), 50));
        leftPart.add(topBar, BorderLayout.NORTH);

        ObjectBrowser objectBrowser = getInstanceForBrowser();
        leftPart.add(objectBrowser, BorderLayout.CENTER);

        mainPanel.add(leftPart, BorderLayout.CENTER);
        mainPanel.add(rightPart, BorderLayout.EAST);

        mainFrame.add(mainPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);
    }

    private void saveDataOnClose() {
        try {
            ArrayList<Collection> allCollections = ObjectBrowser.getInstanceForBrowser().getCollections();
            ArrayList<Tag> allTags = TagsList.getInstanceOfTagsList().getTagSet();

            try (FileOutputStream fosColl = new FileOutputStream("../PhotoBrowser/src/saves/collections.txt");
                 ObjectOutputStream oosColl = new ObjectOutputStream(fosColl)) {
                oosColl.writeObject(allCollections);
            }

            try (FileOutputStream fosTags = new FileOutputStream("../PhotoBrowser/src/saves/tags.txt");
                 ObjectOutputStream oosTags = new ObjectOutputStream(fosTags)) {
                oosTags.writeObject(allTags);
            }
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
