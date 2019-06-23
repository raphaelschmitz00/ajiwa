package net.roughdesign.ajiwa.contexts;


import net.roughdesign.ajiwa.exceptions.CircularDependencyException;
import net.roughdesign.ajiwa.exceptions.MoreThanOneConstructorException;
import net.roughdesign.ajiwa.exceptions.NoAvailableConstructorException;
import net.roughdesign.ajiwa.exceptions.UnresolvedParameterException;
import net.roughdesign.ajiwa.instanceproviders.ConstructorCaller;
import net.roughdesign.ajiwa.instanceproviders.InstanceProvider;
import net.roughdesign.ajiwa.instanceproviders.InstanceProviderRegistry;
import net.roughdesign.ajiwa.instanceproviders.StoredValueProvider;

import java.lang.reflect.Constructor;
import java.util.ArrayList;


class AutoResolver {

    private final InstanceProviderRegistry _instanceProviderRegistry;



    AutoResolver(final InstanceProviderRegistry instanceProviderRegistry) {

        _instanceProviderRegistry = instanceProviderRegistry;
    }



    <T> InstanceProvider<T> getOrCreateInstanceProvider(final Class<T> klass, final ArrayList<Class<?>> requestChain) {

        validateRequestChain(requestChain, klass);
        if (_instanceProviderRegistry.contains(klass)) return _instanceProviderRegistry.get(klass);

        if (klass.isPrimitive()) throw new UnresolvedParameterException(klass);
        if (klass.isInterface()) throw new UnresolvedParameterException(klass);
        if (klass.isArray()) throw new UnresolvedParameterException(klass);

        final StoredValueProvider<T> storedValueProvider = createStoredValueProvider(klass, requestChain);
        _instanceProviderRegistry.register(klass, storedValueProvider);
        return storedValueProvider;
    }



    private <T> void validateRequestChain(final ArrayList<Class<?>> requestChain, final Class<T> klass) {

        if (!requestChain.contains(klass)) return;

        final StringBuilder builder = new StringBuilder();
        builder.append("Circular Dependency detected!\n\n");

        for (final Class<?> loopKlass : requestChain) {
            builder.append(loopKlass);
            builder.append("\n");
        }
        throw new CircularDependencyException(builder.toString());
    }



    private <T> StoredValueProvider<T> createStoredValueProvider(final Class<T> klass,
                                                                 final ArrayList<Class<?>> requestChain) {

        final ArrayList<Class<?>> updatedRequestChain = new ArrayList<>(requestChain);
        updatedRequestChain.add(klass);
        final Constructor<T> constructor = getConstructor(klass);
        final ConstructorCaller<T> constructorCaller = createConstructorCaller(constructor, updatedRequestChain);

        final T instance = constructorCaller.getInstance();
        return new StoredValueProvider<>(instance);
    }



    @SuppressWarnings("unchecked")
    private <T> Constructor<T> getConstructor(final Class<T> klass) {

        final Constructor<?>[] constructors = klass.getConstructors();
        if (constructors.length == 0) throw new NoAvailableConstructorException(klass.toString());
        if (constructors.length > 1) throw new MoreThanOneConstructorException(klass.toString());
        return (Constructor<T>) constructors[0];
    }



    private <T> ConstructorCaller<T> createConstructorCaller(final Constructor<T> constructor,
                                                             final ArrayList<Class<?>> requestChain) {

        final Class<?>[] parameterTypes = constructor.getParameterTypes();
        final ArrayList<InstanceProvider> parameterInstanceProviders = new ArrayList<>();
        for (final Class<?> parameterType : parameterTypes) {
            final InstanceProvider loopInstanceProvider = getOrCreateInstanceProvider(parameterType, requestChain);
            parameterInstanceProviders.add(loopInstanceProvider);
        }

        return new ConstructorCaller<>(constructor, parameterInstanceProviders);
    }
}
