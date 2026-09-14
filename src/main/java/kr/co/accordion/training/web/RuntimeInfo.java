package kr.co.accordion.training.web;

import javax.servlet.http.HttpServletRequest;

final class RuntimeInfo {
    private RuntimeInfo() {}

    static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }

    static String firstHeader(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        if (value == null) return "";
        int comma = value.indexOf(',');
        return (comma >= 0 ? value.substring(0, comma) : value).trim();
    }

    static String ingressHost(HttpServletRequest request) {
        String value = firstHeader(request, "X-Forwarded-Host");
        if (!value.isEmpty()) return value;
        value = firstHeader(request, "Host");
        return value.isEmpty() ? request.getServerName() : value;
    }

    static String ingressScheme(HttpServletRequest request) {
        String value = firstHeader(request, "X-Forwarded-Proto");
        return value.isEmpty() ? request.getScheme() : value;
    }

    static String ingressUrl(HttpServletRequest request) {
        return ingressScheme(request) + "://" + ingressHost(request);
    }

    static String json(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
