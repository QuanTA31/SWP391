UPDATE asset_request
SET
    request_status_id = /* statusId */'APPROVED',
    approved_by = /* approvedBy */1,
    approved_date = CURRENT_DATE, -- Dùng trực tiếp hàm của DB
    note = /* note */'Updated via custom SQL'
WHERE id = /* id */1;