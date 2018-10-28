import React from "react";
import Account from "./account";
import Fieldset from "../../../fieldset";

const TRANSACTION_TYPES = [
  "SERVICES",
  "ENTERTAINMENT",
  "GROCERIES",
  "HEALTH",
  "RESTAURANTS",
  "SHOPPING",
  "TRANSPORT",
  "TRAVEL",
  "UTILITIES",
  "CASH",
  "TRANSFERS",
  "WEALTH",
  "INSURANCE",
  "GENERAL"
];
const Aggregate = () => (
  <>
    <Account name="from" />
    <Account name="to" />
    <Fieldset name="amount" type="numeric" />
    <Fieldset name="type" component="select">
      <option value="" />
      {TRANSACTION_TYPES.map(type => (
        <option value={type} key={type}>
          {type}
        </option>
      ))}
    </Fieldset>
  </>
);

export default Aggregate;
