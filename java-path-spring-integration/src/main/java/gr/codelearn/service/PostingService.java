package gr.codelearn.service;


import java.util.Map;

public interface PostingService {
    Map<String, Object> makeTransaction(Map<String, Object> payload);
    Map<String, Object> makeWalletTransaction(Map<String, Object> payload);
}
