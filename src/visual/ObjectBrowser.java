package visual;

import objects.Photo;
import objects.Collection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ObjectBrowser extends JPanel {
    private static ObjectBrowser singleInstance = null;
    private Collection selectedCollection = null;
    private Collection allPhotos;
    private DefaultListModel<Collection> collectionListModel;
    private DefaultListModel<Photo> photoListModel;
    private JList<Collection> collectionList;
    private JList<Photo> photoList;
    private static ArrayList<Photo> photos;
    private static ArrayList<Collection> collections;

    private ObjectBrowser() {
        initialize();
    }

    private void initialize() {
        setLayout(new GridLayout(2, 1, 10, 0));

        collections = new ArrayList<>();
        if (allPhotos == null) {
            allPhotos = new Collection("All Photos");
        }
        collections.add(allPhotos);
        photos = allPhotos.getContents();

        collectionListModel = new DefaultListModel<>();
        photoListModel = new DefaultListModel<>();

        JLabel collectionsLabel = new JLabel("Collections", JLabel.CENTER);
        JLabel photosLabel = new JLabel("Photos", JLabel.CENTER);

        collectionList = new JList<>(collectionListModel);
        photoList = new JList<>(photoListModel);

        //list selection listeners
        collectionList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Collection selectedCollection = collectionList.getSelectedValue();
                if (selectedCollection != null) {
                    ObjectViewer.getInstanceForViewer().displayCollection(selectedCollection);
                    this.selectedCollection = selectedCollection;
                    photos = selectedCollection.getContents();
                    refreshPhotos();
                }
            }
        });

        photoList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Photo selectedPhoto = photoList.getSelectedValue();
                if (selectedPhoto != null) {
                    ObjectViewer.getInstanceForViewer().displayPhoto(selectedPhoto);
                }
            }
        });

        JScrollPane collectionScrollPane = new JScrollPane(collectionList);
        JScrollPane photoScrollPane = new JScrollPane(photoList);

        JPanel collectionsPanel = new JPanel(new BorderLayout());
        collectionsPanel.add(collectionsLabel, BorderLayout.NORTH);
        collectionsPanel.add(collectionScrollPane, BorderLayout.CENTER);

        JPanel photosPanel = new JPanel(new BorderLayout());
        photosPanel.add(photosLabel, BorderLayout.NORTH);
        photosPanel.add(photoScrollPane, BorderLayout.CENTER);

        add(collectionsPanel);
        add(photosPanel);

        refreshAll();
    }

    public void addPhoto(Photo newPhoto) {
        allPhotos.addPhoto(newPhoto);
        refreshAll();
    }

    public void addCollection(Collection newCollection) {
        collections.add(newCollection);
        refreshCollections();
    }

    //set collections and update allPhotos if necessary
    public void setCollections(ArrayList<Collection> collections) {
        ObjectBrowser.collections = collections;
        boolean allPhotosExists = false;
        for (Collection coll : collections) {
            if (coll.getName().equals(allPhotos.getName())) {
                allPhotosExists = true;
                allPhotos.getContents().addAll(coll.getContents());
                break;
            }
        }
        if (!allPhotosExists) {
            collections.add(allPhotos);
        }
        photos = allPhotos.getContents();
        refreshAll();
    }

    public void refreshPhotos() {
        photoListModel.clear();
        photos.forEach(photoListModel::addElement);
    }

    public void refreshCollections() {
        collectionListModel.clear();
        collections.forEach(collectionListModel::addElement);
    }

    public void refreshAll() {
        refreshCollections();
        refreshPhotos();
    }

    public static ObjectBrowser getInstanceForBrowser() {
        if (singleInstance == null) {
            singleInstance = new ObjectBrowser();
        }
        return singleInstance;
    }

    public ArrayList<Collection> getCollections() {
        return collections;
    }

    public void clearModel() {
        photoListModel.clear();
    }

    public void setAllPhotos(Collection allPhotos) {
        this.allPhotos = allPhotos;
    }

    public Collection getSelectedColl() {
        return selectedCollection;
    }

    public void deleteCollection(Collection collection) {
        collections.remove(collection);
        refreshCollections();
    }
}
