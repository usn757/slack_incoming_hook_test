import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SlackIncomingWebhookSender {

    private static final String WEBHOOK_URL = System.getenv("SLACK_WEBHOOK_URL"); // 환경변수에서 Webhook URL 로드

    public static void main(String[] args) {
        String message = "GitHub Actions (Java) 스케줄러에 의해 전송된 메시지입니다. 현재 시간: " + java.time.LocalDateTime.now();
        if (WEBHOOK_URL == null || WEBHOOK_URL.isEmpty()) {
            System.err.println("ERROR: SLACK_WEBHOOK_URL 환경변수가 설정되지 않았습니다.");
            return;
        }
        sendSlackMessage(message);
    }

    public static void sendSlackMessage(String text) {
        try {
            URL url = new URL(WEBHOOK_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            String payload = String.format("{\"text\":\"%s\"}", text);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Slack 메시지 전송 성공!");
            } else {
                System.out.println("Slack 메시지 전송 실패. 상태 코드: " + responseCode);
            }
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}