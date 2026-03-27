SELECT d.id                   AS asset_external_request_detail_id,
       d.asset_type_id,
       d.quantity,
       d.note,
       o.merchant,
       o.description,
       o.unit_price,
       o.warranty_period
FROM asset_external_request_detail d
         JOIN option_detail o
              ON o.asset_external_request_detail_id = d.id
                  AND o.is_selected = TRUE
WHERE d.asset_request_id = /* assetRequestId */0
ORDER BY d.id
