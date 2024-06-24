package objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Collection implements Serializable {

    private String name;
    private final ArrayList<Photo> contents = new ArrayList<>();

    public Collection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Photo> getContents() {
        return contents;
    }

    public void addPhoto(Photo photo) {
        contents.add(photo);
    }

    public void deletePhoto(Photo photo) {
        contents.remove(photo);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collection that = (Collection) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
