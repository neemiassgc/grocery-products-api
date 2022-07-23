package com.api.repository;

import com.api.entity.SessionStorage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

@SpringBootTest
public class SessionStorageRepositoryIT {

    @Autowired
    private SessionStorageRepository sessionStorageRepository;

    @Test
    @DisplayName("Testing findTopByOrderByCreationDateDesc method")
    void should_return_a_session() {
        final Optional<SessionStorage> actualOptional = sessionStorageRepository.findTopByOrderByCreationDateDesc();

        Assertions.assertThat(actualOptional).isNotNull();
        Assertions.assertThat(actualOptional).isPresent();
        Assertions.assertThat(actualOptional.get().getCreationDate()).isEqualTo(LocalDate.of(2022, Month.JULY, 21));
        Assertions.assertThat(actualOptional.get().getCookieKey()).isEqualTo("COOKIE_SAVEG_MOBILE");
        Assertions.assertThat(actualOptional.get().getCookieValue()).isEqualTo("ORA_WWV-1Ad8ftQ9pjqwWArvbfsdfler");
        Assertions.assertThat(actualOptional.get().getInstance()).isEqualTo(4078553061916L);
        Assertions.assertThat(actualOptional.get().getAjaxId()).isEqualTo("87C8742DE5B50364E11627DF664BB0B6D51B677A681CE819C015977E4F15331F");
    }
}