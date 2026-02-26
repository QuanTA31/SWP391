UPDATE asset_request
SET
    request_status_id = /* assetRequest.requestStatusId */'APPROVED',
    approved_by = /* assetRequest.approvedBy */1,
    approved_date = CURRENT_DATE,
    note = /* assetRequest.note */'Updated via custom SQL'
WHERE id = /* assetRequest.id */1;