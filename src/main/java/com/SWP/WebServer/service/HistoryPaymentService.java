package com.SWP.WebServer.service;

import com.SWP.WebServer.entity.HistoryPayment;
import com.SWP.WebServer.repository.HistoryPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryPaymentService {
    @Autowired
    private HistoryPaymentRepository historyPaymentRepository;

    // getHistoryPayment
    public List<HistoryPayment> getHistoryPayment(int userId) {
        return historyPaymentRepository.findByuid(userId);
    }

    // saveHistoryPayment
    public void saveHistoryPayment(HistoryPayment historyPayment) {
        historyPaymentRepository.save(historyPayment);
    }

}
