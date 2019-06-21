package net.roughdesign.ajiwa.contexts;

import net.roughdesign.ajiwa.contexts.exampleclasses.*;
import net.roughdesign.ajiwa.exceptions.CircularDependencyException;
import net.roughdesign.ajiwa.exceptions.UnresolvedParameterException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InjectContextTest {


    @Test
    public void canAutoResolveNoParameterClass() {

        InjectContext injectContext = new InjectContext();
        NoParameterClass result = injectContext.resolve(NoParameterClass.class);

        assertNotNull(result);
    }


    @Test(expected = UnresolvedParameterException.class)
    public void autoResolveThrowsForUnresolvableDependency() {

        InjectContext injectContext = new InjectContext();
        injectContext.resolve(IntParameterClass.class);
    }


    @Test
    public void canAutoResolveClassWithArgumentWithoutParameter() {

        InjectContext injectContext = new InjectContext();
        AutoResolvableComplexClass result = injectContext.resolve(AutoResolvableComplexClass.class);

        assertNotNull(result);
    }


    @Test(expected = CircularDependencyException.class)
    public void autoResolveThrowsForCircularDependency() {

        InjectContext injectContext = new InjectContext();
        injectContext.resolve(CircularDependencyA.class);
    }


    @Test
    public void canBindToInstance() {

        InjectContext injectContext = new InjectContext();
        NoParameterClass instance = new NoParameterClass();
        injectContext.bind(NoParameterClass.class).FromInstance(instance);
        Object result = injectContext.resolve(Object.class);

        assertEquals(instance, result);
    }


    @Test
    public void canBindToSelf() {

        InjectContext injectContext = new InjectContext();
        injectContext.bind(NoParameterClass.class).ToSelf();
        NoParameterClass result = injectContext.resolve(NoParameterClass.class);

        assertNotNull(result);
    }


    @Test
    public void canBindToImplementingClass() {

        InjectContext injectContext = new InjectContext();
        injectContext.bind(ExampleInterface.class).To(ClassImplementingInterface.class);
        ExampleInterface result = injectContext.resolve(ExampleInterface.class);

        assertEquals(ClassImplementingInterface.class, result.getClass());
    }


    @Test
    public void canBindEnum() {

        InjectContext injectContext = new InjectContext();
        injectContext.bind(ExampleEnum.class).FromInstance(ExampleEnum.ValueB);
        ExampleEnum result = injectContext.resolve(ExampleEnum.class);

        assertEquals(ExampleEnum.ValueB, result);
    }


    @Test
    public void canAutoResolveComplexClass() {

        InjectContext injectContext = new InjectContext();
        injectContext.bind(ExampleInterface.class).To(ClassImplementingInterface.class);
        injectContext.bind(IntParameterClass.class).FromInstance(new IntParameterClass(3));
        ComplexClass result = injectContext.resolve(ComplexClass.class);

        assertNotNull(result);
    }


}