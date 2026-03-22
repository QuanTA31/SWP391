SELECT
    ar.id             AS request_id,
    ar.request_type_id,
    ru.username       AS requested_by,
    ar.requested_date,
    ar.request_status_id,
    au.username       AS approval_by,
    ar.approved_date  AS approval_date,
    ar.handover_date,
    ar.note,
    ar.created_at,
    aid.asset_type_id,
    aid.is_done,
    aid.quantity,
    aid.note          AS aid_note,
    COUNT(1) OVER()   AS total_items
FROM asset_request ar
         LEFT JOIN users ru
                    ON ar.requested_by = ru.id
         LEFT JOIN users au
                   ON ar.approved_by = au.id
         LEFT JOIN request_type rt
                    ON ar.request_type_id = rt.id
         LEFT JOIN request_status rs
                    ON ar.request_status_id = rs.id
--     them cho allocation
         LEFT JOIN asset_internal_request_detail aid
                    ON ar.id = aid.asset_request_id
WHERE 1 = 1

/*%if request.requestTypeIdList != null && !request.requestTypeIdList.isEmpty() */
  AND ar.request_type_id IN /* request.requestTypeIdList */('01')
/*%end */

/*%if request.requestTypeId != null && request.requestTypeId != "" */
  AND ar.request_type_id = /* request.requestTypeId */''
/*%end */

/*%if request.requestStatusId != null && request.requestStatusId != "" */
  AND ar.request_status_id = /* request.requestStatusId */''
/*%end */

-- Allocation
/*%if request.excludeStatusIdList != null && !request.excludeStatusIdList.isEmpty() */
  AND ar.request_status_id NOT IN /* request.excludeStatusIdList */('00')
/*%end */

-- /*%if request.requestedBy != null && request.requestedBy != "" */
--   AND ru.username LIKE CONCAT('%', /* request.requestedBy */'', '%')
-- /*%end */

ORDER BY ar.created_at DESC
    LIMIT /* request.pageSize */15
OFFSET /* request.offset */0
