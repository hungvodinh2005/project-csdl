package models;

public class Service {
    private String serviceId;
    private String serviceName;
    private double price;

    public Service() {}

    public Service(String serviceId, String serviceName, double price) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
