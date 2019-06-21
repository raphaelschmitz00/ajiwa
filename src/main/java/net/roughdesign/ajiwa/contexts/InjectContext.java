package net.roughdesign.ajiwa.contexts;

import net.roughdesign.ajiwa.bindings.BindingHolder;
import net.roughdesign.ajiwa.exceptions.MoreThanOneConstructorException;
import net.roughdesign.ajiwa.exceptions.NoAvailableConstructorException;
import net.roughdesign.ajiwa.exceptions.UnresolvedParameterException;
import net.roughdesign.ajiwa.instanceproviders.ConstructorCaller;
import net.roughdesign.ajiwa.instanceproviders.InstanceProvider;
import net.roughdesign.ajiwa.instanceproviders.StoredValueProvider;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class InjectContext {

    private Map<Class<?>, InstanceProvider> instanceProviders;


    public InjectContext() {
        instanceProviders = new HashMap<>();

    }


    public <T> BindingHolder<T> bind(Class<T> klass) {

        //ComposingResolveProvider<T> resolveProvider = new ComposingResolveProvider<T>(klass);
        //BindingHolder<T> bindingHolder = new BindingHolder<>(klass, resolveProvider);

        //bindingHolderDictionary.put(klass, bindingHolder);

        return null;
    }


    public <T> T resolve(Class<T> klass) {

        InstanceProvider<T> instanceProvider = resolveRequest(klass);
        return instanceProvider.getInstance();
    }


    private Stack<Class<?>> requestChain;


     private <T> InstanceProvider<T> resolveRequest(Class<T> klass) {

        InstanceProvider<T> instanceProvider = instanceProviders.get(klass);
        if (instanceProvider != null) return instanceProvider;

        if (klass.isPrimitive()) throw new UnresolvedParameterException("Can't auto-bind " + klass);
        if (klass.isInterface()) throw new UnresolvedParameterException("Can't auto-bind " + klass);
        if (klass.isArray()) throw new UnresolvedParameterException("Can't auto-bind " + klass);

        Constructor<?>[] constructors = klass.getConstructors();
        if (constructors.length == 0) throw new NoAvailableConstructorException(klass.toString());
        if (constructors.length > 1) throw new MoreThanOneConstructorException(klass.toString());

        ConstructorCaller<T> constructorCaller = createConstructorCaller(constructors[0]);
        T instance = constructorCaller.getInstance();
        StoredValueProvider<T> storedValueProvider = new StoredValueProvider<>(instance);

        instanceProviders.put(klass, storedValueProvider);


        return storedValueProvider;
    }


    @SuppressWarnings("unchecked")
    private <T> ConstructorCaller<T> createConstructorCaller(final Constructor<?> constructor) {

        Constructor<T> theOneConstructor = (Constructor<T>) constructor;
        Class<?>[] parameterTypes = theOneConstructor.getParameterTypes();

        ArrayList<InstanceProvider> parameterInstanceProviders = new ArrayList<>();
        for (Class<?> parameterType : parameterTypes) {
            InstanceProvider loopInstanceProvider = resolveRequest(parameterType);
            parameterInstanceProviders.add(loopInstanceProvider);
        }

        return new ConstructorCaller<>(theOneConstructor, parameterInstanceProviders);
    }

}
