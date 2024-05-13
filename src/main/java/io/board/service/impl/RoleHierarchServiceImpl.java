package io.board.service.impl;

import io.board.entity.RoleHierarchy;
import io.board.repository.RoleHierarchRepository;
import io.board.service.RoleHierarchyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RoleHierarchServiceImpl implements RoleHierarchyService {
    
    private final RoleHierarchRepository roleHierarchRepository;
    
    @Override
    @Transactional(readOnly = true)
    public String findAllHierarchy() {
        List<RoleHierarchy> hierarchyList = roleHierarchRepository.findAll();
        Iterator<RoleHierarchy> iterator = hierarchyList.iterator();
        StringBuilder strRole = new StringBuilder();
        
        while (iterator.hasNext()) {
            RoleHierarchy roleHierarchy = iterator.next();
            if (roleHierarchy.getParent() != null) {
                strRole.append(roleHierarchy.getParent().getRoleName());
                strRole.append(" > ");
                strRole.append(roleHierarchy.getRoleName());
                strRole.append("\n");
            }
        }
        return strRole.toString();
    }
}
