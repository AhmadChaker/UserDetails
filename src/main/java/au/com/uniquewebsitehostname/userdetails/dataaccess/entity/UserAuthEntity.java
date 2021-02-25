package au.com.uniquewebsitehostname.userdetails.dataaccess.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Setter
@Getter
@ToString
@Table(name="USER_AUTH")
public class UserAuthEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;

        @Length(max=50)
        private String username;
        @Length(max=60)
        private String password;
        @Length(max=30)
        private String authorities;
}
