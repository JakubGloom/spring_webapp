package net.atos.spring_webapp.repository;

import net.atos.spring_webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(
            value = "select permission_id, count(*) from user_permission group by permission_id",
            nativeQuery = true)
    List<Object[]> aggregatePermissions();

    @Query(
            value = "select p.role_name, count(*) from user u join user_permission up on (u.user_id = up.user_id) join permission p on (p.permission_id = up.permission_id) group by p.role_name ORDER BY 2",
            nativeQuery = true
    )
    List<Object[]> aggregatePermissionsByRoleName();

    @Modifying
    @Query(
            value = "UPDATE user SET enable = :enable WHERE user_id = :user_id",
            nativeQuery = true
    )
    @Transactional
    void updateUserStatus(
            @Param("enable") boolean enable, @Param("user_id") int userId
    );

    @Modifying
    @Query(
            value = "DELETE FROM user_permission WHERE user_id = ?",
            nativeQuery = true
    )
    @Transactional
    void deleteUserRoles(int userId);


    @Query("SELECT u FROM User u WHERE u.enable = :status")
    List<User> getAllUsersWhereStatus(@Param("status") boolean status);

    List<User> findAllByEnable(boolean enable);

    @Transactional(rollbackFor = Exception.class)
    @Override
    User save(User user);

    @Transactional(rollbackFor = Exception.class)
    @Override
    <S extends User> List<S> saveAll(Iterable<S> iterable);

    User findFirstByEmail(String email);
}
