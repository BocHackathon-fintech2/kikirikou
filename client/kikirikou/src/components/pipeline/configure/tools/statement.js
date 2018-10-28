import React from "react";
import Account from "./account";
import Fieldset from "../../../fieldset";

const Statement = () => (
  <>
    <Account name="account" />
    <Fieldset name="from" type="date" />
    <Fieldset name="to" type="date" />
  </>
);

export default Statement;
