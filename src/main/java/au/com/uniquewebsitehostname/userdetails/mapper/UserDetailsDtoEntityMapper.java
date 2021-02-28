package au.com.uniquewebsitehostname.userdetails.mapper;

import au.com.uniquewebsitehostname.userdetails.dataaccess.entity.UserDetailEntity;
import au.com.uniquewebsitehostname.userdetails.dto.AddressDetailsDto;
import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.dto.UpdateUserDetailsServiceDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsDtoEntityMapper {

    public GetUserDetailsServiceDto mapEntityToGetUserDetailsDto(UserDetailEntity source) {
        GetUserDetailsServiceDto target = new GetUserDetailsServiceDto();
        target.setAddress(new AddressDetailsDto());
        BeanUtils.copyProperties(source, target);
        BeanUtils.copyProperties(source.getAddress(), target.getAddress());
        return target;
    }

    public UserDetailEntity mapUpdateUserDetailsDtoToEntity(UpdateUserDetailsServiceDto source, UserDetailEntity target) {
        BeanUtils.copyProperties(source, target);
        BeanUtils.copyProperties(source.getAddress(), target.getAddress());
        return target;
    }

}
