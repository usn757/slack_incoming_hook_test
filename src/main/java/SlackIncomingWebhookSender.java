import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SlackIncomingWebhookSender {

    private static final String WEBHOOK_URL = System.getenv("SLACK_WEBHOOK_URL"); // 환경변수에서 Webhook URL 로드

    public static void main(String[] args) {
        // 이 메소드에서 Slack 메시지 전송 로직을 실행

        String message = "GitHub Actions (Java) 스케줄러에 의해 전송된 메시지입니다."
                + "\n현재 시간: " + java.time.LocalDateTime.now()
                + "\n" + generateNumbers();
        // Slack으로 전송할 메시지 내용을 생성

        if (WEBHOOK_URL == null || WEBHOOK_URL.isEmpty()) {
            System.err.println("ERROR: SLACK_WEBHOOK_URL 환경변수가 설정되지 않았습니다.");
            return;
            // Webhook URL이 설정되지 않았으므로, 더 이상 진행하지 않고 return 문을 사용하여 메소드 실행을 즉시 중단
        }
        sendSlackMessage(message);
        // Webhook URL이 정상적으로 설정된 경우, sendSlackMessage 메소드를 호출하여 Slack 메시지를 전송합니다.
        // message 변수에 담긴 메시지 내용을 sendSlackMessage 메소드에 인자로 전달합니다.
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

    public static String generateNumbers() {
        Set<Integer> numbers = new HashSet<>();
        Random random = new Random();
        while (numbers.size() < 6) {
            numbers.add(random.nextInt(45) + 1);
        }
        StringBuilder sb = new StringBuilder();
        for (Integer number : numbers) {
            sb.append(number).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return "이번 주 추천 번호는 " + sb.toString() + " 입니다.";
    }
}