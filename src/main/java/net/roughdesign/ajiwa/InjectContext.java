package net.roughdesign.ajiwa;


import net.roughdesign.ajiwa.contexts.Container;
import net.roughdesign.ajiwa.contexts.ContainerImplementation;


public abstract class InjectContext {

    private final ContainerImplementation _containerImplementation;



    public InjectContext() {

        _containerImplementation = new ContainerImplementation();
        addDependencies(_containerImplementation);
        _containerImplementation.sort();
    }



    public <T> T resolve(final Class<T> klass) {

        return _containerImplementation.resolve(klass);
    }



    protected abstract void addDependencies(Container container);
}
