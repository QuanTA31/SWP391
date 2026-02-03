SELECT
    ar.id                     AS asset_request_id,    -- Map với @Column(name="asset_request_id")
    aerd.asset_type_id        AS asset_type_id,       -- Map với @Column(name="asset_type_id")
    aerd.external_status_id   AS external_status_id,  -- Map với @Column(name="external_status_id")
    aerd.quantity             AS quantity,
    aerd.note                 AS note,
    aerd.created_at           AS created_at,

    -- Tổng số dòng phục vụ phân trang
    COUNT(1) OVER()           AS total_items

FROM asset_external_request_detail AS aerd
         INNER JOIN asset_request AS ar ON aerd.asset_request_id = ar.id

WHERE 1 = 1

/*%if request.requestStatusId != null && request.requestStatusId != "" */
  AND ar.request_status_id = /* request.requestStatusId */0
/*%end */

/*%if request.requestTypeId != null && request.requestTypeId != "" */
  AND ar.request_type_id = /* request.requestTypeId */0
/*%end */

ORDER BY aerd.created_at DESC
    LIMIT /* request.pageSize */15
OFFSET /* request.offset */0