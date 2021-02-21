package au.com.uniquewebsitehostname.userdetails.dataaccess.dao;

import au.com.uniquewebsitehostname.userdetails.dataaccess.entity.UserDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDetailsRepository extends JpaRepository<UserDetailEntity, Integer> {
    UserDetailEntity findByEmployeeId(String employeeId);
}
