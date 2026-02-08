package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.auth.AuthGuardService;
import com.example.swp391_assetmanagement.dto.request.OptionDetailListRequest;
import com.example.swp391_assetmanagement.dto.response.OptionDetailListResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetOptionDetailListUseCaseTest {

    @Mock
    OptionDetailService optionDetailService;

    @Mock
    AuthGuardService authGuardService;

    @InjectMocks
    GetOptionDetailListUseCase useCase;

    @Test
    void should_return_first_page_with_all_status() {
        // given
        Long requestDetailId = 100L;
        String status = "all";
        Integer page = 1;

        List<OptionDetail> plans = List.of(new OptionDetail(), new OptionDetail());

        when(optionDetailService.getList(any()))
                .thenReturn(plans);
        when(optionDetailService.count(any()))
                .thenReturn(2);

        // when
        OptionDetailListResponse result =
                useCase.execute(requestDetailId, status, page);

        // then
        verify(authGuardService).checkManagerOrPurchasing();
        verify(authGuardService).checkCanAccessRequest(requestDetailId);
        verify(authGuardService).checkCanAccessRequest(requestDetailId);

        ArgumentCaptor<OptionDetailListRequest> captor =
                ArgumentCaptor.forClass(OptionDetailListRequest.class);

        verify(optionDetailService).getList(captor.capture());
        verify(optionDetailService).count(captor.capture());

        OptionDetailListRequest req = captor.getValue();

        assertThat(req.getRequestDetailId()).isEqualTo(requestDetailId);
        assertThat(req.getIsSelected()).isNull(); // all
        assertThat(req.getOffset()).isEqualTo(0);
        assertThat(req.getPageSize()).isEqualTo(10);

        assertThat(result.getPlans()).hasSize(2);
        assertThat(result.getPage()).isEqualTo(1);
        assertThat(result.getTotalItems()).isEqualTo(2);
    }

    @Test
    void should_return_selected_items_page_2() {
        // given
        Long requestDetailId = 200L;
        String status = "selected";
        Integer page = 2;

        when(optionDetailService.getList(any()))
                .thenReturn(List.of(new OptionDetail()));
        when(optionDetailService.count(any()))
                .thenReturn(11);

        // when
        OptionDetailListResponse result =
                useCase.execute(requestDetailId, status, page);

        // then
        ArgumentCaptor<OptionDetailListRequest> captor =
                ArgumentCaptor.forClass(OptionDetailListRequest.class);

        verify(optionDetailService).getList(captor.capture());
        OptionDetailListRequest req = captor.getValue();

        assertThat(req.getIsSelected()).isTrue();
        assertThat(req.getOffset()).isEqualTo(10); // page 2
        assertThat(result.getPage()).isEqualTo(2);
    }

    @Test
    void should_throw_exception_when_status_invalid() {
        assertThat(
                org.junit.jupiter.api.Assertions.assertThrows(
                        IllegalArgumentException.class,
                        () -> useCase.execute(1L, "abc", 1)
                ).getMessage()
        ).contains("Invalid status");
    }



}
