# Slack Message Scheduler (Java)

이 프로젝트는 GitHub Actions를 사용하여 매주 일요일 자정에 Slack으로 메시지를 자동으로 전송하는 Java 애플리케이션입니다.

## 기능

- 매주 일요일 자정에 GitHub Actions 스케줄러를 통해 Slack 메시지를 자동으로 전송합니다.
- GitHub Actions 워크플로우를 통해 수동으로 메시지를 전송할 수도 있습니다.
- Slack Webhook URL을 환경 변수를 통해 안전하게 관리합니다.
- 현재 시간과 함께 1부터 45까지의 숫자중 랜덤으로 6개의 숫자를 생성하여 메시지에 포함합니다.

## 설정

### Slack Webhook URL 설정

1.  Slack 워크스페이스에서 사용할 앱을 생성하고 Incoming Webhooks를 활성화합니다.
2.  Incoming Webhook URL을 복사합니다.
3.  GitHub 저장소의 Secrets에 `SLACK_WEBHOOK_URL` 이름으로 Webhook URL을 추가합니다.

### GitHub Actions 워크플로우 설정

`.github/workflows/slack-message.yml` 파일을 생성하고 다음 내용을 추가합니다.

```yaml
name: Slack Message Scheduler (Java)

on:
  schedule:
    - cron: "0 0 * * 0" # 매주 일요일 자정에 실행 (UTC 기준)
  workflow_dispatch: # 수동 실행 가능

jobs:
  send_slack_message:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout repository
        uses: actions/checkout@v4 # v4 버전 사용 권장
      - name: Set up JDK 17
        uses: actions/setup-java@v4 # v4 버전 사용 권장
        with:
          java-version: "17"
          distribution: "temurin"
      - name: 실행 가능한 JAR 파일 빌드 (Maven 예시)
        run: mvn clean install # Maven 빌드 명령 (Gradle 사용하는 경우 gradlew build)
      - name: 자바 프로그램 실행 및 Slack 메시지 전송
        run: java -jar target/testSlackApi-1.0-SNAPSHOT.jar # JAR 파일 실행 (파일명은 pom.xml 설정에 따라 다를 수 있음)
        env:
          SLACK_WEBHOOK_URL: ${{ secrets.SLACK_WEBHOOK_URL }} # 환경변수로 Webhook URL 전달
```

### Java 코드 설정

1.  프로젝트의 `SlackIncomingWebhookSender.java` 파일을 생성하고 제공된 코드를 붙여넣습니다.
2.  필요에 따라 메시지 내용을 수정합니다.
3.  Maven 또는 Gradle을 사용하여 프로젝트를 빌드합니다.
4.  JAR 파일을 생성하고 GitHub Actions 워크플로우에서 실행되도록 설정합니다.

## 실행

1.  매주 일요일 자정에 GitHub Actions 스케줄러를 통해 자동으로 실행됩니다.
2.  GitHub 저장소의 Actions 탭에서 워크플로우를 수동으로 실행할 수도 있습니다.

## 주의사항

- Slack Webhook URL을 안전하게 관리해야 합니다.
- GitHub Actions 워크플로우 설정 파일을 올바르게 작성해야 합니다.
- JAR 파일 이름은 `pom.xml` 또는 `build.gradle` 파일의 설정에 따라 달라질 수 있습니다.
- 메이븐을 사용할 경우 해당파일 이름을 `testSlackApi-1.0-SNAPSHOT.jar` 로 변경하거나, 워크플로우 파일의 java -jar target/ 명령부분을 알맞게 수정해주셔야합니다.
