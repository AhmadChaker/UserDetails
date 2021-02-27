package au.com.uniquewebsitehostname.userdetails.mapper;

import au.com.uniquewebsitehostname.userdetails.api.AddressDetails;
import au.com.uniquewebsitehostname.userdetails.api.GetUserDetailsResponse;
import au.com.uniquewebsitehostname.userdetails.api.UpdateUserDetailsRequest;
import au.com.uniquewebsitehostname.userdetails.dto.AddressDetailsDto;
import au.com.uniquewebsitehostname.userdetails.dto.GetUserDetailsServiceDto;
import au.com.uniquewebsitehostname.userdetails.dto.UpdateUserDetailsServiceDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsDtoRequestResponseMapper {

    public GetUserDetailsResponse map(GetUserDetailsServiceDto source) {
        GetUserDetailsResponse target = new GetUserDetailsResponse();
        target.setAddress(new AddressDetails());
        BeanUtils.copyProperties(source, target);
        BeanUtils.copyProperties(source.getAddress(), target.getAddress());
        return target;
    }

    public UpdateUserDetailsServiceDto map(UpdateUserDetailsRequest source, String oldEmployeeId) {
        UpdateUserDetailsServiceDto target = new UpdateUserDetailsServiceDto();
        target.setAddress(new AddressDetailsDto());
        target.setOldEmployeeId(oldEmployeeId);
        BeanUtils.copyProperties(source, target);
        BeanUtils.copyProperties(source.getAddress(), target.getAddress());
        return target;
    }
}
