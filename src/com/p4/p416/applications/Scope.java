package com.p4.p416.applications;

import java.util.concurrent.atomic.AtomicReference;

import com.p4.p416.exceptions.NameCollisionException;

public interface Scope {

    String getScopeName();

    Scope getScope();

    Scope getEnclosingScope();

    void add(Symbol symbol);

    Scope createScope();

    Symbol lookup(Object key);

    Symbol resolve(String symbolName);

    void defineSymbol(AtomicReference<Scope> enclosingScope) throws NameCollisionException;
}
