package au.com.uniquewebsitehostname.userdetails.dataaccess.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name="ADDRESS")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Length(max=100)
    private String street;
    @Length(max=30)
    private String city;
    @Length(max=10)
    private String postcode;
    @Length(max=20)
    private String state;
    @Length(max=20)
    private String country;

    @OneToOne
    @JoinColumn(name="USER_ID", referencedColumnName = "ID")
    private UserDetailEntity userDetail;
}
