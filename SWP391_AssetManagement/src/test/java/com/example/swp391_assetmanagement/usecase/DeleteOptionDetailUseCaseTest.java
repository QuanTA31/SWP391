package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.auth.AuthGuardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteOptionDetailUseCaseTest {

    @Mock
    OptionDetailService optionDetailService;

    @Mock
    AuthGuardService authGuardService;

    @InjectMocks
    DeleteOptionDetailUseCase useCase;

    @Test
    void should_delete_option_detail_by_id() {
        // given
        Long id = 10L;

        // when
        useCase.execute(id);

        // then
        verify(optionDetailService).deleteById(id);
        verifyNoMoreInteractions(optionDetailService);
    }
}

