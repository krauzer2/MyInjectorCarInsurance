import Injectors.MyInjector;
import Services.ICarInsuranceProvider;
import Services.ISomeService;
import Services.MyService;
import Services.OtherService;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        MyInjector injector = new MyInjector();

        final MyService myService = injector.singletonOf(MyService.class);
        final OtherService otherService = injector.oneOf(OtherService.class);
        final List<ICarInsuranceProvider> myShapres = injector.listOf(ICarInsuranceProvider.class);
    }
}

