SELECT
    d.id as detailId,
    a.asset_code as assetCode,
    a.asset_type_id as assetTypeId,
    ud.name as userFullName,
    d.is_done as isDone,
    a.id as assetId,
    a.asset_status_id as statusId -- THÊM DÒNG NÀY
FROM asset_internal_request_detail d
JOIN assets a ON d.asset_id = a.id
LEFT JOIN user_detail ud ON d.from_user_id = ud.user_id
WHERE d.asset_request_id = /* requestId */76
ORDER BY d.is_done ASC, a.asset_code ASC