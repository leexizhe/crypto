package org.aquariux.crypto.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aquariux.crypto.dto.TransactionsRequest;
import org.aquariux.crypto.dto.TransactionsResponse;
import org.aquariux.crypto.service.TransactionsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@Slf4j
@RequiredArgsConstructor
public class TransactionsController {
    private final TransactionsService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionsResponse getLatestPrice(@RequestBody TransactionsRequest request) {
        return service.executeTrade(request);
    }
}
