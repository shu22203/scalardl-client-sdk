package com.org1.contract;

import com.scalar.ledger.asset.Asset;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.ledger.Ledger;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.Optional;

public class StateTransfer extends Contract {

  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> properties) {
    String fromAssetId = argument.getString("from");
    String toAssetId = argument.getString("to");
    int state = argument.getInt("state");

    Optional<Asset> fromAsset = ledger.get(fromAssetId);
    Optional<Asset> toAsset = ledger.get(toAssetId);

    if (!fromAsset.isPresent()) return null;
    if (fromAsset.get().data().getInt("state") - state < 0) return null;

    int fromAfterState = fromAsset.get().data().getInt("state") - state;
    int toAfterState = toAsset.map(asset -> asset.data().getInt("state") + state).orElse(state);

    ledger.put(fromAssetId, Json.createObjectBuilder().add("state", fromAfterState).build());
    ledger.put(toAssetId, Json.createObjectBuilder().add("state", toAfterState).build());

    return null;
  }
}
