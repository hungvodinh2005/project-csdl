package models;

public class Medicine {
    private String medicineId;
    private String medicineName;
    private String category;
    private String manufacturer;

    public Medicine() {}

    public Medicine(String medicineId, String medicineName, String category, String manufacturer) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.category = category;
        this.manufacturer = manufacturer;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
