package net.bluept.forceitembattle.util;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("unused")
public class SafeArrayList<E> extends ArrayList {
    public SafeArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public SafeArrayList() {
        super();
    }

    public SafeArrayList(Collection<? extends E> c) {
        super(c);
    }

    public Object get(int index) {
        try {
            return super.get(index);
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    public Object getUnsafe(int index) {
        return super.get(index);
    }
}
