package objects;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Photo implements Serializable {
    private String title;
    private String description;
    private String date;
    private Set<Tag> tags = new HashSet<>();
    private final String image;

    public Photo(String title, String description, String date, String imagePath) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.image = imagePath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public String getImage() {
        return image;
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    @Override
    public String toString() {
        return title;
    }

    public void setTags(Set<Tag> selectedTags) {
        tags = selectedTags;
    }
}