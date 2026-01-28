SELECT aip.asset_id,
       aip.request_status_id,
       aip.request_type_id,
       aip.from_user_id,
       aip.to_user_id,
       aip.date_of_execution,
       aip.handover_date,
       aip.note,
       rpm.approval_status_id,
       COUNT(1) OVER() AS total_items
FROM asset_internal_process AS aip
         INNER JOIN request_progress_management AS rpm
                    ON aip.request_progress_management_id = rpm.id

WHERE 1 = 1
/*%if request.requestStatusId != null && request.requestStatusId != "" */
  AND aip.request_status_id = /* request.requestStatusId */0
/*%end */
/*%if request.requestTypeId != null && request.requestTypeId != "" */
  AND aip.request_type_id = /* request.requestTypeId */''
/*%end */
/*%if request.approvalStatusId != null && request.approvalStatusId != "" */
  AND rpm.approval_status_id = /* request.approvalStatusId */''
/*%end */

    LIMIT /* request.pageSize */0
OFFSET /* request.offset */0