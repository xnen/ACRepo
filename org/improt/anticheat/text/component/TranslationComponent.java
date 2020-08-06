package org.improt.anticheat.text.component;

import com.sun.istack.internal.NotNull;

import java.lang.reflect.ParameterizedType;

public abstract class TranslationComponent<T> {
    private String translationKey;

    TranslationComponent(String key) {
        this.translationKey = key;
    }

    public String format(@NotNull String s, T o) {
        return s.replaceAll("%(?i)" + this.translationKey + "%", this.convert(o));
    }

    public boolean typeMatches(Object o) {
        return ((Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).isInstance(o);
    }

    abstract String convert(T object);
}
