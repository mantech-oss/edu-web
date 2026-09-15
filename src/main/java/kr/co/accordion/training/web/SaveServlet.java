package kr.co.accordion.training.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/api/save")
public class SaveServlet extends HttpServlet {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String volumePath = "/tmp/k8s-training";
        String fileName = RuntimeInfo.env("SAVE_FILE_NAME", "k8s-training-info.txt");
        Path directory = Paths.get(volumePath);
        Path target = directory.resolve(fileName).normalize();

        try {
            Files.createDirectories(directory);
            String content =
                    "Kubernetes Training Runtime Information\n" +
                    "=======================================\n" +
                    "Saved At : " + LocalDateTime.now().format(FORMATTER) + "\n" +
                    "Domain   : " + RuntimeInfo.ingressHost(request) + "\n" +
                    "Pod Name : " + RuntimeInfo.hostname() + "\n" +
                    "Pod IP   : " + RuntimeInfo.podIp() + "\n";

            Files.write(target, content.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);

            response.getWriter().write("{\"success\":true,\"message\":\"저장 완료\",\"file\":\"" + RuntimeInfo.json(target.toString()) + "\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\":false,\"message\":\"" + RuntimeInfo.json(e.getClass().getSimpleName() + ": " + e.getMessage()) + "\"}");
        }
    }
}
