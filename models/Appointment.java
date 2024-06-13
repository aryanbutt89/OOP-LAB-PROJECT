package application.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Appointment {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty disease;
    private final SimpleStringProperty time;
    private final SimpleStringProperty doctor;
    private final SimpleStringProperty gender;
    private final SimpleStringProperty price;
    private final SimpleStringProperty ward;

    public Appointment(int id, String name, String phone, String disease, String time, String doctor, String gender, String price, String ward) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.disease = new SimpleStringProperty(disease);
        this.time = new SimpleStringProperty(time);
        this.doctor = new SimpleStringProperty(doctor);
        this.gender = new SimpleStringProperty(gender);
        this.price = new SimpleStringProperty(price);
        this.ward = new SimpleStringProperty(ward);
    }

    // Getters
    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getPhone() {
        return phone.get();
    }

    public String getDisease() {
        return disease.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getDoctor() {
        return doctor.get();
    }

    public String getGender() {
        return gender.get();
    }

    public String getPrice() {
        return price.get();
    }

    public String getWard() {
        return ward.get();
    }

    // Property getters
    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public SimpleStringProperty diseaseProperty() {
        return disease;
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public SimpleStringProperty doctorProperty() {
        return doctor;
    }

    public SimpleStringProperty genderProperty() {
        return gender;
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public SimpleStringProperty wardProperty() {
        return ward;
    }

    // Setters
    public void setId(int id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public void setDisease(String disease) {
        this.disease.set(disease);
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public void setDoctor(String doctor) {
        this.doctor.set(doctor);
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public void setWard(String ward) {
        this.ward.set(ward);
    }
}
