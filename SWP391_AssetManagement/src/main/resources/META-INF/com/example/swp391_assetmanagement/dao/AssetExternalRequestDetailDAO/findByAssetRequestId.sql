SELECT request.id,
       request.asset_type_id,
       request.quantity,
       od.warranty_period,
       od.unit_price,
       od.description
FROM asset_external_request_detail AS request
         INNER JOIN option_detail AS od ON request.id = od.asset_external_request_detail_id
INNER JOIN asset_request on asset_request.id = request.asset_request_id
WHERE request.asset_request_id = /* assetRequestId */0
  AND asset_request.request_status_id = '08'
  AND od.is_selected = TRUE
