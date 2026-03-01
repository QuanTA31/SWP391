SELECT COUNT(1)
FROM option_detail AS od
INNER JOIN asset_external_request_detail AS aerd ON od.asset_external_request_detail_id = aerd.id
INNER JOIN asset_request AS ar ON ar.id = aerd.asset_request_id
WHERE ar.id = /* assetRequestId */0
AND od.asset_external_request_detail_id != /* requestDetailId */0
AND od.is_selected IS NULL