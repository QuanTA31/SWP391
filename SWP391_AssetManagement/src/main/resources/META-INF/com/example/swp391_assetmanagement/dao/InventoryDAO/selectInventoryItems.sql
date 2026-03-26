SELECT
    d.id as detail_id,
    a.asset_code as asset_code,
    a.asset_type_id as asset_type_id,
    ud.name as name,
    d.is_done as is_done,
    a.id as asset_id,
    a.asset_status_id as status_id
FROM asset_internal_request_detail d
JOIN assets a ON d.asset_id = a.id
LEFT JOIN user_detail ud ON d.from_user_id = ud.user_id
WHERE d.asset_request_id = /* requestId */76

/*%if assetTypeId != null && assetTypeId != "" */
  AND a.asset_type_id = /* assetTypeId */'01'
/*%end*/

/*%if fullName != null && fullName != "" */
  AND Lower(ud.name) LIKE Lower(CONCAT('%', /* fullName */'lap', '%'))

/*%end*/

ORDER BY d.is_done ASC, a.asset_code ASC