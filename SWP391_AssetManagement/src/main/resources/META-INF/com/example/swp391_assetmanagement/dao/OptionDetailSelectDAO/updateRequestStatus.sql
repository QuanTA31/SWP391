UPDATE asset_request
SET
    request_status_id = /* status */'01',
    approved_by = /* approvedBy */1,
    approved_date = /* approvedDate */'2024-01-01 00:00:00'
WHERE id = /* id */1