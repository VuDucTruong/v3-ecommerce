package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.account.RequestProfileUpdate;
import shop.holy.v3.ecommerce.api.dto.account.ResponseUser;
import shop.holy.v3.ecommerce.api.dto.user.RequestUserCreate;
import shop.holy.v3.ecommerce.api.dto.user.RequestUserSearch;
import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;
import shop.holy.v3.ecommerce.service.biz.UserService;

import java.io.IOException;

@RestController
@RequestMapping("users")
@Tag(name = "Users")
@RequiredArgsConstructor
public class ControllerUser {

    private final UserService userService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAccount(
            @ModelAttribute @Valid RequestUserCreate request) throws IOException {
        ResponseUser account = userService.createUser(request);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUser> getAccount(
            @PathVariable Long id,
            @RequestParam(required = false) boolean deleted) {
        ResponseUser account = userService.getById(id, deleted);
        return ResponseEntity.ok(account);
    }

    @PostMapping("searches")
    @Operation(summary = "Search users", description = "Search users with pagination")
    public ResponseEntity<?> search(
            @RequestBody RequestUserSearch searchSpecs
    ) {
        ResponsePagination<ResponseUser> accounts = userService.search(searchSpecs);
        return ResponseEntity.ok(accounts);
    }

    @PatchMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(@Valid @ModelAttribute RequestProfileUpdate request) throws IOException {
        ResponseProfile profile = userService.updateProfile(request);
        return ResponseEntity.ok(profile);
    }


}
