package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Sale;
import LikeLion.TodaysLunch.dto.SaleDto;
import LikeLion.TodaysLunch.service.MenuService;
import LikeLion.TodaysLunch.service.SaleService;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaleController {
  private final SaleService saleService;
  @Autowired
  public SaleController(SaleService saleService) {
    this.saleService = saleService;
  }

  @GetMapping("/sale-menus")
  public ResponseEntity<HashMap<String, Object>> saleMenuList(Pageable pageable){
    Page<Menu> menus = saleService.saleMenuList(pageable);
    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", menus.getContent());
    responseMap.put("totalPages", menus.getTotalPages());
    return ResponseEntity.status(HttpStatus.OK).body(responseMap);
  }

  @PostMapping("/menus/{menuId}/sale")
  public ResponseEntity<Sale> saleMenuCreate(@RequestBody SaleDto saleDto, @PathVariable Long menuId){
    Sale sale = saleService.create(saleDto, menuId);
    return ResponseEntity.status(HttpStatus.OK).body(sale);
  }

}
