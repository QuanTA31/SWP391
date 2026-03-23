UPDATE asset_internal_request_detail
SET asset_id        = /* detail.assetId */0,
    asset_type_id   = /* detail.assetTypeId */'',
    quantity        = /* detail.quantity */0,
    to_location_id  = /* detail.toLocationId */'',
    note            = /* detail.note */'',
    is_done         = /* detail.isDone */null
WHERE id = /* detail.id */0
