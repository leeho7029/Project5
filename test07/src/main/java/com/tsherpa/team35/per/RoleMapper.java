package com.tsherpa.team35.per;

import com.tsherpa.team35.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface RoleMapper {

    Role getRoleInfo(@Param("role") String role);

}