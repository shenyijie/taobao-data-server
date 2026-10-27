package com.shine;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.shine.entity.UserInfo;
import com.shine.mapper.UserInfoMapper;
import com.shine.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * @since 2025/8/11 10:02
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class DataServiceImpl implements DataService {
    private final UserInfoMapper userInfoMapper;

    /**
     * 获取异常的商品
     */
    @Override
    public JSONObject errorProduct(String productId) {
        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.set("id", productId);
        jsonObject.set("url", StrUtil.format("https://item.taobao.com/item.htm?id={}", productId));
        jsonObject.set("fail", "商品信息不存在或已下架");
        return jsonObject;
    }

    /**
     * 获取商品数据
     */
    @Override
    public Response<?> productData(String token, String productId) {
        // 1. 请求用户信息
        UserInfo userInfo = userInfoMapper.findByToken(token);
        if (ObjectUtil.isNull(userInfo)) {
            return Response.error("用户非法");
        }
        if (userInfo.getBalance() < 1) {
            return Response.error("余额不足，请联系供应商充值");
        }
        log.info("「{}」正在请求「{}」商品信息", token, productId);
        File productFile = getProductFile(token, productId);
        try {
            JSONObject jsonItem = JSONUtil.parseObj(FileUtil.readUtf8String(productFile));
            JSONObject jsonResult = JSONUtil.createObj();
            if (jsonItem.containsKey("error_msg")) {
                return Response.error(jsonItem.getStr("error_msg"));
            }
            if (!jsonItem.containsKey("num_iid")) {
                return Response.error("未查询到商品信息", errorProduct(productId));
            }
            jsonResult.set("id",  jsonItem.get("num_iid"));
            jsonResult.set("title",  jsonItem.get("title"));
            jsonResult.set("brand",  jsonItem.get("brand"));
            jsonResult.set("shop",  jsonItem.get("nick"));
            if (jsonItem.containsKey("item_imgs")) {
                List<String> mainImages = CollectionUtil.newArrayList();
                jsonItem.getJSONArray("item_imgs").toList(JSONObject.class).forEach(item -> mainImages.add(item.getStr("url")));
                jsonResult.set("main_images", mainImages);
            }
            jsonResult.set("detail_url",  jsonItem.get("detail_url"));
            if (jsonItem.containsKey("video")) {
                Object video = jsonItem.get("video");
                if (video instanceof JSONObject) {
                    jsonResult.set("video", ((JSONObject) video).get("url"));
                } else {
                    jsonResult.set("video", null);
                }
            }
            jsonResult.set("desc_images",  jsonItem.get("desc_img"));
            jsonResult.set("location",  jsonItem.get("location"));
            if (jsonItem.containsKey("seller_info")) {
                JSONObject sellerInfo = jsonItem.getJSONObject("seller_info");
                JSONObject seller = JSONUtil.createObj();
                seller.set("nick",  sellerInfo.get("nick"));
                seller.set("shop_name",  sellerInfo.get("shop_name"));
                seller.set("delivery_score",  sellerInfo.get("delivery_score"));
                seller.set("item_score",  sellerInfo.get("item_score"));
                jsonResult.set("seller_info", seller);
            }
            jsonResult.set("seller_id",  jsonItem.get("seller_id"));
            jsonResult.set("shop_id",  jsonItem.get("shop_id"));
            jsonResult.set("category_id",  jsonItem.get("cid"));
            jsonResult.set("tmall",  jsonItem.get("tmall"));
            jsonResult.set("props_info", jsonItem.get("props"));

            JSONObject propsData = jsonItem.getJSONObject("props_data");
            if (ObjectUtil.isNotEmpty(propsData)) {
                Object propsImg = jsonItem.get("props_img");
                JSONObject propImages;
                if (propsImg instanceof JSONObject) {
                    propImages = jsonItem.getJSONObject("props_img");
                } else {
                    propImages = JSONUtil.createObj();
                }
                JSONArray skuData = jsonItem.getJSONObject("skus").getJSONArray("sku");

                // 1. 分析属性信息
                List<String> codes = CollectionUtil.newArrayList();
                Map<String, JSONObject> codeMap = MapUtil.newHashMap();
                for (String propKey : propsData.keySet()) {
                    JSONArray props = propsData.getJSONArray(propKey);
                    for (JSONObject prop : props.toList(JSONObject.class)) {
                        String name = prop.getStr("name");
                        String key = prop.getStr("key");
                        String code = prop.getStr("code");

                        if (!codeMap.containsKey(code)) {
                            JSONObject meta = JSONUtil.createObj();
                            meta.set("pname", name);
                            meta.set("pid", code);
                            meta.set("values", JSONUtil.createArray());
                            codeMap.put(code, meta);
                            codes.add(code);
                        }

                        JSONObject value = JSONUtil.createObj();
                        value.set("vname", prop.get("value"));
                        value.set("vid", key);
                        String codeKey = StrUtil.format("{}:{}", code, key);
                        if (propImages.containsKey(codeKey)) {
                            value.set("sku_url", propImages.get(codeKey));
                        }
                        JSONArray values = codeMap.get(code).getJSONArray("values");
                        values.add(value);
                    }
                }

                // 2. 归档属性信息
                JSONArray props = JSONUtil.createArray();
                codes.forEach(code -> props.add(codeMap.get(code)));

                // 3. 生成sku数据
                JSONObject skuFormatData = JSONUtil.createObj();
                for (JSONObject skuInfo : skuData.toList(JSONObject.class)) {
                    JSONObject sku = JSONUtil.createObj();
                    sku.set("sku_id", skuInfo.get("sku_id"));
                    sku.set("quantity", skuInfo.get("quantity"));
                    sku.set("origin_price", skuInfo.get("orginal_price"));
                    sku.set("price", skuInfo.get("price"));
                    skuFormatData.set(skuInfo.getStr("properties"), sku);
                }

                JSONObject skuResult = JSONUtil.createObj();
                skuResult.set("props", props);
                skuResult.set("data", skuFormatData);

                jsonResult.set("skus_info", skuResult);
            } else {
                jsonResult.set("skus_info", JSONUtil.createObj());
            }

            jsonResult.set("price", jsonItem.get("price_api_old"));
            jsonResult.set("total_sold", jsonItem.getFloat("total_sold", 0.0F));
            jsonResult.set("update_time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

            userInfo.setBalance(userInfo.getBalance() - 1);
            userInfoMapper.update(userInfo);
            log.info("用户「{}」请求「{}」成功，消费积分「1」", token, productId);

            Response<JSONObject> response = Response.success("调用成功", jsonResult);
            response.setBalance(userInfo.getBalance());
            return response;
        } catch (Exception e) {
            log.error("「{}」解析「{}」出错", token, productId, e);
            return Response.error("商品信息解析失败，请联系管理员");
        }
    }

    /**
     * 获取全部用户信息
     */
    @Override
    public Response<?> totalUser(String token) {
        String YUN_TOKEN = "6d96cdf3551f41904d93764b6239c9a5";
        if (!StrUtil.equals(token, YUN_TOKEN)) {
            return Response.error("非法用户");
        }

        List<UserInfo> userInfos = userInfoMapper.selectAll();
        List<UserInfoVO> userInfoVOS = CollectionUtil.newArrayList();
        for (UserInfo userInfo : userInfos) {
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtils.copyProperties(userInfo, userInfoVO);
            userInfoVOS.add(userInfoVO);
        }
        return Response.success("调用成功", userInfoVOS);
    }

    private synchronized File getProductFile(String token, String productId) {
        String requestUrl = StrUtil.format("https://www.order999.com/item-{}.html", productId);
        File productFile = FileUtil.file(String.valueOf(Paths.get("").toAbsolutePath()), "data", StrUtil.format("{}.json", productId));
        if (FileUtil.exist(productFile)) {
            return productFile;
        }
        HttpRequest dataRequest = HttpUtil.createPost(requestUrl);
        JSONObject jsonCookie = JSONUtil.parseObj(MockManager.getMockData("cookie.json"));
        for (String key : jsonCookie.keySet()) {
            String value = jsonCookie.getStr(key);
            dataRequest.header(key, value);
        }
        try (HttpResponse dataResponse = dataRequest.execute()) {
            String content = dataResponse.body();
            Pattern pattern = Pattern.compile("itemInfo = (.*?)};");
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                String itemInfo = StrUtil.format("{}}", matcher.group(1));
                FileUtil.writeUtf8String(JSONUtil.toJsonStr(JSONUtil.parseObj(itemInfo)), productFile);
                log.info("「{}」请求「{}」成功「{}」", token, productId, productFile);
            }
        } catch (Exception e) {
            log.error("「{}」请求「{}」出错", token, productId, e);
        } finally {
            if (!FileUtil.exist(productFile)) {
                FileUtil.writeUtf8String(setErrorProduct(productId), productFile);
            }
        }
        return productFile;
    }

    private String setErrorProduct(String productId) {
        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.set("num_iid", productId);
        jsonObject.set("error_msg", "数据获取异常");
        jsonObject.set("date", System.currentTimeMillis());
        return JSONUtil.toJsonStr(jsonObject);
    }


}
