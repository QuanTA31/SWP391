package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.OptionDetailFormRequest;
import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.auth.AuthGuardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOptionDetailUseCaseTest {

    @Mock
    OptionDetailService optionDetailService;

    @Mock
    private AuthGuardService authGuardService;



    @InjectMocks
    CreateOptionDetailUseCase useCase;

    @Test
    void should_set_default_fields_and_save_all() {
        Long requestDetailId = 100L;

        OptionDetail option = new OptionDetail();
        option.merchant = "Dell";

        List<OptionDetail> plans = List.of(option);

        //useCase.execute(requestDetailId, form);

        assertEquals(requestDetailId, option.assetExternalRequestDetailId);
        assertFalse(option.isSelected);
        assertNull(option.approvedDate);
        assertNull(option.approverBy);

        verify(optionDetailService).saveAll(plans);
    }
}
