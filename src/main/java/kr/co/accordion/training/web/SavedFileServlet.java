package kr.co.accordion.training.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/api/saved")
public class SavedFileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");

        String volumePath = "/tmp/k8s-training";
        String fileName = RuntimeInfo.env("SAVE_FILE_NAME", "k8s-training-info.txt");
        Path target = Paths.get(volumePath).resolve(fileName).normalize();

        if (!Files.exists(target)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("아직 저장된 파일이 없습니다.\n저장 버튼을 눌러 /tmp/k8s-training 경로에 파일을 생성해 보세요.");
            return;
        }

        response.getOutputStream().write(Files.readAllBytes(target));
    }
}
