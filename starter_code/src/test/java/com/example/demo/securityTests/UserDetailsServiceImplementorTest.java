package com.example.demo.securityTests;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.junit.jupiter.api.function.Executable;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplementorTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Mock
    UserRepository userRepository;

    @Test
    public void testLoadUserByUsername() {
        String username = "test";
        User user = new User();
        user.setUsername(username);
        String password = "password";
        user.setPassword(password);
        user.setId(0L);
        when(userRepository.findByUsername(username)).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertNotNull(userDetails);

        Collection<? extends GrantedAuthority> authorityCollection = userDetails.getAuthorities();
        assertNotNull(authorityCollection);
        assertEquals(0, authorityCollection.size());

        assertEquals(password, userDetails.getPassword());
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(null);

        Executable executable = () -> userDetailsService.loadUserByUsername(username);
        assertThrows(UsernameNotFoundException.class, executable);
    }


    @Test
    public void testLoadUserByUsername_NullUsername() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(null);
        });
    }
}

