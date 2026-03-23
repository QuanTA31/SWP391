UPDATE asset_request
SET
    request_status_id = /* assetRequest.requestStatusId */''
    /*%if assetRequest.handoverDate != null*/
  , handover_date     = /* assetRequest.handoverDate */'2024-01-01'
    /*%end */
WHERE id = /* assetRequest.id */1