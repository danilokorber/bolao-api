package io.easyware.shared;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public interface Slug {

    static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public default String slugify(String string) {
        String nowhitespace = WHITESPACE.matcher(string).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    public final String BASE_URL = "/api/v1";
}
