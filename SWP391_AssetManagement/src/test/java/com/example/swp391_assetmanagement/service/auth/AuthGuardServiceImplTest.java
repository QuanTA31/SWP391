package com.example.swp391_assetmanagement.service.auth;

import com.example.swp391_assetmanagement.enums.Roles;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AuthGuardServiceImplTest {

    @Mock
    HttpSession session;

    AuthGuardServiceImpl authGuardService;

    @BeforeEach
    void setUp() {
        authGuardService = new AuthGuardServiceImpl(session);
    }

    @Test
    void should_pass_when_user_is_authenticated() {
        when(session.getAttribute("USER_ID")).thenReturn(1L);

        assertDoesNotThrow(() ->
                authGuardService.checkAuthenticated()
        );
    }

    @Test
    void should_throw_when_user_is_not_authenticated() {
        when(session.getAttribute("USER_ID")).thenReturn(null);

        ResponseStatusException ex =
                assertThrows(ResponseStatusException.class, () ->
                        authGuardService.checkAuthenticated()
                );

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    @Test
    void should_pass_when_role_matches() {
        when(session.getAttribute("ROLE")).thenReturn(Roles.MANAGER.getValue());

        assertDoesNotThrow(() ->
                authGuardService.canApprove()
        );
    }

    @Test
    void should_throw_when_not_manager() {
        // user đã login
        when(session.getAttribute("USER_ID")).thenReturn(1L);

        // role không phải MANAGER
        when(session.getAttribute("ROLE"))
                .thenReturn(Roles.PURCHASING.getValue());

        ResponseStatusException ex =
                assertThrows(ResponseStatusException.class,
                        () -> authGuardService.checkManager());

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }


    @Test
    void should_pass_when_purchasing() {
        // user đã login
        when(session.getAttribute("USER_ID")).thenReturn(1L);

        // role = PURCHASING
        when(session.getAttribute("ROLE"))
                .thenReturn(Roles.PURCHASING.getValue());

        assertDoesNotThrow(() ->
                authGuardService.checkManagerOrPurchasing());
    }


    @Test
    void canApprove_should_return_true_for_manager() {
        when(session.getAttribute("ROLE"))
                .thenReturn(Roles.MANAGER.getValue());

        assertTrue(authGuardService.canApprove());
    }

    @Test
    void canApprove_should_return_false_for_purchasing() {
        when(session.getAttribute("ROLE"))
                .thenReturn(Roles.PURCHASING.getValue());

        assertFalse(authGuardService.canApprove());
    }

    @Test
    void checkManager_should_pass_for_manager() {
        when(session.getAttribute("USER_ID")).thenReturn(1L);
        when(session.getAttribute("ROLE")).thenReturn("02"); // MANAGER

        assertDoesNotThrow(() ->
                authGuardService.checkManager()
        );
    }

    @Test
    void checkManager_should_throw_for_non_manager() {
        when(session.getAttribute("USER_ID")).thenReturn(1L);
        when(session.getAttribute("ROLE")).thenReturn("04"); // PURCHASING

        assertThrows(ResponseStatusException.class, () ->
                authGuardService.checkManager()
        );
    }

    @Test
    void should_pass_for_manager() {
        when(session.getAttribute("USER_ID")).thenReturn(1L);
        when(session.getAttribute("ROLE")).thenReturn("02"); // MANAGER

        assertDoesNotThrow(() ->
                authGuardService.checkManagerOrPurchasing()
        );
    }

    @Test
    void should_pass_for_purchasing() {
        when(session.getAttribute("USER_ID")).thenReturn(1L);
        when(session.getAttribute("ROLE")).thenReturn("04"); // PURCHASING

        assertDoesNotThrow(() ->
                authGuardService.checkManagerOrPurchasing()
        );
    }

    @Test
    void should_throw_for_other_roles() {
        when(session.getAttribute("USER_ID")).thenReturn(1L);
        when(session.getAttribute("ROLE")).thenReturn("03"); // WAREHOUSE

        assertThrows(ResponseStatusException.class, () ->
                authGuardService.checkManagerOrPurchasing()
        );
    }

    @Test
    void checkCanAccessRequest_should_pass_temporarily() {
        when(session.getAttribute("USER_ID")).thenReturn(1L);

        assertDoesNotThrow(() ->
                authGuardService.checkCanAccessRequest(100L)
        );
    }

}

