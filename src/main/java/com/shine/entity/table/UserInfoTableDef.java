package com.shine.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

import java.io.Serial;

/**
 *  表定义层。
 *
 * @author Sky
 * @since 2025-08-11
 */
public class UserInfoTableDef extends TableDef {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public static final UserInfoTableDef USER_INFO = new UserInfoTableDef();

    
    public final QueryColumn ID = new QueryColumn(this, "id");

    
    public final QueryColumn NAME = new QueryColumn(this, "name");

    
    public final QueryColumn TYPE = new QueryColumn(this, "type");

    
    public final QueryColumn PHONE = new QueryColumn(this, "phone");

    
    public final QueryColumn TOKEN = new QueryColumn(this, "token");

    
    public final QueryColumn ENABLE = new QueryColumn(this, "enable");

    
    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    
    public final QueryColumn ACCOUNT = new QueryColumn(this, "account");

    
    public final QueryColumn BALANCE = new QueryColumn(this, "balance");

    
    public final QueryColumn DISCOUNT = new QueryColumn(this, "discount");

    
    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    
    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, ACCOUNT, TOKEN, NAME, PHONE, ENABLE, TYPE, CREATE_TIME, UPDATE_TIME, DISCOUNT, BALANCE, REMARK};

    public UserInfoTableDef() {
        super("", "user_info");
    }

    private UserInfoTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public UserInfoTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new UserInfoTableDef("", "user_info", alias));
    }

}
