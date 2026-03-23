UPDATE assets
SET asset_status_id = /* status */0
WHERE id IN (
    SELECT asset_id
    FROM asset_external_request_detail
    WHERE asset_request_id = /* requestId */0
);