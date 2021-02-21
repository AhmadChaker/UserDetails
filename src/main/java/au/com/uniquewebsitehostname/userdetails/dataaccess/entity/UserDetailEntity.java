package au.com.uniquewebsitehostname.userdetails.dataaccess.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

import java.sql.Timestamp;


@Entity
@Setter
@Getter
@Table(name="USER_DETAIL")
public class UserDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Length(max=10)
    private String employeeId;
    @Length(max=10)
    private String title;
    @Length(max=40)
    private String firstName;
    @Length(max=40)
    private String lastName;
    @Length(max=10)
    private String gender;
    private Timestamp lastUpdatedDateTime;

    @OneToOne(mappedBy = "userDetail", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AddressEntity address;
}
