package models;
public class Medicine {
    private String medicineId;   // MaThuoc
    private String prescriptionId; // MaDon
    private String name;         // TenLoai
    private String manufacturer; // NhaSanXuat

    public Medicine() {}

    public Medicine(String medicineId, String prescriptionId, String name, String manufacturer) {
        this.medicineId = medicineId;
        this.prescriptionId = prescriptionId;
        this.name = name;
        this.manufacturer = manufacturer;
    }

    // Getters & Setters
}
