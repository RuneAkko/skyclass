package com.android.publiccourse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class ExtArrayList<T> extends ArrayList<T> implements Serializable {
    public ExtArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public ExtArrayList() {
    }

    public ExtArrayList(Collection<? extends T> c) {
        super(c);
    }
}