package de.flower.common.util.xstream;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import de.flower.common.util.Clazz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

/**
 * Try threadlocal.
 *
 * @author flowerrrr
 */
public class ObjectSerializationListener implements IObjectSerializationListener {

    private final static Logger log = LoggerFactory.getLogger(ObjectSerializationListener.class);

    public static class Context {

        public Multiset<Class<?>> typeSet = TreeMultiset.create(new Comparator<Class<?>>() {
            @Override
            public int compare(final Class<?> o1, final Class<?> o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    private static ThreadLocal<Context> threadLocal = new ThreadLocal<Context>();

    @Override
    public void notify(final Object object) {
        Context context = getContext();
        Class<?> clazz = object.getClass();
        if (Clazz.isAnonymousInnerClass(clazz)) {
            clazz = Clazz.getSuperClass(clazz);
        }
        context.typeSet.add(clazz);
    }

    public void reset() {
        threadLocal.set(new Context());
    }

    public static Context getContext() {
        Context context = threadLocal.get();
        if (context == null) {
            throw new IllegalStateException("Must call #reset first.");
        }
        return context;
    }

}
