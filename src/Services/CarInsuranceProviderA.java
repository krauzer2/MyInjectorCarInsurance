package Services;

public class CarInsuranceProviderA implements ICarInsuranceProvider{
    private final ISomeService someService;

    public CarInsuranceProviderA(ISomeService someService){
        this.someService=someService;
    }
}
