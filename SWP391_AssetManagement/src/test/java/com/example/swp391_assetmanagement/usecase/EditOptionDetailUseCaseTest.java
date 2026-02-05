package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.auth.AuthGuardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditOptionDetailUseCaseTest {

    @Mock
    OptionDetailService optionDetailService;

    @Mock
    private AuthGuardService authGuardService;

    @InjectMocks
    EditOptionDetailUseCase useCase;

    @Test
    void should_update_existing_option() {
        OptionDetail existing = new OptionDetail();
        existing.id = 1L;

        OptionDetail input = new OptionDetail();
        input.id = 1L;
        input.merchant = "HP";

        when(optionDetailService.getById(1L))
                .thenReturn(Optional.of(existing));

        //useCase.execute(100L, List.of(input));

        assertEquals("HP", existing.merchant);
        verify(optionDetailService).update(existing);
    }
}
