package org.aquariux.crypto.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aquariux.crypto.dto.WalletResponse;
import org.aquariux.crypto.service.WalletService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@Slf4j
@RequiredArgsConstructor
public class WalletController {
    private final WalletService service;

    @GetMapping("/{userId}")
    public WalletResponse getWallet(@PathVariable Long userId) {
        return service.getWallet(userId);
    }
}
