package au.com.uniquewebsitehostname.userdetails.dataaccess.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.Size;

@Entity
@Setter
@Getter
@ToString
@Table(name = "USER_AUTH")
public class UserAuthEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;

        @Size(max = 50)
        private String username;
        @Size(max = 60)
        private String password;
        @Size(max = 30)
        private String authorities;
}
