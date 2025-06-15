package shop.holy.v3.ecommerce.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerFallback {

    @GetMapping("swagger-ui/undefined")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok().build();
    }

}
