package web.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { // <--- Добавили throws
        // Мы можем оставить try-catch для обработки ошибок при записи ответа
        // или для проверки параметров
        try {
            if (req.getParameter("x") != null && req.getParameter("y") != null && req.getParameter("r") != null) {
                System.out.println("[Controller] Получен запрос: " +
                        req.getParameter("x") + " " +
                        req.getParameter("y") + " " +
                        req.getParameter("r"));

                // Если forward бросит исключение, оно будет корректно передано выше
                getServletContext().getRequestDispatcher("/checkArea").forward(req, resp);

            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write("{\"error\": \"Отсутствуют параметры x, y или r\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"error\": \"Внутренняя ошибка контроллера\"}");
        }
    }
}
