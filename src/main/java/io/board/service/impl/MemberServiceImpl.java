package io.board.service.impl;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        
        List<Member> members = queryFactory
                .select(member)
                .from(member)
                .join(memberRole).on(member.id.eq(memberRole.member.id)).fetchJoin()
                .join(role).on(memberRole.role.id.eq(role.id))
                .fetch();
        
        
        return members.stream()
                .map(m -> new MemberDto(
                        m.getId(),
                        m.getUsername(),
                        null,
                        m.getMemberRoles().stream().map(a -> a.getRole().getRoleName()).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
    
    
    @Override
    public Long createMember(MemberDto memberDto) {
        Member createMember = Member.createMember(memberDto);
        createMember.passSetting(passwordEncoder.encode(memberDto.getPassword()));
        Member member = memberRepository.save(createMember);
        Role role = roleRepository.findByName(memberDto.getMemberRoles().stream().findFirst().orElse(null));
        MemberRole memberRole = new MemberRole();
        memberRole.setMember(member);
        memberRole.setRole(role);
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
        
//        if (memberDto != null) {
//            memberDto.setRoles(roles);
//        }
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
