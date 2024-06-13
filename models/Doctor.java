package application.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Doctor {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;

    public Doctor(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    // Getter for id property
    public int getId() {
        return id.get();
    }

    // Setter for id property
    public void setId(int id) {
        this.id.set(id);
    }

    // Getter for name property
    public String getName() {
        return name.get();
    }

    // Setter for name property
    public void setName(String name) {
        this.name.set(name);
    }

    // Getter for id property (JavaFX)
    public SimpleIntegerProperty idProperty() {
        return id;
    }

    // Getter for name property (JavaFX)
    public SimpleStringProperty nameProperty() {
        return name;
    }
}
