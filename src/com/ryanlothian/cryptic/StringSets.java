package com.ryanlothian.cryptic;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public final class StringSets {
   /** 
    * Returns a string set c such that c.admits(t) iff s.equals(t).
    */
   public static StringSet oneString(final String s) { 
       return new StringSet() {
           @Override
           public boolean admits(String t) {
               return s.equals(t);
           }
       };
   }
   
   /** 
    * Returns a string set for which admits always returns false.
    */
   public static StringSet emptySet() {
       return new StringSet() {
           @Override
           public boolean admits(String t) {
               return false;
           }
       };
   }
   
   public static StringSet intersect(final StringSet setA, final StringSet setB) {
       return new StringSet() {
           @Override
           public boolean admits(String t) {
               return setA.admits(t) && setB.admits(t);
           }
       };
   }

   /**
    * Creates a StringSet c such that c.admits(s) iff there exist strings s1, s2 such that
    * s = s1 + s2 and a.admits(s1) and b.admits(s2).
    */
   public static StringSet concat(final StringSet setA, final StringSet setB) {
       return new StringSet() {
           @Override
           public boolean admits(String t) {
               boolean match = false;
               for (int i = 0; i < t.length() && !match; i++) {
                   match = setA.admits(t.substring(0, i)) && setB.admits(t.substring(i));
               }
               return match;
           }
       };
   }

   public static StringSet union(final List<StringSet> sets) {
       return new StringSet() {
           @Override
           public boolean admits(String t) {
               for (StringSet set : sets) {
                   if (set.admits(t)) {
                       return true;
                   }
               }
               return false;
           }
       };
   }

   public static StringSet of(final Set<String> strings) {
       return new StringSet() {
           @Override
           public boolean admits(String t) {
               return strings.contains(t);
           }
       };
   }
   
   public static StringSet of(String... strings) {
       return of(ImmutableSet.copyOf(strings));
   }

}
