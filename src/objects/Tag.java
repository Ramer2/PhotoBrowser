package objects;

import java.io.Serializable;

public record Tag(String tag) implements Serializable {

    @Override
    public String toString() {
        return tag;
    }
}
