package cn.sxl.auth.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 用户实体类
 * @author SxL
 * @since
 * Created on 12/19/2018 3:03 PM.
 */
@Data
@Entity
@Table(name = "user")
public class User {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String secret;
}
