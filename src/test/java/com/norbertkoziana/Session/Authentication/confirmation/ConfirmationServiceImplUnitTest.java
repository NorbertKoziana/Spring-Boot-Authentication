package com.norbertkoziana.Session.Authentication.confirmation;
import com.norbertkoziana.Session.Authentication.data.TestDataUtil;
import com.norbertkoziana.Session.Authentication.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConfirmationServiceImplUnitTest {

    @Mock
    private ConfirmationRepository confirmationRepository;

    ConfirmationServiceImpl underTest;

    @BeforeEach
    void setUp(){
        underTest = new ConfirmationServiceImpl(confirmationRepository);
    }

    @Test
    void NoTokenFoundShouldThrowException() {
        //given
        String token = TestDataUtil.getTokenA();

        //mock
        Mockito.doReturn(null).when(confirmationRepository).findByToken(token);

        //when and then
        assertThrows(Exception.class,() -> underTest.checkToken(token));
    }
    @Test
    void AlreadyConfirmedTokenShouldThrowException() {
        //given
        Confirmation confirmation = TestDataUtil.getConfirmationA();
        confirmation.setConfirmed(true);
        String token = confirmation.getToken();

        //mock
        Mockito.doReturn(Optional.of(confirmation)).when(confirmationRepository).findByToken(token);

        //when and then
        assertThrows(Exception.class,() -> underTest.checkToken(token));
    }
    @Test
    void AlreadyExpiredTokenShouldThrowException() {
        //given
        Confirmation confirmation = TestDataUtil.getConfirmationA();
        confirmation.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        String token = confirmation.getToken();

        //mock
        Mockito.doReturn(Optional.of(confirmation)).when(confirmationRepository).findByToken(token);

        //when and then
        assertThrows(Exception.class,() -> underTest.checkToken(token));
    }

    @Test
    void ProperTokenShouldBeReturned() {
        //given
        Confirmation confirmation = TestDataUtil.getConfirmationA();
        confirmation.setConfirmed(false);
        confirmation.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        String token = confirmation.getToken();

        //mock
        Mockito.doReturn(Optional.of(confirmation)).when(confirmationRepository).findByToken(token);

        //when
        Confirmation returnedConfirmation = underTest.checkToken(token);

        //then
        assertEquals(confirmation, returnedConfirmation);
    }

    @Test
    void ConfirmationWithLongExpiryTimeShouldReturnTrue(){
        //given
        Confirmation confirmation = TestDataUtil.getConfirmationA();
        confirmation.setExpiresAt(LocalDateTime.now().plusMinutes(5).plusSeconds(10));

        //when
        boolean result = underTest.checkIfConfirmationExpiryTimeIsAtLeast5Minutes(confirmation);

        //then
        assertTrue(result);
    }

    @Test
    void ConfirmationWithShortExpiryTimeShouldReturnFalse(){
        //given
        Confirmation confirmation = TestDataUtil.getConfirmationA();
        confirmation.setExpiresAt(LocalDateTime.now().plusMinutes(5).minusSeconds(10));

        //when
        boolean result = underTest.checkIfConfirmationExpiryTimeIsAtLeast5Minutes(confirmation);

        //then
        assertFalse(result);
    }

    @Test
    void ConfirmEmailShouldConfirmUserEmail(){
        //given
        User user = TestDataUtil.getUserC();
        user.setEnabled(false);

        Confirmation confirmation = TestDataUtil.getConfirmationC();
        confirmation.setUser(user);
        confirmation.setConfirmed(false);

        String token = confirmation.getToken();

        //mock
        ConfirmationServiceImpl spy = Mockito.spy(underTest);
        Mockito.doReturn(confirmation).when(spy).checkToken(token);

        //when
        spy.confirmEmail(token);

        //then
        verify(spy).checkToken(token);

        assertTrue(user.getEnabled());
        assertTrue(confirmation.getConfirmed());
    }

}