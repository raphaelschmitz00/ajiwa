package net.roughdesign.ajiwa.bindings;


import net.roughdesign.ajiwa.exceptions.AmbiguousBindingException;

import java.util.HashMap;
import java.util.Map;


public class BindingRegistry {

    private final Map<Class<?>, Object> _boundInstances;
    private final Map<Class<?>, Binding> _dependencies;



    public BindingRegistry() {

        _boundInstances = new HashMap<>();
        _dependencies = new HashMap<>();
    }



    public <T> void addInstance(final Class<T> klass, final T instance) {

        if (containsKey(klass)) throw new AmbiguousBindingException(klass.toString());
        _boundInstances.put(klass, instance);
    }



    public void addBinding(final Class<?> klass, final Binding dependency) {

        if (containsKey(klass)) throw new AmbiguousBindingException(klass.toString());
        _dependencies.put(klass, dependency);
    }



    public Map<Class<?>, Binding> getNonInstanceBindings() {

        return new HashMap<>(_dependencies);
    }



    public boolean containsKey(final Class<?> klass) {

        return _boundInstances.containsKey(klass) || _dependencies.containsKey(klass);
    }
}
