package com.norbertkoziana.Session.Authentication.confirmation;
import com.norbertkoziana.Session.Authentication.data.TestDataUtil;
import com.norbertkoziana.Session.Authentication.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
class ConfirmationRepositoryIntegrationTest {

    private final ConfirmationRepository underTest;

    private final UserRepository userRepository;

    @Autowired
    ConfirmationRepositoryIntegrationTest(ConfirmationRepository underTest, UserRepository userRepository) {
        this.underTest = underTest;
        this.userRepository = userRepository;
    }

    @Test
    void ShouldFindFirstConfirmationByUserAndConfirmedFalseOrderByExpiresAtDesc() {
        //given
        userRepository.save(TestDataUtil.getUserA());
        userRepository.save(TestDataUtil.getUserB());

        underTest.save(TestDataUtil.getConfirmationA());
        underTest.save(TestDataUtil.getConfirmationB());
        Confirmation expectedConfirmation = TestDataUtil.getConfirmationC();
        underTest.save(expectedConfirmation);
        underTest.save(TestDataUtil.getConfirmationD());
        underTest.save(TestDataUtil.getConfirmationE());

        //when
        Confirmation actualConfirmation = underTest.findFirstByUserAndConfirmedFalseOrderByExpiresAtDesc(TestDataUtil.getUserA()).get();

        //then
        assertEquals(expectedConfirmation, actualConfirmation);
    }
}