package com.ryanlothian.cryptic;

import java.util.List;

import com.google.common.collect.ImmutableSet;

public final class StringSet {
    private final ImmutableSet<String> strings;
    
    /** 
     * Returns a string set c such that c.admits(s) iff s.equals("").
     */
    public static StringSet epsilon() { 
        return new StringSet(ImmutableSet.of(""));
    }
    
    /** 
     * Returns a string set c such that c.admits(t) iff s.equals(t).
     */
    public static StringSet oneString(String s) { 
        return new StringSet(ImmutableSet.of(s));
    }
    
    /** 
     * Returns a string set for which admits always returns false.
     */
    public static StringSet emptySet() {
        return new StringSet(ImmutableSet.<String>of());
    }
    
    /**
     * Creates a StringSet c such that c.admits(s) iff there exist strings s1, s2 such that
     * s = s1 + s2 and a.admits(s1) and b.admits(s2).
     */
    public static StringSet concat(StringSet setA, StringSet setB) {
        ImmutableSet.Builder<String> strings = ImmutableSet.builder();
        for (String a : setA.strings) {
            for (String b : setB.strings) {
                strings.add(a + b);
            }
        }
        return new StringSet(strings.build());
    }
    
    /**
     * Creates a StringSet c such that c.admits(s) iff a.admits(s) or b.admits(s).
     */
    public static StringSet union(StringSet setA, StringSet setB) {
        ImmutableSet.Builder<String> strings = ImmutableSet.builder();
        strings.addAll(setA.strings);
        strings.addAll(setB.strings);
        return new StringSet(strings.build());
    }

    public static StringSet union(List<StringSet> stringSets) {
        ImmutableSet.Builder<String> strings = ImmutableSet.builder();
        for (StringSet set : stringSets) {
            strings.addAll(set.strings);
        }
        return new StringSet(strings.build());
    }
    
    public static StringSet of(ImmutableSet<String> strings) {
        return new StringSet(strings);
    }
    
    private StringSet(ImmutableSet<String> strings) {
        this.strings = strings;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String string : this.strings) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(string);
        }
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof StringSet)) return false;
        StringSet other = (StringSet) obj;
        return this.strings.equals(other.strings);
    }
}
