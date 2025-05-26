package org.rhizome.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApiLoggingInterceptor implements HandlerInterceptor {

    // 전용 로거들 정의
    private static final Logger API_SUCCESS_LOG = LoggerFactory.getLogger("API_SUCCESS");
    private static final Logger API_ERROR_LOG = LoggerFactory.getLogger("API_ERROR");

    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 요청 시작 시간 기록
        request.setAttribute(START_TIME, System.currentTimeMillis());

        // 테스트용 로그
        log.info("🔍 인터셉터 동작 - {} {}", request.getMethod(), request.getRequestURI());

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        // 응답 시간 계산
        Long startTime = (Long) request.getAttribute(START_TIME);
        long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;

        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();
        String queryString = request.getQueryString();

        // 로그 메시지 구성
        String logMessage = String.format(
                "%s %s | Status: %d | Duration: %dms | QueryParameter: %s",
                method, uri, status, duration, queryString != null ? queryString : "없음");

        if (ex != null) {
            // 예외 발생한 경우 - api-error.log에 기록
            API_ERROR_LOG.error(
                    "❌ API 실패 - {} | Error: {} | StackTrace: {}",
                    logMessage,
                    ex.getMessage(),
                    getStackTraceAsString(ex));
        } else if (status >= 400) {
            // HTTP 에러 상태 코드 - api-error.log에 기록
            API_ERROR_LOG.error("⚠️ API 에러 응답 - {}", logMessage);
        } else {
            // 성공한 경우 - api-success.log에 기록
            String statusIcon = getStatusIcon(status);
            API_SUCCESS_LOG.info("{} API 성공 - {}", statusIcon, logMessage);
        }
    }

    private String getStatusIcon(int status) {
        if (status >= 200 && status < 300) return "✅";
        if (status >= 300 && status < 400) return "↩️";
        return "📤";
    }

    private String getStackTraceAsString(Exception ex) {
        // 스택트레이스 전체를 문자열로 변환 (간단히 처리)
        return ex.getClass().getSimpleName() + ": " + ex.getMessage();
    }
}
