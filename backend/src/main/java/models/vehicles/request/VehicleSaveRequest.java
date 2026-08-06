package models.vehicles.request;

public class VehicleSaveRequest {
    /** dolu ise guncelleme, bos ise yeni kayit */
    public String plate;

    public String policeId;

    public String brand;

    public String model;

    public Integer modelYear;

    /** Otomobil, Motosiklet */
    public String type;

    public Integer kilometers;

    public String lastMaintenanceDate;

    public VehicleSaveRequest() {
    }
}
