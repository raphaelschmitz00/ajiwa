package net.roughdesign.ajiwa.contexts;


public interface Container {

    <T> void bindInstance(Class<T> klass, T instance);

    <TDependency, TImplementation extends TDependency> void bindSingleton(
            Class<TDependency> dependency, Class<TImplementation> implementation);

    <TDependency, TImplementation extends TDependency> void bindTransient(
            Class<TDependency> dependency, Class<TImplementation> implementation);

    <T> T resolve(Class<T> klass);
}
