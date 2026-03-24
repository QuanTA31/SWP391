UPDATE assets
SET asset_status_id = /* status */'DISPOSED'
WHERE id IN (
    SELECT aare.assetId
    FROM assets_asset_request_external aare
    JOIN asset_external_request_detail aerd
        ON aare.assetExternalRequestDetailId = aerd.id
    WHERE aerd.asset_request_id = /* requestId */0
)