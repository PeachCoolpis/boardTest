package io.board.repository;

import io.board.entity.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResourcesRepository extends JpaRepository<Resources,Long> {
    
    Resources findByResourceNameAndHttpMethod(String resources, String httpMethod);
    
    @Query("select r from Resources r join fetch r.resourcesRoles rr join fetch rr.role where r.resourceType = 'url' ")
    List<Resources> findAllResources();
    
    
}
