package com.shine;

import cn.hutool.json.JSONObject;

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
 * @since 2025/8/11 09:59
 */
public interface DataService {

    /**
     * 获取异常的商品
     */
    JSONObject errorProduct(String productId);

    /**
     * 获取商品数据
     */
    Response<?> productData(String token, String productId);

    /**
     * 获取全部用户信息
     */
    Response<?> totalUser(String token);
}
