SELECT
    a.id as assetId,
    a.asset_code as assetCode,
    at.name as assetTypeName,
    od.merchant as merchant,
    od.unit_price as unitPrice,
    od.description as description,
    od.approved_date as approvedDate
FROM assets a
JOIN asset_type at ON a.asset_type_id = at.id
JOIN assets_asset_request_external aare ON a.id = aare.assetId
JOIN asset_external_request_detail aerd ON aare.assetExternalRequestDetailId = aerd.id
JOIN option_detail od ON aerd.id = od.asset_external_request_detail_id
WHERE aerd.asset_request_id = /* requestId */0
  AND od.is_selected = 1
  /*%if assetTypeId != null && assetTypeId != "" */
  AND a.asset_type_id = /* assetTypeId */'01'
  /*%end*/
  /*%if searchWord != null && searchWord != "" */
  AND a.asset_code LIKE /* @infix(searchWord) */'%A%'
  /*%end*/
ORDER BY a.asset_code
LIMIT /* limit */10 OFFSET /* offset */0
