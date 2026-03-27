SELECT SUM(od.unit_price)
FROM assets a
JOIN assets_asset_request_external aare ON a.id = aare.assetId
JOIN asset_external_request_detail aerd ON aare.assetExternalRequestDetailId = aerd.id
JOIN option_detail od ON aerd.id = od.asset_external_request_detail_id
WHERE aerd.asset_request_id = /* requestId */0
  AND od.is_selected = 1
