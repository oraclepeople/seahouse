package org.hf.concurrent;

import org.hf.concurrent.anotations.GuardedBy;
import org.hf.concurrent.anotations.ThreadSafe;


/**
 * Sequence
 *
 * @author Brian Goetz and Tim Peierls
 */

@ThreadSafe
public class Sequence {
    @GuardedBy("this") private int nextValue;

    public synchronized int getNext() {
        return nextValue++;
    }
}
