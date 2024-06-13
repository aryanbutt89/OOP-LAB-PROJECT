package application.models;

public class Patient {
    private String name;
    private String phone;
    private String disease;
    private String time;
    private String doctor;
    private String gender;

    // Constructor for loading basic patient details
    public Patient(String name, String disease) {
        this.name = name;
        this.disease = disease;
    }

    // Constructor for loading full appointment details
    public Patient(String name, String phone, String disease, String time, String doctor, String gender) {
        this.name = name;
        this.phone = phone;
        this.disease = disease;
        this.time = time;
        this.doctor = doctor;
        this.gender = gender;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDisease() {
        return disease;
    }

    public String getTime() {
        return time;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getGender() {
        return gender;
    }
}
