SELECT
    ar.id              AS request_id,
    ar.request_type_id,
    ru.username        AS requested_by,
    ar.requested_date,
    ar.request_status_id,
    au.username        AS approval_by,
    ar.approved_date   AS approval_date,
    ar.handover_date,
    ar.note,
    ar.created_at,
    -- XÓA CÁC TRƯỜNG aid.asset_type_id, aid.quantity ... ở đây
    COUNT(1) OVER()    AS total_items
FROM asset_request ar
    LEFT JOIN users ru ON ar.requested_by = ru.id
    LEFT JOIN users au ON ar.approved_by = au.id
    LEFT JOIN request_type rt ON ar.request_type_id = rt.id
    LEFT JOIN request_status rs ON ar.request_status_id = rs.id
-- XÓA DÒNG LEFT JOIN aid Ở ĐÂY
WHERE 1 = 1
  AND ar.request_type_id IN ('01', '02', '03', '04', '05')
  AND ar.request_status_id NOT IN ('01')
ORDER BY ar.created_at DESC
LIMIT 15 OFFSET 0