package com.web.spring_boot_securitymaster.dao;

import com.web.spring_boot_securitymaster.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface RoleDao extends JpaRepository<Role, Long> {

    List<Role> findAll();

    Role findRoleByRole(String authority);


}
