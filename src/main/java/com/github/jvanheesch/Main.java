package com.github.jvanheesch;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings("UseOfObsoleteCollectionType")
public class Main {
    private static final String DIRECTORY = "/home/x/Documents";

    public static void main(String[] args) throws NamingException {
        Hashtable<String, String> hashtableEnvironment = new Hashtable<>();
        hashtableEnvironment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
        hashtableEnvironment.put(Context.PROVIDER_URL, "file://" + DIRECTORY);
        Context context = new InitialContext(hashtableEnvironment);

        NamingEnumeration<Binding> namingenumeration = context.listBindings("");

        enumerationAsStream(namingenumeration)
                .forEach(binding -> System.out.println(binding.getName() + " " + binding.getObject()));

        context.close();
    }

    /**
     * https://stackoverflow.com/a/23276455
     * TODO: java9: Enumeration.asIterator() - https://stackoverflow.com/a/32627991
     */
    public static <T> Stream<T> enumerationAsStream(Enumeration<T> e) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new Iterator<T>() {
                            public T next() {
                                return e.nextElement();
                            }

                            public boolean hasNext() {
                                return e.hasMoreElements();
                            }
                        },
                        Spliterator.ORDERED), false);
    }
}