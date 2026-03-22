UPDATE asset_request
SET
    request_status_id = /* assetRequest.requestStatusId */'APPROVED',
    approved_by = /* assetRequest.approvedBy */1,
    note = /* assetRequest.note */'',
    approved_date = CURRENT_DATE
WHERE id = /* assetRequest.id */1
