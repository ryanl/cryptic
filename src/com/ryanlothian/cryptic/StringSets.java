package com.ryanlothian.cryptic;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public final class StringSets {
    
    private static final class AnagramStringSet implements StringSet {
        private final int counts[] = new int[26];
        private final int length;
        
        public AnagramStringSet(String s) {
            this.length = s.length();
            for (int i = 0; i < s.length(); i++) {
                this.counts[s.charAt(i) - 'a']++;
            } 
        }
        
        @Override
        public boolean admits(String s) {
            if (this.length != s.length()) return false;
            final int sCounts[] = new int[26];
            for (int i = 0; i < s.length(); i++) {
                sCounts[s.charAt(i) - 'a']++;
            }
            for (int i = 0; i < 26; i++) {
                if (counts[i] != sCounts[i]) return false;
            }
            return true;
        }
    }
    
    private static final class IntersectionStringSet implements StringSet {
        private final StringSet[] sets;
        
        public IntersectionStringSet(StringSet... sets) {
            this.sets = sets;
        }
        
        @Override
        public boolean admits(String s) {
            for (StringSet set : this.sets) {
                if (!set.admits(s)) {
                    return false;
                }
            }
            return true;
        }
    }
    
    private static final class ConcatStringSet implements StringSet {
        private final StringSet setA;
        private final StringSet setB;
        
        public ConcatStringSet(StringSet setA, StringSet setB) {
            this.setA = setA;
            this.setB = setB;
        }
        
        @Override
        public boolean admits(String s) {
            boolean match = false;
            for (int i = 0; i < s.length() && !match; i++) {
                match = this.setA.admits(s.substring(0, i)) 
                        && setB.admits(s.substring(i));
            }
            return match;
        }
    }
    
    private static final class FixedSetStringSet implements StringSet {
        private final ImmutableSet<String> strings;
        
        public FixedSetStringSet(Set<String> strings) {
            this.strings = ImmutableSet.copyOf(strings); 
        }
        
        @Override
        public boolean admits(String s) {
            return this.strings.contains(s);
        }
        
    }
    
    private static final class UnionStringSet implements StringSet {
        private final List<StringSet> sets;
        
        public UnionStringSet(List<StringSet> sets) {
            this.sets = sets;
        }
        
        @Override
        public boolean admits(String s) {
            for (StringSet set : this.sets) {
                if (set.admits(s)) {
                    return true;
                }
            }
            return false;
        }
        
//        public StringSet without(StringSet a) {
//            List<StringSet> newSets = Lists.newArrayList();
//            for (StringSet set : sets) {
//                newSets.add(set.without(a));
//            }
//            return new UnionStringSet(newSets);
//        }
    }

   public static StringSet oneString(String s) { 
       return new FixedSetStringSet(ImmutableSet.of(s));
   }
   
   /** 
    * Returns a string set for which admits always returns false.
    */
   public static StringSet emptySet() {
       return new FixedSetStringSet(ImmutableSet.<String>of());
   }
   
   public static StringSet intersect(StringSet setA, StringSet setB) {
       return new IntersectionStringSet(setA, setB);
   }

   /**
    * Creates a StringSet c such that c.admits(s) iff there exist strings s1, s2 such that
    * s = s1 + s2 and a.admits(s1) and b.admits(s2).
    */
   public static StringSet concat(StringSet setA, StringSet setB) {
       return new ConcatStringSet(setA, setB);
   }

   public static StringSet anagram(final String s) {
       return new AnagramStringSet(s);
   }
   
//   public static StringSet without(StringSet a, StringSet b) {
//       
//   }
   
   public static StringSet union(List<StringSet> sets) {
       return new UnionStringSet(sets);
   }

   public static StringSet of(Set<String> strings) {
       return new FixedSetStringSet(strings);
   }
   
   public static StringSet of(String... strings) {
       return of(ImmutableSet.copyOf(strings));
   }

}
