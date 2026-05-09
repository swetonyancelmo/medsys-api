package com.devsolutions.medsys.repository;

import com.devsolutions.medsys.model.UserRole;
import com.devsolutions.medsys.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}
