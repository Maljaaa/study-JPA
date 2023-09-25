package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepositoryNew;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // rollback하기 위해서
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepositoryNew memberRepository;
    @Autowired
    EntityManager entityManager;

    @Test
//    @Rollback(value = false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("shin");

        //when
        Long saveId = memberService.join(member);

        //then
        entityManager.flush();
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예약() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("shin");

        Member member2 = new Member();
        member2.setName("shin");

        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
        fail("예외가 발생해야 하는데 안 나오네");
    }

}