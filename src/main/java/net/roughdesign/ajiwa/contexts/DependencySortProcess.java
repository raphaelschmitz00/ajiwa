package net.roughdesign.ajiwa.contexts;


import net.roughdesign.ajiwa.bindings.Binding;
import net.roughdesign.ajiwa.bindings.BindingRegistry;
import net.roughdesign.ajiwa.bindings.Scope;
import net.roughdesign.ajiwa.exceptions.UnresolvedParameterException;
import net.roughdesign.ajiwa.instanceproviders.ConstructorCaller;
import net.roughdesign.ajiwa.instanceproviders.InstanceProvider;
import net.roughdesign.ajiwa.instanceproviders.InstanceProviderRegistry;
import net.roughdesign.ajiwa.instanceproviders.StoredValueProvider;

import java.lang.reflect.Constructor;
import java.util.*;


class DependencySortProcess {


    private final BindingRegistry _bindingRegistry;
    private final InstanceProviderRegistry _instanceProviderRegistry;



    DependencySortProcess(final BindingRegistry bindingRegistry,
                          final InstanceProviderRegistry instanceProviderRegistry) {

        _bindingRegistry = bindingRegistry;
        _instanceProviderRegistry = instanceProviderRegistry;
    }



    void sort() {


        final Map<Class<?>, Binding> bindingsToAdd = new HashMap<>(_bindingRegistry.getNonInstanceBindings());

        while (!bindingsToAdd.isEmpty()) {
            boolean progressHasBeenMade = false;

            final Set<Class<?>> keys = new HashSet<>(bindingsToAdd.keySet());
            for (final Class<?> klass : keys) {
                final Binding binding = bindingsToAdd.get(klass);
                if (!bindingCanBeResolvedRightNow(binding)) continue;
                addInstanceProvider(klass, binding);
                bindingsToAdd.remove(klass);
                progressHasBeenMade = true;
            }

            if (progressHasBeenMade) continue;
            throw new UnresolvedParameterException(keys.iterator().next());
        }
    }



    private boolean bindingCanBeResolvedRightNow(final Binding binding) {

        for (final Class<?> parameterType : binding.getParameterTypes()) {
            if (!_instanceProviderRegistry.contains(parameterType)) return false;
        }
        return true;
    }



    private <T> void addInstanceProvider(final Class<T> klass, final Binding binding) {

        final Constructor<?> constructor = binding.getConstructor();
        final Class<?>[] parameterTypes = binding.getParameterTypes();

        final ArrayList<InstanceProvider> parameterInstanceProviders = new ArrayList<>();
        for (final Class<?> parameterClass : parameterTypes) {
            final InstanceProvider<?> instanceProvider = _instanceProviderRegistry.get(parameterClass);
            parameterInstanceProviders.add(instanceProvider);
        }

        final ConstructorCaller constructorCaller = new ConstructorCaller(constructor, parameterInstanceProviders);
        if (binding.getScope() == Scope.Transient) {
            _instanceProviderRegistry.register(klass, constructorCaller);
            return;
        }

        final Object instance = constructorCaller.getInstance();
        final StoredValueProvider storedValueProvider = new StoredValueProvider(instance);
        _instanceProviderRegistry.register(klass, storedValueProvider);
    }
}
