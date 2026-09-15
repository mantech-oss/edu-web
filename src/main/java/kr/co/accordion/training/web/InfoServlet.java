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
                "\"domain\":\"" + RuntimeInfo.json(RuntimeInfo.ingressHost(request)) + "\"," +
                "\"podName\":\"" + RuntimeInfo.json(RuntimeInfo.hostname()) + "\"," +
                "\"podIp\":\"" + RuntimeInfo.json(RuntimeInfo.podIp()) + "\"," +
                "\"volumePath\":\"" + RuntimeInfo.json("/tmp/k8s-training") + "\"" +
                "}";

        response.getWriter().write(json);
    }
}
