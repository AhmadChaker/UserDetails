package au.com.uniquewebsitehostname.userdetails.mapper;

import au.com.uniquewebsitehostname.userdetails.dataaccess.entity.UserDetailEntity;
import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.dto.UpdateUserDetailsServiceDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsDtoEntityMapper {

    public GetUserDetailsServiceDto map(UserDetailEntity source) {
        GetUserDetailsServiceDto target = new GetUserDetailsServiceDto();
        target.setAddress(target.new AddressDetails());
        BeanUtils.copyProperties(source, target);
        BeanUtils.copyProperties(source.getAddress(), target.getAddress());
        return target;
    }

    public UserDetailEntity map(UpdateUserDetailsServiceDto source, UserDetailEntity target) {
        BeanUtils.copyProperties(source, target);
        BeanUtils.copyProperties(source.getAddress(), target.getAddress());
        return target;
    }

}
