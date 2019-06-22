package net.roughdesign.ajiwa.instanceproviders;

import net.roughdesign.ajiwa.exceptions.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InstanceProviderRegistry {


    private Map<Class<?>, InstanceProvider> instanceProviders;


    public InstanceProviderRegistry() {

        instanceProviders = new HashMap<>();
    }


    public void register(Class<?> klass, InstanceProvider instanceProvider) {
        if (instanceProviders.containsKey(klass)) throw new AmbiguousBindingException(klass.toString());
        instanceProviders.put(klass, instanceProvider);
    }


    public <T> StoredValueProvider<T> createStoredValueProvider(final Class<T> klass,
                                                                ArrayList<Class<?>> requestChain) {

        ArrayList<Class<?>> updatedRequestChain = new ArrayList<>(requestChain);
        updatedRequestChain.add(klass);
        ConstructorCaller<T> constructorCaller = createConstructorCaller(klass, updatedRequestChain);

        T instance = constructorCaller.getInstance();
        return new StoredValueProvider<>(instance);
    }


    public <T> ConstructorCaller<T> createConstructorCaller(Class<T> klass, ArrayList<Class<?>> requestChain) {
        Constructor<T> constructor = getConstructor(klass);
        return createConstructorCaller(constructor, requestChain);
    }


    public <T> InstanceProvider<T> getOrCreateInstanceProvider(Class<T> klass, ArrayList<Class<?>> requestChain) {

        validateRequestChain(requestChain, klass);
        InstanceProvider<T> instanceProvider = getInstanceProvider(klass);
        if (instanceProvider != null) return instanceProvider;

        if (klass.isPrimitive()) throw new UnresolvedParameterException("Can't auto-bind " + klass);
        if (klass.isInterface()) throw new UnresolvedParameterException("Can't auto-bind " + klass);
        if (klass.isArray()) throw new UnresolvedParameterException("Can't auto-bind " + klass);

        StoredValueProvider<T> storedValueProvider = createStoredValueProvider(klass, requestChain);
        instanceProviders.put(klass, storedValueProvider);
        return storedValueProvider;
    }


    private <T> void validateRequestChain(final ArrayList<Class<?>> requestChain, Class<T> klass) {

        if (!requestChain.contains(klass)) return;

        StringBuilder builder = new StringBuilder();
        builder.append("Circular Dependency detected!\n\n");

        for (Class<?> loopKlass : requestChain) {
            builder.append(loopKlass.toString());
            builder.append("\n");
        }
        throw new CircularDependencyException(builder.toString());
    }


    @SuppressWarnings("unchecked")
    private <T> InstanceProvider<T> getInstanceProvider(final Class<T> klass) {

        return instanceProviders.get(klass);
    }


    @SuppressWarnings("unchecked")
    private <T> Constructor<T> getConstructor(final Class<T> klass) {

        Constructor<?>[] constructors = klass.getConstructors();
        if (constructors.length == 0) throw new NoAvailableConstructorException(klass.toString());
        if (constructors.length > 1) throw new MoreThanOneConstructorException(klass.toString());
        return (Constructor<T>) constructors[0];
    }


    private <T> ConstructorCaller<T> createConstructorCaller(final Constructor<T> constructor,
                                                             ArrayList<Class<?>> requestChain) {

        Class<?>[] parameterTypes = constructor.getParameterTypes();

        ArrayList<InstanceProvider> parameterInstanceProviders = new ArrayList<>();
        for (Class<?> parameterType : parameterTypes) {
            InstanceProvider loopInstanceProvider = getOrCreateInstanceProvider(parameterType, requestChain);
            parameterInstanceProviders.add(loopInstanceProvider);
        }

        return new ConstructorCaller<T>(constructor, parameterInstanceProviders);
    }


}
