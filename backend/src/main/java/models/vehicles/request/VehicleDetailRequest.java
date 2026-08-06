package models.vehicles.request;

public class VehicleDetailRequest {
    /** polis id'si ile veya plaka ile sorgulanabilir, ikisinden biri yeterli */
    public String policeId;

    public String plate;

    public VehicleDetailRequest() {
    }
}
