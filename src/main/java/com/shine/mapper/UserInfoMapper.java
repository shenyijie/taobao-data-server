package com.shine.mapper;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import com.shine.entity.UserInfo;

import static com.shine.entity.table.UserInfoTableDef.USER_INFO;

/**
 *  映射层。
 *
 * @author Sky
 * @since 2025-08-11
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    default UserInfo findByToken(String token) {
        if (StrUtil.isBlank(token)) {
            return null;
        }
        QueryWrapper queryWrapper = QueryWrapper
                .create()
                .select()
                .where(USER_INFO.TOKEN.eq(token));
        return selectOneByQuery(queryWrapper);
    }
}
