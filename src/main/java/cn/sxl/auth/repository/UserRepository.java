package cn.sxl.auth.repository;

import cn.sxl.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户 ORM
 *
 * @author SxL
 * @since Created on 12/19/2018 3:05 PM.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}
