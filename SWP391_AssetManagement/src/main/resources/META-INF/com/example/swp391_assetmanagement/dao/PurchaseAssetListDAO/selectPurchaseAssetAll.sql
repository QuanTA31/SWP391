SELECT
    a.asset_code,
    a.asset_type_id,
    a.description,
    a.warranty_period,
    a.original_price,
    a.received_date,
    COUNT(1) OVER() AS total_items

FROM assets a

INNER JOIN assets_asset_request_external aare
    ON a.id = aare.assetId

INNER JOIN asset_external_request_detail aerd
    ON aerd.id = aare.assetExternalRequestDetailId

INNER JOIN asset_request ar
    ON ar.id = aerd.asset_request_id

WHERE ar.id = /* request.assetRequestId */''

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