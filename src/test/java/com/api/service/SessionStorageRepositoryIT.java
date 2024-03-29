package com.api.service;

import com.api.entity.SessionStorage;
import com.api.service.interfaces.SessionStorageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Optional;

@SpringBootTest
public class SessionStorageRepositoryIT {

    @Autowired
    private SessionStorageService sessionStorageService;

    @Test
    @DisplayName("Testing findTopBy method")
    void should_return_a_session() {
        final Sort.Order orderByCreationDateDesc = Sort.Order.desc("creationDate");
        final Sort.Order orderByIdAsc = Sort.Order.asc("id");
        final Optional<SessionStorage> actualOptional =
            sessionStorageService.findTopBy(Sort.by(orderByCreationDateDesc, orderByIdAsc));

        Assertions.assertThat(actualOptional).isNotNull();
        Assertions.assertThat(actualOptional).isPresent();
        Assertions.assertThat(actualOptional.get().getCreationDate()).isEqualTo(LocalDate.now(ZoneId.of("UTC")));
    }
}
