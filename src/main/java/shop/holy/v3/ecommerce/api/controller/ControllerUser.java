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
import shop.holy.v3.ecommerce.service.biz.user.UserCommand;
import shop.holy.v3.ecommerce.service.biz.user.UserQuery;

@RestController
@RequestMapping("users")
@Tag(name = "Users", description = "=> to serve CRUD on users")
@RequiredArgsConstructor
public class ControllerUser {

    private final UserCommand userCommand;
    private final UserQuery userQuery;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    @Operation(summary = "create 1: admin only", description = "Use for Admin only, to manually Create a User \n -> this is authenticated")
    public ResponseEntity<ResponseUser> createAccount(
            @ModelAttribute @Valid RequestUserCreate request) {
        ResponseUser account = userCommand.createUser(request);
        return ResponseEntity.ok(account);
    }

    @GetMapping("")
    @Operation(summary = "get 1 => if customer -> requires no id; \n if admin => must give id", description = """
            only admin provide id, for user leave all defaults (no set) \n
            This is also endpoint to automatic login \n
            if this return AUTHORISATION_NULL || AUTHORISATION_ANNONYMOUS || AUTHORISATION_INVALID \n 
                    => simply route to login page \n
            """)
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0) and principal.enabled")
    public ResponseEntity<ResponseUser> getuser(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) boolean deleted) {
        ResponseUser account = userQuery.getById(id, deleted);
        return ResponseEntity.ok(account);
    }

    @PostMapping("searches")
    @Operation(summary = "Search users: role >= staff", description = "Search users with pagination, \n all conditions are 'AND' concatenated ")
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_STAFF)")
    public ResponseEntity<ResponsePagination<ResponseUser>> search(
            @RequestBody RequestUserSearch searchSpecs
    ) {
        ResponsePagination<ResponseUser> accounts = userQuery.search(searchSpecs);
        return ResponseEntity.ok(accounts);
    }

    @PatchMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0) and principal.enabled")
    @Operation(summary = "Update oneSelf's profile", description = """
            ID là id của profile \n
            => if Admin && id != null \n
            ====> update profile by Id (chính là profileId) \n
            """)
    public ResponseEntity<ResponseProfile> updateProfile(@ModelAttribute RequestProfileUpdate request) {
        ResponseProfile profile = userCommand.updateProfile(request);
        return ResponseEntity.ok(profile);
    }

    @DeleteMapping("me")
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0) and principal.enabled")
    @Operation(summary = "delete 1 ==> admin can't delete self")
    public ResponseEntity<Integer> deleteMe() {
        var x = userCommand.deleteAccount();
        return ResponseEntity.ok(x);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    @Operation(summary = "delete 1 : admin only ==> use this to delete 1 by Id")
    public ResponseEntity<Integer> delete1(@PathVariable long id) {
        var x = userCommand.deleteAccountById(id);
        return ResponseEntity.ok(x);
    }

    @DeleteMapping("")
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    @Operation(summary = "delete many: admin only ==> use this to delete 1 is ok")
    public ResponseEntity<Integer> deletemany(@RequestParam long[] ids) {
        var x = userCommand.deleteAccounts(ids);
        return ResponseEntity.ok(x);
    }


}
