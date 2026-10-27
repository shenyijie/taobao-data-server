package com.shine.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  实体类。
 *
 * @author Sky
 * @since 2025-08-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("user_info")
public class UserInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    private String account;

    private String token;

    private String name;

    private String phone;

    private Integer enable;

    private Integer type;

    private Long createTime;

    private Long updateTime;

    private Integer discount;

    private Float balance;

    private String remark;

}
