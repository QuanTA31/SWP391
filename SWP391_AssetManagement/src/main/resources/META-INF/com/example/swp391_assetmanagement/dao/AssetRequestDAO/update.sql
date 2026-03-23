UPDATE asset_request
SET request_status_id = /* assetRequest.requestStatusId */''
    /*%if assetRequest.approvedBy != null && assetRequest.approvedBy != "" */
  , approved_by       = /* assetRequest.approvedBy */1
    /*%end */
    /*%if assetRequest.note != null && assetRequest.note != "" */
  , note              = /* assetRequest.note */''
    /*%end */
    /*%if assetRequest.approvedDate != null */
  , approved_date     = /* assetRequest.approvedDate */''
    /*%end */
    /*%if assetRequest.handoverDate != null*/
  , handover_date     = /* assetRequest.handoverDate */''
    /*%end */
WHERE id = /* assetRequest.id */1
