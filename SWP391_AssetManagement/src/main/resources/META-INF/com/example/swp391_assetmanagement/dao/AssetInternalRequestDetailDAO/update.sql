UPDATE asset_internal_request_detail
SET asset_type_id    = /* detail.assetTypeId */'',
    quantity         = /* detail.quantity */0,
    to_location_id   = /* detail.toLocationId */'',
    note             = /* detail.note */''
WHERE asset_request_id = /* detail.assetRequestId */0
