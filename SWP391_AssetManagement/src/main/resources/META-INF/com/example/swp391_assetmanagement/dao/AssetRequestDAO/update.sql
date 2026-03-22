UPDATE asset_request
SET
    request_status_id = /* statusId */'APPROVED',
    approved_by = /* approvedBy */1,
    approved_date = CURRENT_DATE
WHERE id = /* id */1
