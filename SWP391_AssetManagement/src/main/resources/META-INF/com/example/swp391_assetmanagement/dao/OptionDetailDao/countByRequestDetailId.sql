SELECT COUNT(1)
FROM option_detail
WHERE asset_external_request_detail_id = /* requestDetailId */0
/*%if isSelected != null */
  AND is_selected = /* isSelected */true
/*%end*/
