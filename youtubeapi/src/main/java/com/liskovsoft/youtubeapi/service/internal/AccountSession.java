package com.liskovsoft.youtubeapi.service.internal;

import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicLong;

/** Identifies a selection session, including switching away from and back to the same account. */
public final class AccountSession {
    private static final AtomicLong GENERATION = new AtomicLong();

    private AccountSession() {
    }

    public static long getGeneration() {
        return GENERATION.get();
    }

    public static void invalidate() {
        GENERATION.incrementAndGet();
    }

    public static void checkCurrent(long generation) {
        if (generation != getGeneration()) {
            throw new CancellationException("Account changed during history update");
        }
    }

    /** Keeps a cached result bound to the session which started its request. */
    public static final class Value<T> {
        private final long mGeneration;
        private final T mValue;

        public Value(long generation, T value) {
            mGeneration = generation;
            mValue = value;
        }

        public T get() {
            return mGeneration == getGeneration() ? mValue : null;
        }
    }
}
