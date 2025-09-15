package wwwyssa;

import com.fastcgi.FCGIInterface;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

public class Main {
    private static final String HTTP_RESPONSE = """
            HTTP/1.1 200 OK
            Content-Type: application/json
            Content-Length: %d
            
            %s
            """;
    private static final String HTTP_ERROR = """
            HTTP/1.1 400 Bad Request
            Content-Type: application/json
            Content-Length: %d
            
            %s
            """;
    private static final String RESULT_JSON = """
            {
                "time": %d,
                "now": "%s",
                "result": %b
            }
            """;
    private static final String ERROR_JSON = """
            {
                "now": "%s",
                "reason": "%s"
            }
            """;

    public static void main(String[] args) {
        FCGIInterface fcgi = new FCGIInterface();
        while (fcgi.FCGIaccept() >= 0) {
            try {
                String method = System.getProperty("REQUEST_METHOD");
                String queryString = "";

                if ("GET".equalsIgnoreCase(method)) {
                    // Для GET запросов
                    queryString = System.getProperty("QUERY_STRING");
                } else if ("POST".equalsIgnoreCase(method)) {
                    // Для POST запросов - читаем тело запроса
                    String contentLengthStr = System.getProperty("CONTENT_LENGTH");
                    if (contentLengthStr != null && !contentLengthStr.isEmpty()) {
                        int contentLength = Integer.parseInt(contentLengthStr);
                        if (contentLength > 0) {
                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(System.in, StandardCharsets.UTF_8));
                            char[] buffer = new char[contentLength];
                            reader.read(buffer, 0, contentLength);
                            queryString = new String(buffer);
                        }
                    }
                } else {
                    throw new ValidationException("Unsupported HTTP method: " + method);
                }

                Params params = new Params(queryString);
                LocalDateTime startTime = LocalDateTime.now();
                Boolean result = calculate(params.getX(), params.getY(), params.getR());
                LocalDateTime endTime = LocalDateTime.now();

                String json = String.format(RESULT_JSON,
                        ChronoUnit.NANOS.between(startTime, endTime),
                        LocalDateTime.now(),
                        result);
                String response = String.format(HTTP_RESPONSE,
                        json.getBytes(StandardCharsets.UTF_8).length,
                        json);
                System.out.println(response);
            } catch (ValidationException e) {
                String json = String.format(ERROR_JSON, LocalDateTime.now(), e.getMessage());
                String response = String.format(HTTP_ERROR,
                        json.getBytes(StandardCharsets.UTF_8).length,
                        json);
                System.out.println(response);
            } catch (Exception e) {
                String json = String.format(ERROR_JSON, LocalDateTime.now(), "Internal server error");
                String response = String.format(HTTP_ERROR,
                        json.getBytes(StandardCharsets.UTF_8).length,
                        json);
                System.out.println(response);
            }
        }
    }

    private static boolean calculate(float x, float y, float r) {
        if (x > 0 && y > 0) {
            return false;
        }
        if (x < 0 && y > 0) {
            if ((y - x) > (r / 2)) {
                return false;
            }
        }
        if (x < 0 && y < 0) {
            if ((x * x + y * y) > (r * r)) {
                return false;
            }
        }
        if (x > 0 && y < 0) {
            if (x > r || y < -r / 2) {
                return false;
            }
        }
        return true;
    }
}