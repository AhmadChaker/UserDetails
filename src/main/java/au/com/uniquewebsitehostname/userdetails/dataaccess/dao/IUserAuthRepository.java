package au.com.uniquewebsitehostname.userdetails.dataaccess.dao;

import au.com.uniquewebsitehostname.userdetails.dataaccess.entity.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserAuthRepository extends JpaRepository<UserAuthEntity, Integer> {
    UserAuthEntity findByUsername(String employeeId);
}
