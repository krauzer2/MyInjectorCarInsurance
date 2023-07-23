package Services;

public class CarInsuranceProviderB implements ICarInsuranceProvider{
    private final CarInsuranceProviderC carInsuranceProviderC;

    public CarInsuranceProviderB(CarInsuranceProviderC carInsuranceProviderC){
        this.carInsuranceProviderC=carInsuranceProviderC;
    }
}
