package com.official.pium.repository;

import com.official.pium.RepositoryTest;
import com.official.pium.domain.Member;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 사용자_저장() {
        Member member = Member.builder()
                .email("hello@aaa.com")
                .build();

        Member save = memberRepository.save(member);

        assertAll(
                () -> assertThat(save).isNotNull(),
                () -> assertThat(save.getId()).isEqualTo(member.getId())
        );
    }

    @Test
    void 사용자_조회() {
        Member member = Member.builder().email("hello@aaa.com").build();

        Member save = memberRepository.save(member);

        assertThat(memberRepository.findById(save.getId())).isPresent();
    }
}
