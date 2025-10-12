package web.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.beans.ResultBean;
import web.models.Point;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@WebServlet("/checkArea")
public class AreaCheckServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LocalDateTime startTime = LocalDateTime.now();

        try {
            String xParam = req.getParameter("x");
            String yParam = req.getParameter("y");
            String rParam = req.getParameter("r");

            if (xParam == null || yParam == null || rParam == null ||
                    xParam.isEmpty() || yParam.isEmpty() || rParam.isEmpty()) {
                sendErrorResponse(resp, "Пустые параметры запроса");
                return;
            }

            double x = Double.parseDouble(xParam);
            double y = Double.parseDouble(yParam);
            double r = Double.parseDouble(rParam);

            // Проверка диапазонов
            if (!isValidX(x) || !isValidY(y) || !isValidR(r)) {
                sendErrorResponse(resp, "Параметры вне допустимого диапазона");
                return;
            }

            Point point = new Point(x, y, r);
            boolean isInside = point.checkInside(x, y, r); // Используем метод проверки
            point.setInside(isInside); // Устанавливаем результат

            LocalDateTime endTime = LocalDateTime.now();
            point.setExecutionTime(ChronoUnit.MILLIS.between(startTime, endTime));

            // Добавляем результат в историю
            ResultBean resultBean = (ResultBean) req.getSession().getAttribute("results");
            if (resultBean == null) {
                resultBean = new ResultBean();
                req.getSession().setAttribute("results", resultBean);
            }
            resultBean.addPoint(point);

            // Перенаправляем на страницу результатов
            resp.sendRedirect("result.jsp");

        } catch (NumberFormatException e) {
            sendErrorResponse(resp, "Неверный формат параметров");
        } catch (Exception e) {
            sendErrorResponse(resp, "Внутренняя ошибка сервера");
        }
    }

    private boolean isValidX(double x) {
        return x >= -5 && x <= 5;
    }

    private boolean isValidY(double y) {
        return y >= -5 && y <= 5;
    }

    // R оставляем дискретным
    private boolean isValidR(double r) {
        double[] allowedR = {1, 2, 3, 4, 5};
        for (double value : allowedR) {
            if (Math.abs(r - value) < 1e-6) return true;
        }
        return false;
    }


    private void sendSuccessResponse(HttpServletResponse resp, double x, double y, double r, boolean isInside, LocalDateTime start, LocalDateTime end) throws IOException {
        String JSONresponse = """
        {
            "x": %f,
            "y": %f,
            "r": %f,
            "result": %b,
            "now": "%s",
            "time": %d
        }
        """;

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(String.format(JSONresponse,
                x, y, r, isInside,
                LocalDateTime.now(),
                ChronoUnit.NANOS.between(start, end)
        ));
    }

    private void sendErrorResponse(HttpServletResponse resp, String message) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}