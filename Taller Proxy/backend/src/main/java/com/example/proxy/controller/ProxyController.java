package com.example.proxy.controller;

import com.example.proxy.model.ChatRequest;
import com.example.proxy.model.ChatResponse;
import com.example.proxy.model.QuotaStatus;
import com.example.proxy.model.UserState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class ProxyController {
    private final Map<String, UserState> users = new ConcurrentHashMap<>();

    private static final Map<String, PlanInfo> PLANS = Map.of(
            "FREE", new PlanInfo("FREE", 1000, 5, 7),
            "PRO", new PlanInfo("PRO", 5000, 20, 30),
            "ENTERPRISE", new PlanInfo("ENTERPRISE", 20000, 60, 60)
    );

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                             @RequestBody ChatRequest request) {
        userId = Optional.ofNullable(userId).filter(id -> !id.isBlank()).orElse("default-user");
        UserState state = users.computeIfAbsent(userId, this::createUserState);
        synchronized (state) {
            resetIfNeeded(state);
            int estimatedTokens = estimateTokens(request.getPrompt());
            if (state.isMonthlyQuotaExceeded(estimatedTokens)) {
                return ResponseEntity.badRequest().body(new ChatResponse(
                        "Cuota mensual agotada. Por favor actualiza el plan.", 0, state.getMonthlyUsedTokens(), state.getMonthlyQuota(), state.getRateLimitRemaining(), state.getSecondsUntilRateLimitReset(), true));
            }
            if (!state.canSendRequest()) {
                return ResponseEntity.status(429).body(new ChatResponse(
                        "Límite de requests por minuto alcanzado. Espera unos segundos.", 0, state.getMonthlyUsedTokens(), state.getMonthlyQuota(), state.getRateLimitRemaining(), state.getSecondsUntilRateLimitReset(), true));
            }
            state.consumeRequest(estimatedTokens);
            String answer = generateResponse(request.getPrompt());
            updateDailyUsage(state, estimatedTokens);
            return ResponseEntity.ok(new ChatResponse(answer, estimatedTokens, state.getMonthlyUsedTokens(), state.getMonthlyQuota(), state.getRateLimitRemaining(), state.getSecondsUntilRateLimitReset(), false));
        }
    }

    @GetMapping("/usage")
    public QuotaStatus usage(@RequestHeader(value = "X-User-Id", required = false) String userId) {
        userId = Optional.ofNullable(userId).filter(id -> !id.isBlank()).orElse("default-user");
        UserState state = users.computeIfAbsent(userId, this::createUserState);
        synchronized (state) {
            resetIfNeeded(state);
            return QuotaStatus.fromState(state);
        }
    }

    private UserState createUserState(String userId) {
        PlanInfo planInfo = PLANS.get("FREE");
        return new UserState(userId, planInfo.name, planInfo.monthlyQuota, planInfo.requestsPerMinute, planInfo.dailyHistorySize);
    }

    private void resetIfNeeded(UserState state) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        if (!today.equals(state.getLastMonthlyReset())) {
            state.resetMonthlyUsage(today);
        }
        state.resetRateLimitIfNeeded();
    }

    private int estimateTokens(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            return 1;
        }
        int tokens = Math.max(1, prompt.trim().length() / 4);
        return Math.min(tokens, 200);
    }

    private String generateResponse(String prompt) {
        String base = prompt == null ? "" : prompt.trim();
        if (base.isBlank()) {
            return "Hola, ¿en qué puedo ayudarte hoy?";
        }
        return "Hola, esta es una respuesta generada por el proxy de IA para tu prompt:\n\n" + base;
    }

    private void updateDailyUsage(UserState state, int tokens) {
        state.addDailyUsage(tokens);
    }

    private static final class PlanInfo {
        final String name;
        final int monthlyQuota;
        final int requestsPerMinute;
        final int dailyHistorySize;

        PlanInfo(String name, int monthlyQuota, int requestsPerMinute, int dailyHistorySize) {
            this.name = name;
            this.monthlyQuota = monthlyQuota;
            this.requestsPerMinute = requestsPerMinute;
            this.dailyHistorySize = dailyHistorySize;
        }
    }
}
