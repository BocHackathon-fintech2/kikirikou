import Fieldset from "../../../fieldset";
import accounts from "../../../other/accounts";
import React from "react";

const Account = ({ name }) => (
  <Fieldset name={name} component="select">
    <option value="" />
    {Object.entries(accounts).map(([id, name]) => (
      <option key={id} value={id}>
        {name}
      </option>
    ))}
  </Fieldset>
);

export default Account;
