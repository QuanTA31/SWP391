SELECT axp.asset_id,
       axp.asset_type_id,
       axp.request_status_id,
       axp.request_type_id,
       axp.quantity,
       axp.handover_date,
       axp.note,
       rpm.approval_status_id AS approval_status_id,
       od.approval_status_id AS option_detail,
       COUNT(1) OVER() AS total_items
FROM asset_external_process AS axp
         INNER JOIN request_progress_management AS rpm
                    ON axp.request_progress_management_id = rpm.id
         LEFT JOIN option_detail AS od
                   ON axp.id = od.asset_external_process_id

WHERE 1 = 1
/*%if request.requestStatusId != null && request.requestStatusId != "" */
  AND axp.request_status_id = /* request.requestStatusId */0
/*%end */
/*%if request.requestTypeId != null && request.requestTypeId != "" */
  AND axp.request_type_id = /* request.requestTypeId */''
/*%end */
/*%if request.approvalStatusId != null && request.approvalStatusId != "" */
  AND rpm.approval_status_id = /* request.approvalStatusId */''
/*%end */

    LIMIT /* request.pageSize */0
OFFSET /* request.offset */0