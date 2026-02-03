SELECT
    aird.asset_id             AS asset_id,            -- Khớp @Column(name="asset_id")
    ar.id                     AS asset_request_id,    -- Khớp @Column(name="asset_request_id")
    aird.asset_type_id        AS asset_type_id,       -- Khớp @Column(name="asset_type_id")
    aird.quantity             AS quantity,
    aird.from_location_id     AS from_location_id,
    aird.to_location_id       AS to_location_id,

    -- Lấy username (chữ) gán vào cột user_id để hiển thị
    uf.username               AS from_user_id,        -- Khớp @Column(name="from_user_id")
    ut.username               AS to_user_id,          -- Khớp @Column(name="to_user_id")

    aird.note                 AS note,
    aird.created_at           AS created_at,

    -- Tổng số dòng cho phân trang
    COUNT(1) OVER()           AS total_items          -- Khớp @Column(name="total_items")

FROM asset_internal_request_detail AS aird
         INNER JOIN asset_request AS ar ON aird.asset_request_id = ar.id
         LEFT JOIN users AS uf ON aird.from_user_id = uf.id
         LEFT JOIN users AS ut ON aird.to_user_id = ut.id

WHERE 1 = 1

/*%if request.requestStatusId != null && request.requestStatusId != "" */
  AND ar.request_status_id = /* request.requestStatusId */0
/*%end */

/*%if request.requestTypeId != null && request.requestTypeId != "" */
  AND ar.request_type_id = /* request.requestTypeId */0
/*%end */

ORDER BY aird.created_at DESC
    LIMIT /* request.pageSize */15
OFFSET /* request.offset */0