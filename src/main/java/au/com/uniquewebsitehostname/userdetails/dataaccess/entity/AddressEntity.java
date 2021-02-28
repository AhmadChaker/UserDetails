package au.com.uniquewebsitehostname.userdetails.dataaccess.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Setter
@Getter
@Table(name = "ADDRESS")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Size(max = 100)
    private String street;
    @NotNull
    @Size(max = 30)
    private String city;
    @NotNull
    @Size(max = 10)
    private String postcode;
    @Size(max = 20)
    private String state;
    @NotNull
    @Size(max = 20)
    private String country;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private UserDetailEntity userDetail;

    // Manually override to exclude userDetail due to circular reference
    @Override
    public String toString() {
        return "AddressEntity{"
                + "street='" + street + '\''
                + ", city='" + city + '\''
                + ", postcode='" + postcode + '\''
                + ", state='" + state + '\''
                + ", country='" + country + '\''
                + '}';
    }
}
