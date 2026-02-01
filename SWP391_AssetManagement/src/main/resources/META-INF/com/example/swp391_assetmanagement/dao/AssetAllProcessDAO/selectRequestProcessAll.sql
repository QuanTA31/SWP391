SELECT
    ar.request_type_id,
    ru.username       AS requested_by,
    ar.requested_date,
    ar.request_status_id,
    au.username       AS approval_by,
    ar.approved_date  AS approval_date,
    ar.handover_date,
    ar.note,
    ar.created_at,
    COUNT(1) OVER()   AS total_items
FROM asset_request ar
         INNER JOIN users ru
                    ON ar.requested_by = ru.id
         LEFT JOIN users au
                   ON ar.approved_by = au.id
         INNER JOIN request_type rt
                    ON ar.request_type_id = rt.id
         INNER JOIN request_status rs
                    ON ar.request_status_id = rs.id
WHERE 1 = 1

/*%if request.requestTypeId != null && request.requestTypeId != "" */
  AND ar.request_type_id = /* request.requestTypeId */''
/*%end */

/*%if request.requestStatusId != null && request.requestStatusId != "" */
  AND ar.request_status_id = /* request.requestStatusId */''
/*%end */

-- /*%if request.requestedBy != null && request.requestedBy != "" */
--   AND ru.username LIKE CONCAT('%', /* request.requestedBy */'', '%')
-- /*%end */

ORDER BY ar.created_at DESC
    LIMIT /* request.pageSize */0
OFFSET /* request.offset */0
