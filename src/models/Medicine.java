package models;

public class Medicine {
    private String medicineId;
    private String medicineName;
    private String unit;
    private int quantity;
    private double price;

    public Medicine() {}

    public Medicine(String medicineId, String medicineName, String unit, int quantity, double price) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.unit = unit;
        this.quantity = quantity;
        this.price = price;
    }

    public String getMedicineId() { return medicineId; }
    public void setMedicineId(String medicineId) { this.medicineId = medicineId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
