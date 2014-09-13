package com.ryanlothian.cryptic;

import java.util.List;

public final class Util {

    /** Equivalent to separator.join(parts) in Python. */ 
    static String join(String separator, List<String> parts) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : parts) {
            if (!first) {
                sb.append(separator);
            }
            first = false;
            sb.append(s);
        }
        return sb.toString();
    }

    // Disallow construction.
    private Util() {}
    
}
