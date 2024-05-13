package io.board.controller;


import io.board.dto.MemberDto;
import io.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;
    
    @GetMapping("/save")
    public Long createMember(@Validated @RequestBody MemberDto memberDto) {
        return memberService.createMember(memberDto);
    }
}
