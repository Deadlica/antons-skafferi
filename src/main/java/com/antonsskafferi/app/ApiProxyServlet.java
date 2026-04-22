package com.antonsskafferi.app;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WebServlet("/api-proxy/*")
public class ApiProxyServlet extends HttpServlet {
    private static final HttpClient client = HttpClient.newHttpClient();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.isBlank() || path.contains("..")) { resp.sendError(400); return; }
        String qs = req.getQueryString();
        String url = API.link + path.substring(1) + (qs != null ? "?" + qs : "");
        try {
            HttpResponse<byte[]> backend = client.send(
                    HttpRequest.newBuilder(URI.create(url)).GET().build(),
                    HttpResponse.BodyHandlers.ofByteArray());
            resp.setStatus(backend.statusCode());
            backend.headers().firstValue("Content-Type").ifPresent(resp::setContentType);
            resp.getOutputStream().write(backend.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            resp.sendError(502);
        }
    }
}
