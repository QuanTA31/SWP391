SELECT
    *
FROM
    assets
WHERE
    asset_type_id = /* typeId */'01'
    AND asset_status_id IN ('01', '08')
ORDER BY
    created_at DESC
