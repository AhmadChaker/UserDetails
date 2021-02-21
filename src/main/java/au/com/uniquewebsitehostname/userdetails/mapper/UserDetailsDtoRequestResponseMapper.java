package au.com.uniquewebsitehostname.userdetails.mapper;

import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsResponseDto;
import au.com.uniquewebsitehostname.userdetails.dto.UpdateUserDetailsRequestDto;
import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.dto.UpdateUserDetailsServiceDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsDtoRequestResponseMapper {

    public GetUserDetailsResponseDto map(GetUserDetailsServiceDto source) {
        GetUserDetailsResponseDto target = new GetUserDetailsResponseDto();
        target.setAddress(target.new AddressDetails());
        BeanUtils.copyProperties(source, target);
        BeanUtils.copyProperties(source.getAddress(), target.getAddress());
        return target;
    }

    public UpdateUserDetailsServiceDto map(UpdateUserDetailsRequestDto source, String oldEmployeeId) {
        UpdateUserDetailsServiceDto target = new UpdateUserDetailsServiceDto();
        target.setAddress(target.new AddressDetails());
        target.setOldEmployeeId(oldEmployeeId);
        BeanUtils.copyProperties(source, target);
        BeanUtils.copyProperties(source.getAddress(), target.getAddress());
        return target;
    }
}
