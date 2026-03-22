SELECT
    id,
    asset_code,
    asset_status_id,
    asset_type_id,
    warranty_period,
    original_price,
    description,
    current_user_id,
    location_id,
    depreciation,
    received_date,
    note,
    created_at
FROM
    assets
WHERE
    id = /* id */1