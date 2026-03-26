SELECT a.asset_code,
       a.description,
       a.original_price,
       a.warranty_period,
       a.received_date,
       a.location_id,
       a.asset_status_id,
       a.current_user_id,
       ud.name AS current_user_name,
       a.asset_type_id,
       COUNT(1) OVER() AS total_items
FROM assets as a
         LEFT JOIN user_detail AS ud ON ud.user_id = a.current_user_id

WHERE a.location_id IN /* request.locationIdList */()
/*%if request.locationId != null && request.locationId != "" */
  AND a.location_id = /* request.locationId */0
/*%end */
/*%if request.assetTypeId != null && request.assetTypeId != "" */
  AND a.asset_type_id = /* request.assetTypeId */''
/*%end */
/*%if request.assetStatusId != null && request.assetStatusId != "" */
  AND a.asset_status_id = /* request.assetStatusId */''
/*%end */
/*%if request.searchWord != null && request.searchWord != "" */
  AND a.asset_code LIKE CONCAT('%', /* request.searchWord */'', '%')
/*%end */

LIMIT /* request.pageSize */0
OFFSET /* request.offset */0
