package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "Users", description = "=> to serve CRUD on users")
@RequiredArgsConstructor
public class ControllerUser {

    private final UserService userService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    @Operation(description = "Use for Admin only, to manually Create a User \n -> this is authenticated")
    public ResponseEntity<?> createAccount(
            @ModelAttribute @Valid RequestUserCreate request) throws IOException {
        ResponseUser account = userService.createUser(request);
        return ResponseEntity.ok(account);
    }

    @GetMapping("")
    @Operation(description = """
            only admin provide id, for user leave all defaults (no set) \n
            This is also endpoint to automatic login \n
            if this return AUTHORISATION_NULL || AUTHORISATION_ANNONYMOUS || AUTHORISATION_INVALID \n 
                    => simply route to login page \n
            """)
    public ResponseEntity<ResponseUser> getusers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) boolean deleted) {
        ResponseUser account = userService.getById(id, deleted);
        return ResponseEntity.ok(account);
    }

    @PostMapping("searches")
    @Operation(summary = "Search users", description = "Search users with pagination, \n all conditions are 'AND' concatenated ")
    public ResponseEntity<?> search(
            @RequestBody RequestUserSearch searchSpecs
    ) {
        ResponsePagination<ResponseUser> accounts = userService.search(searchSpecs);
        return ResponseEntity.ok(accounts);
    }

    @PatchMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    @Operation(description = "Perhaps use for users only, since why tf admin need profile update?")
    public ResponseEntity<?> updateProfile(@Valid @ModelAttribute RequestProfileUpdate request) throws IOException {
        ResponseProfile profile = userService.updateProfile(request);
        return ResponseEntity.ok(profile);
    }




}
