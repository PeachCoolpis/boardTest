package io.board.service.impl;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.board.dto.MemberDto;
import io.board.entity.*;
import io.board.repository.MemberRepository;
import io.board.repository.MemberRoleRepository;
import io.board.repository.RoleRepository;
import io.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JPAQueryFactory queryFactory;
    
    @Override
    @Transactional(readOnly = true)
    public List<MemberDto> findAllMember() {
        QMember member = QMember.member;
        QMemberRole memberRole = QMemberRole.memberRole;
        QRole role = QRole.role;
        
        List<MemberDto> memberDtos = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.id.as("id"),
                        member.username.as("username"),
                        member.password.as("password")
                ))
                .from(member)
                .fetch();
        
        
        for (MemberDto memberDto : memberDtos) {
            List<String> roles = queryFactory
                    .select(role.roleName)
                    .from(memberRole)
                    .join(member).on(memberRole.id.eq(member.id))
                    .join(role).on(memberRole.id.eq(role.id))
                    .where(memberRole.member.id.eq(memberDto.getId()))
                    .fetch();
            
            memberDto.setRoles(roles);
        }
        return memberDtos;
    }
    
    
    @Override
    public Long createMember(MemberDto memberDto) {
        Member createMember = Member.createMember(memberDto);
        createMember.passSetting(passwordEncoder.encode(memberDto.getPassword()));
        Member member = memberRepository.save(createMember);
        Role role = roleRepository.findByName("ROLE_USER");
        roleRepository.save(role);
        MemberRole memberRole = MemberRole.createMemberRole(member, role);
        memberRoleRepository.save(memberRole);
        return member.getId();
    }
    
    @Override
    @Transactional(readOnly = true)
    public MemberDto findMember(Long id) {
        QMember member = QMember.member;
        QMemberRole memberRole = QMemberRole.memberRole;
        QRole role = QRole.role;
        
        
        List<String> roles = getList(id, role, memberRole, member);
        
        
        MemberDto memberDto = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.id,
                        member.username,
                        member.password
                ))
                .from(member)
                .where(member.id.eq(id))
                .fetchOne();
        
        if (memberDto != null) {
            memberDto.setRoles(roles);
        }
        return memberDto;
    }
    
    @Override
    public Long modifyMember(Long id ,MemberDto memberDto) {
        
        QMember member = QMember.member;
        QMemberRole memberRole = QMemberRole.memberRole;
        QRole role = QRole.role;
        
        
        return null;
    }
    
    private List<String> getList(Long id, QRole role, QMemberRole memberRole, QMember member) {
        if (id == null) {
            return queryFactory
                    .select(role.roleName)
                    .from(memberRole)
                    .join(member).on(memberRole.id.eq(member.id))
                    .join(role).on(memberRole.id.eq(member.id))
                    .fetch();
        } else {
            return queryFactory
                    .select(role.roleName)
                    .from(memberRole)
                    .join(member).on(memberRole.id.eq(member.id))
                    .join(role).on(memberRole.id.eq(member.id))
                    .where(member.id.eq(id))
                    .fetch();
        }
    }
}
