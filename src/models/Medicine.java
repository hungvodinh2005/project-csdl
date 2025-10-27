package models;

public class Medicine{
    private String medicineID, medicineLoai, medicineNsx, medicineMaDon;
    
    public Medicine(){}
    
    public Medicine(String id,String loai,String madon,String nsx){
        this.medicineID = id;
        this.medicineLoai = loai;
        this.medicineMaDon = madon;
        this.medicineNsx = nsx;
    }

    public String getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID;
    }

    public String getMedicineLoai() {
        return medicineLoai;
    }

    public void setMedicineLoai(String medicineLoai) {
        this.medicineLoai = medicineLoai;
    }

    public String getMedicineNsx() {
        return medicineNsx;
    }

    public void setMedicineNsx(String medicineNsx) {
        this.medicineNsx = medicineNsx;
    }

    public String getMedicineMaDon() {
        return medicineMaDon;
    }

    public void setMedicineMaDon(String medicineMaDon) {
        this.medicineMaDon = medicineMaDon;
    }
    
}
