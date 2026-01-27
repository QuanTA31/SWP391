SELECT asset_code,
       description,
       original_price,
       warranty_period,
       received_date,
       location_id,
       asset_status_id,
       current_user_id,
       asset_type_id,
       COUNT(1) OVER() AS total_items
FROM assets

WHERE 1 = 1
/*%if request.locationId != null && request.locationId != "" */
  AND location_id = /* request.locationId */0
/*%end */
/*%if request.assetTypeId != null && request.assetTypeId != "" */
  AND asset_type_id = /* request.assetTypeId */''
/*%end */
/*%if request.assetStatusId != null && request.assetStatusId != "" */
  AND asset_status_id = /* request.assetStatusId */''
/*%end */

LIMIT /* request.pageSize */0
OFFSET /* request.offset */0