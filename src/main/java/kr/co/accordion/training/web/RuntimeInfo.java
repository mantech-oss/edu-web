package kr.co.accordion.training.web;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

final class RuntimeInfo {
    private RuntimeInfo() {}

    static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }

    private static String runCommand(String... command) {
        try {
            Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (result.length() > 0) result.append(' ');
                result.append(line.trim());
            }
            process.waitFor();
            return result.toString().trim();
        } catch (Exception e) {
            return "";
        }
    }

    static String hostname() {
        String value = runCommand("hostname");
        if (!value.isEmpty()) return value;

        value = System.getenv("HOSTNAME");
        if (value != null && !value.trim().isEmpty()) return value.trim();

        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "unknown";
        }
    }

    static String podIp() {
        String value = runCommand("hostname", "-I");
        if (!value.isEmpty()) return value;

        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "unknown";
        }
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
