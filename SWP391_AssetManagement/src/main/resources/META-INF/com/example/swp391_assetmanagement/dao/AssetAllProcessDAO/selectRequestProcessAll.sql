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
    aid.note          AS aid_note,
    COALESCE(aid_count.qty, 0) AS quantity,
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
--     them cho allocation: only pick the first detail row per request (avoids N-row duplication)
         LEFT JOIN (
             SELECT *
             FROM asset_internal_request_detail
             WHERE id IN (
                 SELECT MIN(id) FROM asset_internal_request_detail GROUP BY asset_request_id
             )
         ) aid ON ar.id = aid.asset_request_id
--     count total detail records per request (= true requested quantity in N-records model)
         LEFT JOIN (
             SELECT asset_request_id, COUNT(*) AS qty
             FROM asset_internal_request_detail
             GROUP BY asset_request_id
         ) aid_count ON ar.id = aid_count.asset_request_id
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

-- Department
/*%if request.departmentId != null */
  AND ar.requested_by = /* request.departmentId */1
/*%end */

ORDER BY ar.created_at DESC
    LIMIT /* request.pageSize */15
OFFSET /* request.offset */0
