package org.crypto.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crypto.dto.TransactionsRequest;
import org.crypto.dto.TransactionsResponse;
import org.crypto.service.TransactionsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@Slf4j
@RequiredArgsConstructor
public class TransactionsController {
    private final TransactionsService service;

    @GetMapping("/{userId}")
    public List<TransactionsResponse> getUserTradeHistory(@PathVariable Long userId) {
        return service.getUserTradeHistory(userId);
    }

    @PostMapping("/trade")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionsResponse executeTrade(@Valid @RequestBody TransactionsRequest request) {
        return service.executeTrade(request);
    }
}
