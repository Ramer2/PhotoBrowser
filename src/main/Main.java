package main;

import objects.Collection;
import objects.Tag;
import visual.ObjectBrowser;
import visual.PhotoBrowserGUI;
import visual.TagsList;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {
    public static void read() {
        try {
            ArrayList<Collection> allCollections;
            ArrayList<Tag> allTags;

            FileInputStream fisColl = new FileInputStream("../S30422Sysoiev/src/saves/collections.txt");
            ObjectInputStream oisColl = new ObjectInputStream(fisColl);
            FileInputStream fisTags = new FileInputStream("../S30422Sysoiev/src/saves/tags.txt");
            ObjectInputStream oisTags = new ObjectInputStream(fisTags);

            allCollections = (ArrayList<Collection>) oisColl.readObject();
            allTags = (ArrayList<Tag>) oisTags.readObject();

            oisTags.close();
            oisColl.close();

            Iterator<Collection> iterator = allCollections.iterator();
            while (iterator.hasNext()) {
                Collection collection = iterator.next();
                if (collection.getName().equals("All Photos")) {
                    iterator.remove();
                    ObjectBrowser.getInstanceForBrowser().setAllPhotos(collection);
                }
            }

            ObjectBrowser.getInstanceForBrowser().setCollections(allCollections);
            TagsList.getInstanceOfTagsList().setTagSet(allTags);
            ObjectBrowser.getInstanceForBrowser().refreshAll();
            TagsList.getInstanceOfTagsList().refresh();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        read();
        new PhotoBrowserGUI();
    }
}
