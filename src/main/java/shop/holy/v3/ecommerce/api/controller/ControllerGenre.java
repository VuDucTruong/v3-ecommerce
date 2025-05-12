package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.blog.genre.RequestGenreUpsert;
import shop.holy.v3.ecommerce.api.dto.blog.genre.ResponseGenre;
import shop.holy.v3.ecommerce.service.biz.GenreService;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Tag(name = "Genres", description = "May perform get only, as I may hard code this")
public class ControllerGenre {
    private final GenreService genreService;

    @PutMapping
    public ResponseEntity<ResponseGenre> upsert(@RequestBody RequestGenreUpsert request) {

        var x = genreService.upsert(request);
        return ResponseEntity.ok(x);
    }

    @GetMapping("identifier")
    public ResponseEntity<ResponseGenre> getByIdentifier(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) boolean deleted) {
        var x = genreService.getByIdentifier(id, name, deleted);
        return ResponseEntity.ok(x);
    }

    @GetMapping
    public ResponseEntity<ResponseGenre[]> getAllGenres() {
        var x = genreService.getAllGenres();
        return ResponseEntity.ok(x);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<ResponseGenre> delete(long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.ok().build();
    }


}
