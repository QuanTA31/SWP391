SELECT
    a.asset_code,
    a.asset_type_id,
    a.asset_status_id,
--    a.describe,
    a.warranty_period,
    a.original_price,
    ar.approved_date,
    COUNT(1) OVER() AS total_items

FROM assets a

LEFT JOIN assets_asset_request_external aare
    ON a.id = aare.assetId

LEFT JOIN asset_external_request_detail aerd
    ON aerd.id = aare.assetExternalRequestDetailId

LEFT JOIN asset_request ar
    ON ar.id = aerd.asset_request_id

WHERE 1 = 1

/*%if request.assetTypeId != null && request.assetTypeId != "" */
    AND a.asset_type_id = /* request.assetTypeId */''
/*%end */

/*%if request.searchWord != null && request.searchWord != "" */
    AND (
        a.asset_code LIKE CONCAT('%', /* request.searchWord */'', '%')
        OR ar.id LIKE CONCAT('%', /* request.searchWord */'', '%')
    )
/*%end */

ORDER BY a.created_at DESC

LIMIT /* request.pageSize */0
OFFSET /* request.offset */0