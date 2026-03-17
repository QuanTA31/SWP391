package com.example.swp391_assetmanagement.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMaintenanceRequestDTORequest {

    /** ID của asset được chọn để sửa */
    private Long assetId;

    /** Mô tả lỗi do manager nhập */
    private String issueDescription;

    /** Mức độ ưu tiên: LOW / MEDIUM / HIGH / CRITICAL */
    private String priority;

    /** Ghi chú thêm (tuỳ chọn) */
    private String note;

    /**
     * true  → Gửi ngay (request_status = APPROVED)
     * false → Lưu nháp (request_status = DRAFT)
     */
    private Boolean isSubmitted;
}
