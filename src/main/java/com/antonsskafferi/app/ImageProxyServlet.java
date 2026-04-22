package com.antonsskafferi.app;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@WebServlet("/image")
public class ImageProxyServlet extends HttpServlet {
    private static final HttpClient client = HttpClient.newHttpClient();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getParameter("fileName");
        if (fileName == null || fileName.isBlank()) { resp.sendError(400); return; }
        if (fileName.contains("/") || fileName.contains("..")) { resp.sendError(400); return; }
        try {
            HttpRequest r = HttpRequest.newBuilder(URI.create(
                    API.link + "event/image?fileName=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8)))
                    .GET().build();
            HttpResponse<byte[]> backend = client.send(r, HttpResponse.BodyHandlers.ofByteArray());
            if (backend.statusCode() != 200 || backend.body().length == 0) { resp.sendError(404); return; }
            String lower = fileName.toLowerCase();
            String ct = lower.endsWith(".png") ? "image/png"
                    : lower.endsWith(".gif") ? "image/gif"
                    : lower.endsWith(".webp") ? "image/webp"
                    : "image/jpeg";
            resp.setContentType(ct);
            resp.setContentLength(backend.body().length);
            resp.setHeader("Cache-Control", "public, max-age=3600");
            resp.getOutputStream().write(backend.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            resp.sendError(502);
        }
    }
}
