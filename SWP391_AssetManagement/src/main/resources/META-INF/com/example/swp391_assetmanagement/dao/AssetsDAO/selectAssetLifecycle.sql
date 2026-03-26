SELECT
    ar.id                       AS request_id,
    ar.request_type_id          AS request_type_id,
    ar.request_status_id        AS request_status_id,
    ar.requested_date           AS requested_date,
    ar.requested_by             AS requested_by,
    ur.name                     AS requested_by_name,
    ar.approved_date            AS approved_date,
    ar.approved_by              AS approved_by,
    ua.name                     AS approved_by_name,
    ar.handover_date            AS handover_date,
    ar.note                     AS note,
    COUNT(1) OVER ()            AS total_items
FROM asset_request ar
LEFT JOIN user_detail ur ON ur.user_id = ar.requested_by
LEFT JOIN user_detail ua ON ua.user_id = ar.approved_by
WHERE (ar.id IN (
    SELECT aird.asset_request_id
    FROM asset_internal_request_detail aird
    INNER JOIN assets a ON a.id = aird.asset_id
    WHERE a.asset_code = /* request.assetCode */''
) OR ar.id IN (
    SELECT aerd.asset_request_id
    FROM asset_external_request_detail aerd
    INNER JOIN assets_asset_request_external aare ON aare.assetExternalRequestDetailId = aerd.id
    INNER JOIN assets a ON a.id = aare.assetId
    WHERE a.asset_code = /* request.assetCode */''
))
/*%if request.requestTypeId != null && request.requestTypeId != "" */
  AND ar.request_type_id = /* request.requestTypeId */''
/*%end */
ORDER BY ar.created_at DESC
LIMIT /* request.pageSize */0
OFFSET /* request.offset */0
