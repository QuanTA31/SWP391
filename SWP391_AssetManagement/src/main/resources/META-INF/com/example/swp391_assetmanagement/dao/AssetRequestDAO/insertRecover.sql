INSERT INTO asset_request (
    request_type_id,
    requested_by,
    requested_date,
    request_status_id,
    approved_by,   -- Cột người duyệt
    approved_date, -- Cột ngày duyệt
    note
) VALUES (
    /* assetRequest.requestTypeId */'02',
    /* assetRequest.requestedBy */1,
    /* assetRequest.requestedDate */'2026-03-22',
    /* assetRequest.requestStatusId */'03',
    /* assetRequest.approvedBy */1,    -- Sẽ nhận giá trị userId truyền vào
    /* assetRequest.approvedDate */'2026-03-22', -- Sẽ nhận ngày hiện tại
    /* assetRequest.note */null
)