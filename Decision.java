package model;

import java.util.Objects;

public class Decision {
    private String title;
    private String description;

    public Decision(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Decision(String title) {
        this(title, "");
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return title + (description.isEmpty() ? "" : (" — " + description));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Decision)) return false;
        Decision d = (Decision) o;
        return Objects.equals(title, d.title) && Objects.equals(description, d.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description);
    }
}
