package net.roughdesign.ajiwa.bindings;


import net.roughdesign.ajiwa.exceptions.MoreThanOneConstructorException;
import net.roughdesign.ajiwa.exceptions.NoAvailableConstructorException;
import net.roughdesign.ajiwa.exceptions.UnresolvedParameterException;

import java.lang.reflect.Constructor;


public class Binding {

    private final Scope _scope;
    private final Constructor<?> _constructor;
    private final Class<?>[] _parameterTypes;



    private Binding(final Scope scope, final Constructor<?> constructor, final Class<?>[] parameterTypes) {


        _scope = scope;
        _constructor = constructor;
        _parameterTypes = parameterTypes;
    }



    public static Binding create(final Class<?> implementation, final Scope scope) {

        if (implementation.isPrimitive()) throw new UnresolvedParameterException(implementation);
        if (implementation.isInterface()) throw new UnresolvedParameterException(implementation);
        if (implementation.isArray()) throw new UnresolvedParameterException(implementation);
        final Constructor<?> constructor = getConstructor(implementation);
        final Class<?>[] parameterTypes = constructor.getParameterTypes();
        return new Binding(scope, constructor, parameterTypes);
    }



    private static Constructor<?> getConstructor(final Class<?> klass) {

        final Constructor<?>[] constructors = klass.getConstructors();
        if (constructors.length == 0) throw new NoAvailableConstructorException(klass.toString());
        if (constructors.length > 1) throw new MoreThanOneConstructorException(klass.toString());
        return constructors[0];
    }



    public Scope getScope() {

        return _scope;
    }



    public Constructor<?> getConstructor() {

        return _constructor;
    }



    public Class<?>[] getParameterTypes() {

        return _parameterTypes;
    }
}
