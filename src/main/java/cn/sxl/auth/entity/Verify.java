package cn.sxl.auth.entity;

import lombok.Data;

/**
 * 验证对象
 *
 * @author SxL
 * @since Created on 12/26/2018 2:38 PM.
 */
@Data
public class Verify {

    private String email;

    private Long code;
}
