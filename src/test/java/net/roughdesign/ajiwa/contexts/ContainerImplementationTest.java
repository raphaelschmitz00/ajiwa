package net.roughdesign.ajiwa.contexts;

import net.roughdesign.ajiwa.contexts.exampleclasses.*;
import net.roughdesign.ajiwa.exceptions.CircularDependencyException;
import net.roughdesign.ajiwa.exceptions.UnresolvedParameterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


public class ContainerImplementationTest {


    @Test
    public void canAutoResolveNoParameterClass() {

        final ContainerImplementation containerImplementation = new ContainerImplementation();
        final NoParameterClass result = containerImplementation.resolve(NoParameterClass.class);

        Assertions.assertNotNull(result);
    }


    @Test
    public void autoResolveThrowsForUnresolvableDependency() {

        final ContainerImplementation containerImplementation = new ContainerImplementation();

        Assertions.assertThrows(UnresolvedParameterException.class, new Executable() {
            @Override
            public void execute() {
                containerImplementation.resolve(IntParameterClass.class);
            }
        });
    }


    @Test
    public void canAutoResolveClassWithArgumentWithoutParameter() {

        final ContainerImplementation containerImplementation = new ContainerImplementation();
        final AutoResolvableComplexClass result = containerImplementation.resolve(AutoResolvableComplexClass.class);

        Assertions.assertNotNull(result);
    }


    @Test
    public void autoResolveThrowsForCircularDependency() {

        final ContainerImplementation containerImplementation = new ContainerImplementation();

        Assertions.assertThrows(CircularDependencyException.class, new Executable() {
            @Override
            public void execute() {
                containerImplementation.resolve(CircularDependencyA.class);
            }
        });
    }


    @Test
    public void canBindToInstance() {

        final ContainerImplementation containerImplementation = new ContainerImplementation();
        final NoParameterClass instance = new NoParameterClass();
        containerImplementation.bindInstance(NoParameterClass.class, instance);
        containerImplementation.sort();
        final Object result = containerImplementation.resolve(NoParameterClass.class);

        Assertions.assertEquals(instance, result);
    }


    @Test
    public void canBindToImplementingClass() {

        final ContainerImplementation containerImplementation = new ContainerImplementation();
        containerImplementation.bindSingleton(ExampleInterface.class, ClassImplementingInterface.class);
        containerImplementation.sort();
        final ExampleInterface result = containerImplementation.resolve(ExampleInterface.class);

        Assertions.assertEquals(ClassImplementingInterface.class, result.getClass());
    }


    @Test
    public void bindingToImplementingClassIsSingleton() {

        final ContainerImplementation containerImplementation = new ContainerImplementation();
        containerImplementation.bindSingleton(ExampleInterface.class, ClassImplementingInterface.class);
        containerImplementation.sort();
        final ExampleInterface resultA = containerImplementation.resolve(ExampleInterface.class);
        final ExampleInterface resultB = containerImplementation.resolve(ExampleInterface.class);

        Assertions.assertEquals(resultA, resultB);
    }


    @Test
    public void canBindEnum() {

        final ContainerImplementation containerImplementation = new ContainerImplementation();
        containerImplementation.bindInstance(ExampleEnum.class, ExampleEnum.ValueB);
        containerImplementation.sort();
        final ExampleEnum result = containerImplementation.resolve(ExampleEnum.class);

        Assertions.assertEquals(ExampleEnum.ValueB, result);
    }


    @Test
    public void canBindTransient() {

        final ContainerImplementation containerImplementation = new ContainerImplementation();
        containerImplementation.bindTransient(ExampleInterface.class, ClassImplementingInterface.class);
        containerImplementation.sort();
        final ExampleInterface resultA = containerImplementation.resolve(ExampleInterface.class);
        final ExampleInterface resultB = containerImplementation.resolve(ExampleInterface.class);

        Assertions.assertNotEquals(resultA, resultB);
    }


    @Test
    public void canAutoResolveComplexClass() {

        final ContainerImplementation containerImplementation = new ContainerImplementation();
        containerImplementation.bindSingleton(ExampleInterface.class, ClassImplementingInterface.class);
        containerImplementation.bindInstance(IntParameterClass.class, new IntParameterClass(3));
        containerImplementation.sort();
        final ComplexClass result = containerImplementation.resolve(ComplexClass.class);

        Assertions.assertNotNull(result);
    }


    @Test
    public void orderOfBindingIsNotImportant() {

        final ContainerImplementation containerImplementation = new ContainerImplementation();
        containerImplementation.bindSingleton(ClassRelyingOnInterface.class, ClassRelyingOnInterface.class);
        containerImplementation.bindSingleton(ExampleInterface.class, ClassImplementingInterface.class);
        containerImplementation.sort();
        final ExampleInterface result = containerImplementation.resolve(ExampleInterface.class);

        Assertions.assertNotNull(result);
    }


}