package com.antonsskafferi.app;

public class URL {
    private static final String HOST = firstNonBlank(System.getenv("BACKEND_HOST"), "localhost");

    public URL() {}

    public String getLink() { return HOST; }

    private static String firstNonBlank(String a, String b) {
        return (a != null && !a.isBlank()) ? a : b;
    }
}
