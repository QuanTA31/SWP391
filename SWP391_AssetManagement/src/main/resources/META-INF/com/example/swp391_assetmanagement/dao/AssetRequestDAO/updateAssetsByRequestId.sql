UPDATE assets
SET status_id = /* status */'06'
WHERE id IN (
    SELECT asset_id
    FROM assets_asset_request_external
    WHERE asset_external_request_detail_id IN (
        SELECT id
        FROM asset_external_request_detail
        WHERE asset_request_id = /* requestId */1
    )
)