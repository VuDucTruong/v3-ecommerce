package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.RequestPageable;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.comment.RequestComment;
import shop.holy.v3.ecommerce.api.dto.comment.RequestCommentSearch;
import shop.holy.v3.ecommerce.api.dto.comment.RequestCommentUpdate;
import shop.holy.v3.ecommerce.api.dto.comment.ResponseComment;
import shop.holy.v3.ecommerce.service.biz.comment.CommentService;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;

@RequiredArgsConstructor
@RestController
@RequestMapping("comments")
@Tag(name = "Comments")
public class ControllerComment {

    private final CommentService commentService;

    @GetMapping("{id}")
//    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    @Operation(summary = "Get 1 Comment-> Có thể là ko cần dùng, vì ai đi lấy 1 comment làm gì?")
    public ResponseEntity<ResponseComment> getResponseCommentById(@PathVariable Long id, @RequestParam(required = false) boolean deleted) {
        var rs = commentService.getById(id, deleted);
        return ResponseEntity.ok(rs);
    }

    @Operation(summary = "get many", description = """
            Nếu có productId
            ==> Vẫn trả về comment kể cả đã delete \n
            Nếu không:
            ==> Chỉ trả về các comment của bản thân \n
            ==> yêu cầu phải có AUTH
            ===> phần comment nào bị delete sẽ bị content = null \n
            ====> if deleted = true, trả về comment và ko bị content = null \n
            """)
    @GetMapping("")
    public ResponseEntity<ResponsePagination<ResponseComment.Light>> getCommentsByProductIds(@RequestParam(required = false) Long productId,
                                                                                             @RequestParam(required = false) boolean deleted,
                                                                                             @ParameterObject RequestPageable requestPageable) {
        Pageable pageable = MappingUtils.fromRequestPageableToPageable(requestPageable);
        ResponsePagination<ResponseComment.Light> cmt;
        if(productId != null) {
            cmt = commentService.getCommentsByProductId(productId, deleted, pageable);
        }else
            cmt = commentService.getSelfComments(deleted, pageable);
        return ResponseEntity.ok(cmt);
    }


    @PostMapping("/searches")
    @Operation(summary = "đọc description trước", description = """
            1. <b>response.Parent</b> sẽ ko có product => lấy product từ <b>response.Product</b> luôn
            2. parentCommentId == parent.id
            3. search params sẽ áp dụng trên cả response & <b>response.Parent</b>
            """)
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    public ResponseEntity<ResponsePagination<ResponseComment.Flattened>> search(
            @RequestBody RequestCommentSearch searchReq
    ) {
        return ResponseEntity.ok(commentService.search(searchReq));
    }


    @Operation(summary = "delete 1", description = """
        Nếu là CUSTOMER
        sẽ chỉ xóa khi đó là comment của bản thân
        Còn lại được xóa hết thoải mái
            """)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    public ResponseEntity<Integer> deleteCommentById(@PathVariable long id) {
        return ResponseEntity.ok(commentService.deleteComment(id));
    }

    @Operation(summary = "delete many")
    @DeleteMapping("")
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    public ResponseEntity<Integer> deleteComments(@RequestParam long[] ids) {
        return ResponseEntity.ok(commentService.deleteComments(ids));
    }

    @Operation(summary = "create 1")
    @PostMapping("")
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    public ResponseEntity<ResponseComment> createComment(
            @RequestBody RequestComment request
    ) {
        return ResponseEntity.ok(commentService.insert(request));
    }

    @Operation(summary = "update 1")
    @PutMapping("")
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    public ResponseEntity<ResponseComment> updateComment(
            @RequestBody @NotNull(message = "Invalid Request must not be null") @Valid RequestCommentUpdate request) {
        var cmt = commentService.update(request);
        return ResponseEntity.ok(cmt);
    }


}
