package kr.co.accordion.training.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/info")
public class InfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");

        String json = "{" +
                "\"ingressUrl\":\"" + RuntimeInfo.json(RuntimeInfo.ingressUrl(request)) + "\"," +
                "\"ingressHost\":\"" + RuntimeInfo.json(RuntimeInfo.ingressHost(request)) + "\"," +
                "\"serviceName\":\"" + RuntimeInfo.json(RuntimeInfo.env("SERVICE_NAME", "training-web")) + "\"," +
                "\"servicePort\":\"" + RuntimeInfo.json(RuntimeInfo.env("SERVICE_PORT", "80")) + "\"," +
                "\"podName\":\"" + RuntimeInfo.json(RuntimeInfo.env("POD_NAME", "unknown")) + "\"," +
                "\"podIp\":\"" + RuntimeInfo.json(RuntimeInfo.env("POD_IP", "unknown")) + "\"," +
                "\"namespace\":\"" + RuntimeInfo.json(RuntimeInfo.env("POD_NAMESPACE", "default")) + "\"," +
                "\"nodeName\":\"" + RuntimeInfo.json(RuntimeInfo.env("NODE_NAME", "unknown")) + "\"," +
                "\"volumePath\":\"" + RuntimeInfo.json(RuntimeInfo.env("VOLUME_PATH", "/volume")) + "\"" +
                "}";

        response.getWriter().write(json);
    }
}
