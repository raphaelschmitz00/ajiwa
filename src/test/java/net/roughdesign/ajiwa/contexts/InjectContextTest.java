package net.roughdesign.ajiwa.contexts;

import net.roughdesign.ajiwa.contexts.exampleclasses.*;
import net.roughdesign.ajiwa.exceptions.CircularDependencyException;
import net.roughdesign.ajiwa.exceptions.UnresolvedParameterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


public class InjectContextTest {


    @Test
    public void canAutoResolveNoParameterClass() {

        InjectContext injectContext = new InjectContext();
        NoParameterClass result = injectContext.resolve(NoParameterClass.class);

        Assertions.assertNotNull(result);
    }


    @Test
    public void autoResolveThrowsForUnresolvableDependency() {

        final InjectContext injectContext = new InjectContext();

        Assertions.assertThrows(UnresolvedParameterException.class, new Executable() {
            @Override
            public void execute() {
                injectContext.resolve(IntParameterClass.class);
            }
        });
    }


    @Test
    public void canAutoResolveClassWithArgumentWithoutParameter() {

        InjectContext injectContext = new InjectContext();
        AutoResolvableComplexClass result = injectContext.resolve(AutoResolvableComplexClass.class);

        Assertions.assertNotNull(result);
    }


    @Test
    public void autoResolveThrowsForCircularDependency() {

        final InjectContext injectContext = new InjectContext();

        Assertions.assertThrows(CircularDependencyException.class, new Executable() {
            @Override
            public void execute() {
                injectContext.resolve(CircularDependencyA.class);
            }
        });
    }


    @Test
    public void canBindToInstance() {

        InjectContext injectContext = new InjectContext();
        NoParameterClass instance = new NoParameterClass();
        injectContext.bind(NoParameterClass.class).toInstance(instance);
        Object result = injectContext.resolve(NoParameterClass.class);

        Assertions.assertEquals(instance, result);
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
        injectContext.bind(ExampleEnum.class).toInstance(ExampleEnum.ValueB);
        ExampleEnum result = injectContext.resolve(ExampleEnum.class);

        Assertions.assertEquals(ExampleEnum.ValueB, result);
    }


    @Test
    public void canAutoResolveComplexClass() {

        InjectContext injectContext = new InjectContext();
        injectContext.bind(ExampleInterface.class).To(ClassImplementingInterface.class);
        injectContext.bind(IntParameterClass.class).toInstance(new IntParameterClass(3));
        ComplexClass result = injectContext.resolve(ComplexClass.class);

        Assertions.assertNotNull(result);
    }


}