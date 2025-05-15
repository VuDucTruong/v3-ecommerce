package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Statistics", description = "=> for users home page")
@RequiredArgsConstructor
@RequestMapping("statistics")
public class ControllerStatistics {



}
