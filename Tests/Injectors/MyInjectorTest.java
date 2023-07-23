package Injectors;

import Services.*;
import org.junit.Assert;

import static org.junit.Assert.*;

public class MyInjectorTest {
    MyInjector injector = new MyInjector();

    @org.junit.Test
    public void singletonOf() throws Exception {
        var obj1 = injector.singletonOf(MyService.class);
        var obj2 = injector.singletonOf(MyService.class);

        Assert.assertEquals(obj1,obj2);
    }

    @org.junit.Test
    public void oneOf() throws Exception {
        var obj1 = injector.oneOf(OtherService.class);
        var obj2 = injector.oneOf(OtherService.class);

        Assert.assertNotEquals(obj1,obj2);

        var obj3 = injector.oneOf(ISomeService.class);
        var obj4 = injector.oneOf(ISomeService.class);

        Assert.assertNotEquals(obj3,obj4);
    }

    @org.junit.Test
    public void listOf() throws Exception {
        Assert.assertEquals(injector.listOf(ICarInsuranceProvider.class).size(), 3);
        Assert.assertEquals(injector.listOf(ISomeService.class).size(), 1);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void listOfException() throws Exception {
        Assert.assertEquals(injector.listOf(SomeService.class).size(), 3);
    }
}