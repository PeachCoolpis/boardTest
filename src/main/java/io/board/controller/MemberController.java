package io.board.controller;


import io.board.dto.MemberDto;
import io.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class MemberController {
    
    private final MemberService memberService;
    
    @PostMapping("/save")
    public Long createMember(@RequestBody MemberDto memberDto) {
        Long member = memberService.createMember(memberDto);
        return member;
    }
    
    @GetMapping("/findAll")
    public List<MemberDto> member() {
        return memberService.findAllMember();
    }
    
    
}
