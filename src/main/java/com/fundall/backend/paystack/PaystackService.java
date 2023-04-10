package com.fundall.backend.paystack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fundall.backend.user.User;
import com.fundall.backend.user.UserRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
public class PaystackService {

    private static final Logger logger = LoggerFactory.getLogger(PaystackService.class);

    @Value("${API_KEY}")
    private String API_KEY;
    private static final String INITIALIZE_URL = "https://api.paystack.co/transaction/initialize";
    private static final String VERIFY_URL = "https://api.paystack.co/transaction/verify/";

    @Autowired
    private UserRepository userRepository;

    private User findAUserByEmail(String email) {
        return userRepository.findByEmail(email)
        .orElseThrow(() -> {
                logger.error("User not found with email: {}", email);
                return new EntityNotFoundException("User could bot be found");
        });
    }

    public JSONObject createTransaction(String email, int amount, long cardId) {
        var user = findAUserByEmail(email);
        user.setCardToTopUp(cardId);
        userRepository.save(user);

        String reference = generateReference();

        // Create transaction
        JSONObject initializeData = new JSONObject()
                .put("email", email)
                .put("amount", amount)
                .put("callback_url", "https://github.com/ozuem-liam")
                .put("reference", reference);
        JSONObject initializeResponse = sendPostRequest(initializeData);

        // Return transaction data
        return initializeResponse.getJSONObject("data");
    }

    public JSONObject verifyTransaction(String reference) {
        try {
            Thread.sleep(10000); // Wait 10 seconds for payment to be completed
            JSONObject verifyResponse = sendGetRequest(reference);
            return verifyResponse;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject sendPostRequest(JSONObject data) {
        try {
            URL url = new URL(INITIALIZE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(data.toString().getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject sendGetRequest(String urlString) {
        try {
            URL url = new URL(VERIFY_URL + urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);

            int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateReference() {
        long timestamp = System.currentTimeMillis();
        return "REF" + timestamp;
    }
}

