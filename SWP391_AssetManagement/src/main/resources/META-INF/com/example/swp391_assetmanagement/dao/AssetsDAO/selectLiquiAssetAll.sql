SELECT a.id,
       a.asset_code,
       a.description,
       a.original_price,
       a.warranty_period,
       a.received_date,
       a.location_id,
       a.asset_status_id,
       a.current_user_id,
       a.asset_type_id,
/*%if request.assetRequestId != null */
       CASE
           WHEN d.asset_id IS NOT NULL THEN TRUE
           ELSE FALSE
           END AS is_selected,
/*%end */
       COUNT(1)   OVER() AS total_items
FROM assets a
/*%if request.assetRequestId != null */
         LEFT JOIN asset_internal_request_detail d ON a.id = d.asset_id
/*%end */

WHERE a.location_id = '05'
AND a.asset_status_id IN ('01', '08')
/*%if request.assetRequestId != null */
  AND d.asset_request_id = /* request.assetRequestId */0
/*%end */
/*%if request.assetTypeId != null && request.assetTypeId != "" */
  AND a.asset_type_id = /* request.assetTypeId */''
/*%end */
/*%if request.searchWord != null && request.searchWord != "" */
  AND a.asset_code LIKE CONCAT('%', /* request.searchWord */'', '%')
/*%end */

    LIMIT /* request.pageSize */0
OFFSET /* request.offset */0
