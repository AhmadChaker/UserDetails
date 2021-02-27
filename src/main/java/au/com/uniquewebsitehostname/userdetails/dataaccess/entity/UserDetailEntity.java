package au.com.uniquewebsitehostname.userdetails.dataaccess.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.validation.constraints.Size;

import java.sql.Timestamp;


@Entity
@Setter
@Getter
@ToString
@Table(name="USER_DETAIL")
public class UserDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Size(max=10)
    private String employeeId;
    @Size(max=10)
    private String title;
    @Size(max=40)
    private String firstName;
    @Size(max=40)
    private String lastName;
    @Size(max=10)
    private String gender;
    private Timestamp lastUpdatedDateTime;

    @OneToOne(mappedBy = "userDetail", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AddressEntity address;
}
