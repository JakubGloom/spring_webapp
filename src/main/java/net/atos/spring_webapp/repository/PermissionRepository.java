package net.atos.spring_webapp.repository;

import net.atos.spring_webapp.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Byte> {
}
