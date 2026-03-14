SELECT aex.asset_request_id
FROM option_detail AS op
    INNER JOIN asset_external_request_detail AS aex
    ON op.asset_external_request_detail_id = aex.id
WHERE /* OptionDetailId */''