package net.roughdesign.ajiwa.contexts;

import net.roughdesign.ajiwa.contexts.exampleclasses.*;
import net.roughdesign.ajiwa.exceptions.CircularDependencyException;
import net.roughdesign.ajiwa.exceptions.UnresolvedParameterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class InjectContextTest {


    @Test
    public void canAutoResolveNoParameterClass() {

        InjectContext injectContext = new InjectContext();
        NoParameterClass result = injectContext.resolve(NoParameterClass.class);

        Assertions.assertNotNull(result);
    }


    @Test
    public void autoResolveThrowsForUnresolvableDependency() {

        InjectContext injectContext = new InjectContext();

        Assertions.assertThrows(UnresolvedParameterException.class,
                () -> injectContext.resolve(IntParameterClass.class));

    }


    @Test
    public void canAutoResolveClassWithArgumentWithoutParameter() {

        InjectContext injectContext = new InjectContext();
        AutoResolvableComplexClass result = injectContext.resolve(AutoResolvableComplexClass.class);

        Assertions.assertNotNull(result);
    }


    @Test
    public void autoResolveThrowsForCircularDependency() {

        InjectContext injectContext = new InjectContext();

        Assertions.assertThrows(CircularDependencyException.class,
                () -> injectContext.resolve(CircularDependencyA.class));

    }


    @Test
    public void canBindToInstance() {

        InjectContext injectContext = new InjectContext();
        NoParameterClass instance = new NoParameterClass();
        injectContext.bind(NoParameterClass.class).FromInstance(instance);
        Object result = injectContext.resolve(Object.class);

        Assertions.assertEquals(instance, result);
    }


    @Test
    public void canBindToSelf() {

        InjectContext injectContext = new InjectContext();
        injectContext.bind(NoParameterClass.class).ToSelf();
        NoParameterClass result = injectContext.resolve(NoParameterClass.class);

        Assertions.assertNotNull(result);
    }


    @Test
    public void canBindToImplementingClass() {

        InjectContext injectContext = new InjectContext();
        injectContext.bind(ExampleInterface.class).To(ClassImplementingInterface.class);
        ExampleInterface result = injectContext.resolve(ExampleInterface.class);

        Assertions.assertEquals(ClassImplementingInterface.class, result.getClass());
    }


    @Test
    public void canBindEnum() {

        InjectContext injectContext = new InjectContext();
        injectContext.bind(ExampleEnum.class).FromInstance(ExampleEnum.ValueB);
        ExampleEnum result = injectContext.resolve(ExampleEnum.class);

        Assertions.assertEquals(ExampleEnum.ValueB, result);
    }


    @Test
    public void canAutoResolveComplexClass() {

        InjectContext injectContext = new InjectContext();
        injectContext.bind(ExampleInterface.class).To(ClassImplementingInterface.class);
        injectContext.bind(IntParameterClass.class).FromInstance(new IntParameterClass(3));
        ComplexClass result = injectContext.resolve(ComplexClass.class);

        Assertions.assertNotNull(result);
    }


}