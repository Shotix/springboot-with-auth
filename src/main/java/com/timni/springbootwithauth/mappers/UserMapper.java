package com.timni.springbootwithauth.mappers;

import com.timni.springbootwithauth.entities.User;
import com.timni.springbootwithauth.mappers.base.BaseMapper;
import com.timni.springbootwithauth.requests.user.CreateUserRequest;
import com.timni.springbootwithauth.requests.user.PatchUserRequest;
import com.timni.springbootwithauth.requests.user.UpdateUserRequest;
import com.timni.springbootwithauth.responses.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<
        User,
        CreateUserRequest,
        UpdateUserRequest,
        PatchUserRequest,
        UserResponse>
{}
