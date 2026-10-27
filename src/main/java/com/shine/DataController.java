package com.shine;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ━━━━━━━━━佛祖保佑━━━━━━━━━━━
 *                  ,;,,;
 *                ,;;' (    社
 *      ___      ,;;' ' \   会
 *    /'  '\'~~'~' \ /'\.)  主
 * ,;(      )     /  |.     义
 *,;' \    /-.,,(   ) \     码
 *     ) /       ) / )|     农
 *     ||        ||  \\
 *    (_\       (_\  (_\
 * ━━━━━━━━━永无BUG━━━━━━━━━━━━
 *
 * @author Shine
 * @since 2025/8/11 09:31
 */

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class DataController {
    private final DataService dataService;

    @GetMapping("/productData")
    public Response<?> productData(@RequestParam(required = false) String token, @RequestParam(required = false) String productId) {
        if (StrUtil.isBlank(token) || StrUtil.length(token) != 32) {
            return Response.error("用户非法");
        }
        if (StrUtil.isBlank(productId) || StrUtil.length(productId) != 12 || !NumberUtil.isNumber(productId)) {
            return Response.error("商品非法", dataService.errorProduct(productId));
        }
        return dataService.productData(token, productId);
    }

    @GetMapping("/totalUser")
    public Response<?> totalUser(@RequestParam(required = false) String token) {
        if (StrUtil.isBlank(token)) {
            return Response.error("用户非法");
        }
        return dataService.totalUser(token);
    }
}
