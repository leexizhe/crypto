package org.aquariux.crypto.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aquariux.crypto.dto.TransactionsRequest;
import org.aquariux.crypto.dto.TransactionsResponse;
import org.aquariux.crypto.exception.ApplicationException;
import org.aquariux.crypto.service.TransactionsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionsResponse getLatestPrice(@RequestBody TransactionsRequest request) {
        return service.executeTrade(request);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<String> handlePriceNotFoundException(ApplicationException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
