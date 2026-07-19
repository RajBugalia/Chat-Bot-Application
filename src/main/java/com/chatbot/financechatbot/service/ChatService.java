package com.chatbot.financechatbot.service;

import com.chatbot.financechatbot.model.ChatRequest;
import com.chatbot.financechatbot.model.ChatResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Service
public class ChatService {

    private final String LIVE_AI_URL = "https://52.35.65.204:8081/completion";
//    private final String LIVE_AI_URL = "https://13.126.65.16:8081/completion";


    public ChatService() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                    }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ChatResponse getChatCompletion(String userPrompt) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ChatRequest requestBody = new ChatRequest();
            requestBody.setPrompt(userPrompt);

            HttpEntity<ChatRequest> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<ChatResponse> response = restTemplate.postForEntity(
                    LIVE_AI_URL,
                    requestEntity,
                    ChatResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                return new ChatResponse("Server returned an error code: " + response.getStatusCode(), "system");
            }

        } catch (Exception e) {
            System.err.println("Live API Error: " + e.getMessage());

            // Fallback content if the AI server is disconnected
            String fallbackContent = "As a financial analyst, I am here to help you with any financial questions, analysis, or insights you might need.\n\n" +
                    "To give you the most valuable assistance, please tell me what you're interested in.\n" +
                    "For example, are you looking for:\n\n" +
                    "* Investment Advice & Analysis?\n" +
                    "* Analyzing a specific stock (e.g., \"What are the valuation metrics for Apple?\")\n" +
                    "* Comparing different sectors (e.g., \"How does the tech sector look\")\n\n" +
                    "⚠️ [API Not Connected]";

            return new ChatResponse(fallbackContent, "assistant");
        }
    }
}